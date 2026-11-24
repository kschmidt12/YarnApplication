package controller;


import auth.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.User;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import persistence.GenericDao;
import util.PropertiesLoader;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;


@WebServlet(
        urlPatterns = {"/auth"}
)
// TODO if something goes wrong it this process, route to an error page. Currently, errors are only caught and logged.
/**
 * Inspired by: https://stackoverflow.com/questions/52144721/how-to-get-access-token-using-client-credentials-using-java-code
 */

public class Auth extends HttpServlet implements PropertiesLoader {
    Properties properties;
    String CLIENT_ID;
    String CLIENT_SECRET;
    String OAUTH_URL;
    String LOGIN_URL;
    String REDIRECT_URL;
    String REGION;
    String POOL_ID;
    Keys jwks;

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public void init() throws ServletException {
        super.init();
        loadProperties();
        loadKey();
    }

    /**
     * Gets the auth code from the request and exchanges it for a token containing user info.
     * @param req servlet request
     * @param resp servlet response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String authCode = req.getParameter("code");

        if (authCode == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Missing authorization code.");
            return;
        }

        try {
            HttpRequest authRequest = buildAuthRequest(authCode);
            TokenResponse tokenResponse = getToken(authRequest);

            DecodedJWT jwt = validate(tokenResponse);

            User user = checkUser(jwt);

            req.getSession();
            req.changeSessionId();
            req.getSession().setAttribute("userName", user.getUserName());
            req.getSession().setAttribute("userId", user.getId());

            RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");
            dispatcher.forward(req, resp);

        } catch (JWTVerificationException e) {
            logger.error("Token validation failed.", e);
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);

        } catch (IOException e) {
            logger.error("Error getting or validating token.", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Error getting token from Cognito.", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        } catch (RuntimeException e) {
            logger.error("Error processing user login.", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private TokenResponse getToken(HttpRequest authRequest)
            throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(
                authRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Cognito token request failed.");
        }

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(response.body(), TokenResponse.class);
    }

    /**
     * Validates the Cognito ID token.
     *
     * @param tokenResponse the response from Cognito
     * @return the verified JWT
     * @throws IOException if token validation cannot be completed
     */
    private DecodedJWT validate(TokenResponse tokenResponse) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        CognitoTokenHeader tokenHeader = mapper.readValue(
                CognitoJWTParser.getHeader(tokenResponse.getIdToken()).toString(),
                CognitoTokenHeader.class);

        String keyId = tokenHeader.getKid();

        // Find the correct signing key
        KeysItem signingKey = null;

        for (KeysItem key : jwks.getKeys()) {
            if (keyId.equals(key.getKid())) {
                signingKey = key;
                break;
            }
        }

        if (signingKey == null) {
            throw new IOException("Unable to find Cognito signing key.");
        }

        BigInteger modulus = new BigInteger(1,
                org.apache.commons.codec.binary.Base64.decodeBase64(
                        signingKey.getN()));

        BigInteger exponent = new BigInteger(1,
                org.apache.commons.codec.binary.Base64.decodeBase64(
                        signingKey.getE()));

        PublicKey publicKey;

        try {
            publicKey = KeyFactory.getInstance("RSA")
                    .generatePublic(new RSAPublicKeySpec(modulus, exponent));

        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new IOException("Unable to generate public key.", e);
        }

        Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicKey, null);

        String iss = String.format(
                "https://cognito-idp.%s.amazonaws.com/%s", REGION, POOL_ID);

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(iss)
                .withAudience(CLIENT_ID)
                .withClaim("token_use", "id")
                .build();

        return verifier.verify(tokenResponse.getIdToken());
    }

    /** Create the auth url and use it to build the request.
     *
     * @param authCode auth code received from Cognito as part of the login process
     * @return the constructed oauth request
     */
    private HttpRequest buildAuthRequest(String authCode) {
        String keys = CLIENT_ID + ":" + CLIENT_SECRET;

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("grant_type", "authorization_code");
        parameters.put("client_id", CLIENT_ID);
        parameters.put("code", authCode);
        parameters.put("redirect_uri", REDIRECT_URL);

        String form = parameters.keySet().stream()
                .map(key -> key + "=" + URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        String encoding = Base64.getEncoder().encodeToString(keys.getBytes());

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(OAUTH_URL))
                .headers("Content-Type", "application/x-www-form-urlencoded", "Authorization", "Basic " + encoding)
                .POST(HttpRequest.BodyPublishers.ofString(form)).build();
        return request;
    }

    /**
     * Gets the JSON Web Key Set (JWKS) for the user pool from cognito and loads it
     * into objects for easier use.
     *
     * JSON Web Key Set (JWKS) location: https://cognito-idp.{region}.amazonaws.com/{userPoolId}/.well-known/jwks.json
     * Demo url: https://cognito-idp.us-east-2.amazonaws.com/us-east-2_XaRYHsmKB/.well-known/jwks.json
     *
     * @see Keys
     * @see KeysItem
     */
    private void loadKey() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            URL jwksURL = new URL(String.format("https://cognito-idp.%s.amazonaws.com/%s/.well-known/jwks.json", REGION, POOL_ID));
            File jwksFile = new File("jwks.json");
            FileUtils.copyURLToFile(jwksURL, jwksFile);
            jwks = mapper.readValue(jwksFile, Keys.class);
            logger.debug("Keys are loaded. Here's e: " + jwks.getKeys().get(0).getE());
        } catch (IOException ioException) {
            logger.error("Cannot load json..." + ioException.getMessage(), ioException);
        } catch (Exception e) {
            logger.error("Error loading json" + e.getMessage(), e);
        }
    }

    /**
     * Read in the cognito props file and get/set the client id, secret, and required urls
     * for authenticating a user.
     */
    // TODO This code appears in a couple classes, consider using a startup servlet similar to adv java project
    private void loadProperties() {
        try {
            properties = loadProperties("/cognito.properties");
            CLIENT_ID = properties.getProperty("client.id");
            CLIENT_SECRET = properties.getProperty("client.secret");
            OAUTH_URL = properties.getProperty("oauthURL");
            LOGIN_URL = properties.getProperty("loginURL");
            REDIRECT_URL = properties.getProperty("redirectURL");
            REGION = properties.getProperty("region");
            POOL_ID = properties.getProperty("poolId");
        } catch (Exception e) {
            logger.error("Error loading properties" + e.getMessage(), e);
        }
    }

    /**
     * Retrieves an existing user or creates a new user
     * using verified Cognito information.
     *
     * @param jwt the verified Cognito token
     * @return the database user
     */
    private User checkUser(DecodedJWT jwt) {

        String userName = jwt.getClaim("cognito:username").asString();
        String email = jwt.getClaim("email").asString();
        String name = jwt.getClaim("name").asString();

        if (userName == null || userName.isBlank()) {
            throw new IllegalStateException("Missing Cognito username.");
        }

        GenericDao<User> userDao = new GenericDao<>(User.class);

        List<User> users = userDao.findByPropertyEqual("userName", userName);

        if (users.size() > 1) {
            throw new IllegalStateException("Duplicate database users.");
        }

        if (users.isEmpty()) {

            User user = new User();

            user.setUserName(userName);
            user.setEmail(email);
            user.setName(name == null || name.isBlank() ? userName : name);

            int id = userDao.insert(user);
            user.setId(id);

            return user;
        }

        return users.get(0);
    }
}


package persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.TavilyResponse;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;

import java.util.Map;

public class TavilyDao {

    public TavilyResponse searchProjects(String yarnWeight) throws Exception {

        String apiKey = System.getenv("TAVILY_API_KEY");
        String query = "free crochet patterns using weight " + yarnWeight + " yarn";

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("https://api.tavily.com/search");
        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(
                Map.of("query", query, "max_results", 5)
        );

        try {
            String response = target.request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + apiKey)
                    .post(Entity.json(json), String.class);

            return mapper.readValue(response, TavilyResponse.class);
        } finally {
            client.close();
        }
    }
}
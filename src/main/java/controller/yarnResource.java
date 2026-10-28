package controller;

import entity.Yarn;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.GenericDao;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/yarn")
public class yarnResource {

    private final GenericDao<Yarn> yarnDao = new GenericDao<>(Yarn.class);

    @GET
    @Produces("application/json")
    public Response getAllYarn() {

        List<Yarn> yarns = yarnDao.getAll();
        JSONArray jsonArray = new JSONArray();

        for (Yarn yarn : yarns) {
            JSONObject json = new JSONObject();

            json.put("id", yarn.getId());
            json.put("brandName", yarn.getBrandName());
            json.put("color", yarn.getColor());
            json.put("yarn amount", yarn.getYarnAmount());
            json.put("yarn size", yarn.getYarnSize());
            json.put("hook size", yarn.getHookSize());
            json.put("bought from", yarn.getBoughtFrom());

            jsonArray.put(json);
        }
        return Response.status(200)
                .entity(jsonArray.toString())
                .build();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response getYarnById(@PathParam("id") int id) {

        Yarn yarn = yarnDao.getById(id);
        if (yarn == null) {
            return Response.status(404).build();
        }

        JSONObject json = new JSONObject();
        json.put("id", yarn.getId());
        json.put("brandName", yarn.getBrandName());
        json.put("color", yarn.getColor());
        json.put("yarn amount", yarn.getYarnAmount());
        json.put("yarn size", yarn.getYarnSize());
        json.put("hook size", yarn.getHookSize());
        json.put("bought from", yarn.getBoughtFrom());

        return Response.status(200)
                .entity(json.toString())
                .build();
    }
}

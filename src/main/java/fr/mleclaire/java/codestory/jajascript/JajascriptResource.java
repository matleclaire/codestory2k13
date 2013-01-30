package fr.mleclaire.java.codestory.jajascript;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Path("jajascript")
public class JajascriptResource {

    @POST
    @Path("optimize")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public Response optimize(String content) throws IOException {

        List flights = new ObjectMapper().readValue(content, new TypeReference<List<Flight>>(){});
        Candidate result = Jajascript.optimize(flights);
        return Response.ok(result).status(201).build();
    }
}

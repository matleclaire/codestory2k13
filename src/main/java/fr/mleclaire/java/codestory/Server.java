package fr.mleclaire.java.codestory;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;

import org.glassfish.grizzly.http.server.HttpServer;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Minimalist http server with Grizzly
 *
 * @author mathieu
 */

@Path("/")
public class Server {

    private static final String EMAIL_PARAM = "Quelle est ton adresse email";

    private HttpServer httpServer;
   
    @GET
    @Produces({"text/plain", "text/html"})
    public Response email(@DefaultValue("") @QueryParam("q") String q) {
         if (q.equals(EMAIL_PARAM))
            return Response.ok("mat.leclaire@gmail.com","text/plain")
                    .header("Server", "AllYourBaseAreBelongToUs")
                    .build();
         else
            return Response.ok(getClass().getResourceAsStream( "/web/index.html")).build();
    }

    public void start(String name, int port) throws IOException {
        httpServer = GrizzlyServerFactory.createHttpServer(String.format("http://%s:%d/", name, port));
        httpServer.start();
    }

    public void stop() {
        httpServer.stop();
    }

}

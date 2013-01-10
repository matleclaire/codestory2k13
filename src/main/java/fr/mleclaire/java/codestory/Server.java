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
    private static final String MAILING_LIST_PARAM = "Es tu abonne a la mailing list(OUI/NON)";
    private static final String HAPPY_PARAM = "Es tu heureux de participer(OUI/NON)";


    private HttpServer httpServer;
   
    @GET
    @Produces({"text/plain", "text/html"})
    public Response email(@DefaultValue("") @QueryParam("q") String q) {
         if (q.equals(EMAIL_PARAM))
            return Response.ok("mat.leclaire@gmail.com","text/plain").build();
         else if (q.equals(MAILING_LIST_PARAM) || q.equals(HAPPY_PARAM))
            return Response.ok("OUI","text/plain").build();
         else
            return Response.ok(getClass().getResourceAsStream( "/web/index.html")).build();
    }

    public void start(String name, int port) throws IOException {
        httpServer = GrizzlyServerFactory.createHttpServer(String.format("http://%s:%d/", name, port));
        httpServer.start();

        // To log request from client
        TrafficMonitor.log(httpServer);
    }

    public void stop() {
        httpServer.stop();
    }

}

package fr.mleclaire.java.codestory;
import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;

import org.glassfish.grizzly.http.server.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Minimalist http server with Grizzly
 *
 * @author mathieu
 */

@Path("/")
public class Server {

    static final Logger logger = LoggerFactory.getLogger(Server.class);

    private static final String EMAIL_PARAM         = "Quelle est ton adresse email";
    private static final String MAILING_LIST_PARAM  = "Es tu abonne a la mailing list(OUI/NON)";
    private static final String HAPPY_PARAM         = "Es tu heureux de participer(OUI/NON)";
    private static final String CHALLENGE_PARAM     = "Es tu pret a recevoir une enonce au format markdown par http post(OUI/NON)";
    private static final String TROLL_PARAM         = "Est ce que tu reponds toujours oui(OUI/NON)";
    private HttpServer httpServer;
   
    @GET
    @Produces({"text/plain", "text/html"})
    public Response email(@DefaultValue("") @QueryParam("q") String q) {
         if (q.equals(EMAIL_PARAM))
            return Response.ok("mat.leclaire@gmail.com", "text/plain").build();
         else if (q.equals(MAILING_LIST_PARAM)
                 || q.equals(HAPPY_PARAM)
                 || q.equals(CHALLENGE_PARAM))
            return Response.ok("OUI","text/plain").build();
         else if (q.equals(TROLL_PARAM))
             return Response.ok("NON","text/plain").build();
         else
             return Response.ok(getClass().getResourceAsStream( "/web/index.html")).build();
    }


    @POST
    @Consumes("text/plain")
    public Response acceptChallenge(String content) {
        File newTextFile = new File("challenge.md");
        try {
            FileWriter fileWriter = new FileWriter(newTextFile);
            fileWriter.write(content);
            fileWriter.close();
            return Response.status(201).build();
        } catch (IOException e) {
            logger.error("failed to save challenge in a file",e);
        }
        return Response.status(500).build();
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

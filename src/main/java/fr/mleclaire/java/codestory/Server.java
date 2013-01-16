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

public class Server {

    static final Logger logger = LoggerFactory.getLogger(Server.class);

    private HttpServer httpServer;

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

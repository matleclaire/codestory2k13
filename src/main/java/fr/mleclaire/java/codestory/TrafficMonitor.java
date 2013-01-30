package fr.mleclaire.java.codestory;

import org.glassfish.grizzly.filterchain.FilterChain;
import org.glassfish.grizzly.http.*;
import org.glassfish.grizzly.http.server.*;
import org.glassfish.grizzly.Buffer;
import org.glassfish.grizzly.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Log HTTP traffic (request and response) 
 * 
 *  @author test
 */


public class TrafficMonitor {

    static final Logger logger = LoggerFactory.getLogger(TrafficMonitor.class);


    public static void log(HttpServer httpServer) {




        httpServer.getServerConfiguration().addHttpHandler(new StaticHttpHandler());
        FilterChain filterChain = httpServer.getListener("grizzly").getFilterChain();

        // Get HttpCodecFilter
        HttpCodecFilter codecFilter = (HttpCodecFilter) filterChain.get(filterChain.indexOfType(HttpCodecFilter.class));
        codecFilter.getMonitoringConfig().addProbes(new HttpProbe.Adapter() {
            public void onDataReceivedEvent(Connection connection, Buffer buffer) {
                // Log incoming traffic - CodeStory is watching us !
                logger.debug("Incomming Request ({}) :\n{}", connection.getPeerAddress(), buffer.toStringContent());
            }

            public void onDataSentEvent(Connection connection, Buffer buffer) {
                // Log outgoing traffic
                logger.debug("Response :\n{}",buffer.toStringContent());
            }
        });
        
	}
}
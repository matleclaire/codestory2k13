package fr.mleclaire.java.codestory;

import java.io.IOException;

/**
 * Main app to launch server
 *
 * @author mathieu
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        Server serv = new Server();
        serv.start("codestory2k13.miniserv.fr", 7070);
        System.out.print("Press any key to quit...");
        System.in.read();
        serv.stop();
    }
}

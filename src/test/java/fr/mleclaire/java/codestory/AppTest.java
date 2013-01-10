package fr.mleclaire.java.codestory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Unit test App.
 */
public class AppTest {

    private final static String HOSTNAME = "localhost";
    private final static int PORT = 7070;

    private Server server;
    WebResource service;

    @Before
    public void setUp() throws Exception {
        // Starting server
        server = new Server();
        server.start(HOSTNAME, PORT);

        //Starting client
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        service = client.resource(String.format("http://%s:%d/", HOSTNAME, PORT));
    }

    @After
    public void tearDown() {
        server.stop();
    }

    @Test
    public void should_show_email() {
        String result = service.path("/").queryParam("q","Quelle est ton adresse email").accept("text/plain").get(String.class);
        assertThat(result).isEqualTo("mat.leclaire@gmail.com");
    }

    @Test
    public void should_confirm_mailing_list_subscription() {
        String result = service.path("/").queryParam("q","Es tu abonne a la mailing list(OUI/NON)").accept("text/plain").get(String.class);
        assertThat(result).isEqualTo("OUI");
    }

}

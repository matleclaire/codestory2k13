package fr.mleclaire.java.codestory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.junit.*;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Unit test App.
 */
public class AppTest {

    private final static String HOSTNAME = "localhost";
    private final static int PORT = 7071;

    private static Server server;
    private static WebResource service;

    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        // Starting server
        server = new Server();
        server.start(HOSTNAME, PORT);

        //Starting client
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        service = client.resource(String.format("http://%s:%d/", HOSTNAME, PORT));
    }

    @AfterClass
    public static void oneTimeTearDown() {
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

    @Test
    public void should_confirm_happiness() {
        String result = service.path("/").queryParam("q","Es tu heureux de participer(OUI/NON)").accept("text/plain").get(String.class);
        assertThat(result).isEqualTo("OUI");
    }

    @Test
    public void should_accept_POST_challenge() {
        String result = service.path("/").queryParam("q","Es tu pret a recevoir une enonce au format markdown par http post(OUI/NON)").accept("text/plain").get(String.class);
        assertThat(result).isEqualTo("OUI");
    }

    @Test
    public void should_receive_POST_challenge() {
        ClientResponse response = service.path("/enonce/1").type("application/x-www-form-urlencoded").post(ClientResponse.class, "fake challenge");
        assertThat(response.getStatus()).isEqualTo(201);
    }

    @Test
    public void should_answer_no() {
        String result = service.path("/").queryParam("q","Est ce que tu reponds toujours oui(OUI/NON)").accept("text/plain").get(String.class);
        assertThat(result).isEqualTo("NON");
    }

    @Test
    public void should_change_cents() {
        String result = service.path("/scalaskel/change/7").accept("application/json").get(String.class);
        // Test both ways
        assertThat(result.trim()).isIn("[{\"foo\":7},{\"bar\":1}]", "[{\"bar\":1},{\"foo\":7}]");
    }
}

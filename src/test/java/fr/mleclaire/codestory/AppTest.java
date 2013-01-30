package fr.mleclaire.codestory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import fr.mleclaire.codestory.Server;
import fr.mleclaire.codestory.jajascript.Candidate;
import fr.mleclaire.codestory.jajascript.Flight;
import org.junit.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;


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
    public void should_receive_challenge_1() {
        ClientResponse response = service.path("/enonce/1").type("application/x-www-form-urlencoded").post(ClientResponse.class, "fake challenge");
        assertThat(response.getStatus()).isEqualTo(201);
    }

    @Test
    public void should_answer_no() {
        String result = service.path("/").queryParam("q","Est ce que tu reponds toujours oui(OUI/NON)").accept("text/plain").get(String.class);
        assertThat(result).isEqualTo("NON");
    }

    @Test
    public void should_confirm_challenge_reception_challenge_1() {
        String result = service.path("/").queryParam("q","As tu bien recu le premier enonce(OUI/NON)").accept("text/plain").get(String.class);
        assertThat(result).isEqualTo("OUI");
    }

    @Test
    public void should_change_cents() {
        String result = service.path("/scalaskel/change/7").accept("application/json").get(String.class);
        // Test both ways
        assertThat(result.trim()).isIn("[{\"foo\":7},{\"bar\":1}]", "[{\"bar\":1},{\"foo\":7}]");
    }

    @Test
    public void should_add_one_and_one() {
        String result = service.path("/").queryParam("q","1 1").accept("text/plain").get(String.class);
        assertThat(result).isEqualTo("2");
    }

    @Test
    public void should_add_two_and_two() {
        String result = service.path("/").queryParam("q","2 2").accept("text/plain").get(String.class);
        assertThat(result).isEqualTo("4");
    }

    @Test
    public void should_compute() {
        String result = service.path("/").queryParam("q","3/2").accept("text/plain").get(String.class);
        assertThat(result).isEqualTo("1,5");
    }

    @Test
    public void should_compute_again() {
        String result = service.path("/").queryParam("q","6/2").accept("text/plain").get(String.class);
        assertThat(result).isEqualTo("3");
    }

    @Test
    public void should_compute_without_exponent() {
        String result = service.path("/").queryParam("q","As tu passe une bonne nuit malgre les bugs de l etape precedente(PAS_TOP/BOF/QUELS_BUGS)").accept("text/plain").get(String.class);
        assertThat(result).isEqualTo("QUELS_BUGS");
    }

    @Test
    public void should_receive_challenge_2() {
        ClientResponse response = service.path("/enonce/1").type("application/x-www-form-urlencoded").post(ClientResponse.class, "fake challenge");
        assertThat(response.getStatus()).isEqualTo(201);
    }

    @Test
    public void should_confirm_challenge_reception_challenge_2() {
        String result = service.path("/").queryParam("q","As tu bien recu le second enonce(OUI/NON)").accept("text/plain").get(String.class);
        assertThat(result).isEqualTo("OUI");
    }

    @Test
    public void should_optimize_jajascript_simple() {
        String payload = "[ {\"VOL\": \"AF514\", \"DEPART\":0, \"DUREE\":5, \"PRIX\": 10} ]";
        ClientResponse response = service.path("/jajascript/optimize").type("application/x-www-form-urlencoded").post(ClientResponse.class, payload);

        String output = response.getEntity(String.class);
        System.out.println(output);
        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(output).isEqualTo("{\"gain\":10,\"path\":[\"AF514\"]}");
    }

    @Test
    public void should_optimize_jajascript_sample() {
        String payload = "[ {\"VOL\": \"AF514\", \"DEPART\":0, \"DUREE\":5, \"PRIX\": 10}, \n" +
                "{\"VOL\": \"CO5\", \"DEPART\":3, \"DUREE\":7, \"PRIX\": 14}, \n" +
                "{\"VOL\": \"AF515\", \"DEPART\":5, \"DUREE\":9, \"PRIX\": 7}, \n" +
                "{\"VOL\": \"BA01\", \"DEPART\":6, \"DUREE\":9, \"PRIX\": 8} ]";
        ClientResponse response = service.path("/jajascript/optimize").type("application/x-www-form-urlencoded").post(ClientResponse.class, payload);

        String output = response.getEntity(String.class);
        System.out.println(output);
        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(output).isEqualTo("{\"gain\":18,\"path\":[\"AF514\",\"BA01\"]}");
    }

    @Test
    public void should_optimize_jajascript_payload1() {
        String payload = "[ { \"VOL\": \"panicky-sieve-17\", \"DEPART\": 2, \"DUREE\": 2, \"PRIX\": 8 }, { \"VOL\": \"cooing-juggler-2\", \"DEPART\": 2, \"DUREE\": 1, \"PRIX\": 7 }, { \"VOL\": \"harsh-surfboard-58\", \"DEPART\": 1, \"DUREE\": 9, \"PRIX\": 1 }, { \"VOL\": \"brainy-sorrow-85\", \"DEPART\": 4, \"DUREE\": 1, \"PRIX\": 14 }, { \"VOL\": \"gorgeous-pollution-89\", \"DEPART\": 0, \"DUREE\": 8, \"PRIX\": 1 }, { \"VOL\": \"flat-shrubbery-7\", \"DEPART\": 5, \"DUREE\": 3, \"PRIX\": 11 }, { \"VOL\": \"tall-plow-35\", \"DEPART\": 8, \"DUREE\": 7, \"PRIX\": 22 }, { \"VOL\": \"thoughtful-ivy-2\", \"DEPART\": 9, \"DUREE\": 7, \"PRIX\": 3 }, { \"VOL\": \"fantastic-thinner-24\", \"DEPART\": 9, \"DUREE\": 3, \"PRIX\": 13 }, { \"VOL\": \"frightened-minicam-82\", \"DEPART\": 8, \"DUREE\": 10, \"PRIX\": 7 }, { \"VOL\": \"filthy-quagmire-34\", \"DEPART\": 12, \"DUREE\": 2, \"PRIX\": 3 }, { \"VOL\": \"quick-vermin-78\", \"DEPART\": 14, \"DUREE\": 6, \"PRIX\": 10 }, { \"VOL\": \"relieved-block-1\", \"DEPART\": 12, \"DUREE\": 9, \"PRIX\": 9 }, { \"VOL\": \"faithful-nun-60\", \"DEPART\": 13, \"DUREE\": 9, \"PRIX\": 11 }, { \"VOL\": \"calm-rugby-61\", \"DEPART\": 13, \"DUREE\": 5, \"PRIX\": 4 }, { \"VOL\": \"disturbed-penniless-45\", \"DEPART\": 19, \"DUREE\": 3, \"PRIX\": 25 }, { \"VOL\": \"friendly-grassland-59\", \"DEPART\": 19, \"DUREE\": 10, \"PRIX\": 8 }, { \"VOL\": \"busy-azalea-31\", \"DEPART\": 15, \"DUREE\": 5, \"PRIX\": 5 }, { \"VOL\": \"famous-syllable-12\", \"DEPART\": 15, \"DUREE\": 8, \"PRIX\": 6 }, { \"VOL\": \"voiceless-sortie-39\", \"DEPART\": 19, \"DUREE\": 20, \"PRIX\": 4 }, { \"VOL\": \"big-orangutan-85\", \"DEPART\": 20, \"DUREE\": 7, \"PRIX\": 15 }, { \"VOL\": \"depressed-paddle-76\", \"DEPART\": 24, \"DUREE\": 6, \"PRIX\": 23 }, { \"VOL\": \"crooked-undershorts-16\", \"DEPART\": 21, \"DUREE\": 6, \"PRIX\": 4 }, { \"VOL\": \"inquisitive-drop-47\", \"DEPART\": 23, \"DUREE\": 3, \"PRIX\": 15 }, { \"VOL\": \"anxious-spaghetti-29\", \"DEPART\": 23, \"DUREE\": 2, \"PRIX\": 2 }, { \"VOL\": \"tough-thunder-75\", \"DEPART\": 25, \"DUREE\": 8, \"PRIX\": 21 }, { \"VOL\": \"black-minus-8\", \"DEPART\": 27, \"DUREE\": 2, \"PRIX\": 14 }, { \"VOL\": \"miniature-punk-37\", \"DEPART\": 26, \"DUREE\": 4, \"PRIX\": 6 }, { \"VOL\": \"brave-birdseed-29\", \"DEPART\": 27, \"DUREE\": 2, \"PRIX\": 14 }, { \"VOL\": \"annoyed-bankroll-62\", \"DEPART\": 28, \"DUREE\": 17, \"PRIX\": 6 }, { \"VOL\": \"relieved-heart-12\", \"DEPART\": 31, \"DUREE\": 8, \"PRIX\": 28 }, { \"VOL\": \"confused-sociology-90\", \"DEPART\": 31, \"DUREE\": 5, \"PRIX\": 4 }, { \"VOL\": \"raspy-abalone-80\", \"DEPART\": 33, \"DUREE\": 3, \"PRIX\": 1 }, { \"VOL\": \"open-sextant-38\", \"DEPART\": 33, \"DUREE\": 10, \"PRIX\": 6 }, { \"VOL\": \"dizzy-minicam-38\", \"DEPART\": 32, \"DUREE\": 4, \"PRIX\": 3 }, { \"VOL\": \"chubby-hatchet-86\", \"DEPART\": 39, \"DUREE\": 9, \"PRIX\": 15 }, { \"VOL\": \"curious-voyage-32\", \"DEPART\": 38, \"DUREE\": 8, \"PRIX\": 4 }, { \"VOL\": \"loud-vase-79\", \"DEPART\": 37, \"DUREE\": 6, \"PRIX\": 5 }, { \"VOL\": \"selfish-funnel-69\", \"DEPART\": 36, \"DUREE\": 10, \"PRIX\": 11 }, { \"VOL\": \"depressed-knight-45\", \"DEPART\": 38, \"DUREE\": 6, \"PRIX\": 3 }, { \"VOL\": \"obedient-underclassman-82\", \"DEPART\": 42, \"DUREE\": 10, \"PRIX\": 15 }, { \"VOL\": \"slow-gentleman-64\", \"DEPART\": 43, \"DUREE\": 5, \"PRIX\": 10 }, { \"VOL\": \"upset-wart-8\", \"DEPART\": 40, \"DUREE\": 7, \"PRIX\": 2 }, { \"VOL\": \"shiny-tonsillitis-31\", \"DEPART\": 44, \"DUREE\": 6, \"PRIX\": 12 }, { \"VOL\": \"outrageous-storefront-41\", \"DEPART\": 41, \"DUREE\": 20, \"PRIX\": 2 }, { \"VOL\": \"rich-guerilla-51\", \"DEPART\": 45, \"DUREE\": 4, \"PRIX\": 18 }, { \"VOL\": \"sparkling-homemaker-54\", \"DEPART\": 49, \"DUREE\": 1, \"PRIX\": 20 }, { \"VOL\": \"soft-nap-38\", \"DEPART\": 47, \"DUREE\": 1, \"PRIX\": 9 }, { \"VOL\": \"excited-signboard-99\", \"DEPART\": 45, \"DUREE\": 10, \"PRIX\": 7 }, { \"VOL\": \"graceful-yard-51\", \"DEPART\": 49, \"DUREE\": 18, \"PRIX\": 6 }, { \"VOL\": \"comfortable-rag-41\", \"DEPART\": 52, \"DUREE\": 6, \"PRIX\": 16 }, { \"VOL\": \"gleaming-collar-6\", \"DEPART\": 52, \"DUREE\": 6, \"PRIX\": 9 }, { \"VOL\": \"alert-miter-39\", \"DEPART\": 54, \"DUREE\": 6, \"PRIX\": 2 }, { \"VOL\": \"nutty-bike-15\", \"DEPART\": 51, \"DUREE\": 8, \"PRIX\": 6 }, { \"VOL\": \"gleaming-ferret-31\", \"DEPART\": 53, \"DUREE\": 15, \"PRIX\": 3 }, { \"VOL\": \"innocent-rifleman-49\", \"DEPART\": 59, \"DUREE\": 5, \"PRIX\": 4 }, { \"VOL\": \"round-foot-11\", \"DEPART\": 59, \"DUREE\": 7, \"PRIX\": 4 }, { \"VOL\": \"dizzy-squirrel-58\", \"DEPART\": 58, \"DUREE\": 8, \"PRIX\": 9 }, { \"VOL\": \"bewildered-doctor-74\", \"DEPART\": 59, \"DUREE\": 5, \"PRIX\": 7 }, { \"VOL\": \"terrible-bench-33\", \"DEPART\": 58, \"DUREE\": 20, \"PRIX\": 6 }, { \"VOL\": \"clumsy-tailor-43\", \"DEPART\": 61, \"DUREE\": 5, \"PRIX\": 26 }, { \"VOL\": \"thankful-queue-63\", \"DEPART\": 63, \"DUREE\": 1, \"PRIX\": 20 }, { \"VOL\": \"fancy-pocket-9\", \"DEPART\": 62, \"DUREE\": 7, \"PRIX\": 4 }, { \"VOL\": \"rich-mice-97\", \"DEPART\": 62, \"DUREE\": 2, \"PRIX\": 6 }, { \"VOL\": \"prickly-pesticide-37\", \"DEPART\": 62, \"DUREE\": 19, \"PRIX\": 2 }, { \"VOL\": \"hushed-traction-42\", \"DEPART\": 68, \"DUREE\": 7, \"PRIX\": 20 }, { \"VOL\": \"cruel-vegetarian-7\", \"DEPART\": 67, \"DUREE\": 3, \"PRIX\": 11 }, { \"VOL\": \"curious-salad-80\", \"DEPART\": 65, \"DUREE\": 9, \"PRIX\": 6 }, { \"VOL\": \"colossal-garbage-88\", \"DEPART\": 68, \"DUREE\": 8, \"PRIX\": 14 }, { \"VOL\": \"colorful-quota-8\", \"DEPART\": 65, \"DUREE\": 8, \"PRIX\": 1 }, { \"VOL\": \"purring-void-40\", \"DEPART\": 71, \"DUREE\": 2, \"PRIX\": 2 }, { \"VOL\": \"skinny-pink-63\", \"DEPART\": 74, \"DUREE\": 4, \"PRIX\": 7 }, { \"VOL\": \"frantic-stereo-67\", \"DEPART\": 73, \"DUREE\": 3, \"PRIX\": 4 }, { \"VOL\": \"rich-zen-41\", \"DEPART\": 71, \"DUREE\": 10, \"PRIX\": 14 }, { \"VOL\": \"uptight-velour-83\", \"DEPART\": 74, \"DUREE\": 7, \"PRIX\": 5 }, { \"VOL\": \"fantastic-queue-49\", \"DEPART\": 78, \"DUREE\": 1, \"PRIX\": 13 }, { \"VOL\": \"stormy-billiards-99\", \"DEPART\": 76, \"DUREE\": 4, \"PRIX\": 5 }, { \"VOL\": \"vast-adhesive-92\", \"DEPART\": 77, \"DUREE\": 9, \"PRIX\": 4 }, { \"VOL\": \"bloody-hangar-82\", \"DEPART\": 76, \"DUREE\": 8, \"PRIX\": 10 }, { \"VOL\": \"prickly-rhino-10\", \"DEPART\": 75, \"DUREE\": 10, \"PRIX\": 2 }, { \"VOL\": \"elated-reactionary-6\", \"DEPART\": 80, \"DUREE\": 9, \"PRIX\": 20 }, { \"VOL\": \"motionless-cinnamon-66\", \"DEPART\": 81, \"DUREE\": 3, \"PRIX\": 18 }, { \"VOL\": \"old-fashioned-cowhide-95\", \"DEPART\": 80, \"DUREE\": 9, \"PRIX\": 8 }, { \"VOL\": \"misty-balloon-82\", \"DEPART\": 83, \"DUREE\": 9, \"PRIX\": 7 }, { \"VOL\": \"excited-comedienne-10\", \"DEPART\": 82, \"DUREE\": 17, \"PRIX\": 7 }, { \"VOL\": \"filthy-forehand-60\", \"DEPART\": 85, \"DUREE\": 10, \"PRIX\": 25 }, { \"VOL\": \"mute-workbook-72\", \"DEPART\": 88, \"DUREE\": 5, \"PRIX\": 5 }, { \"VOL\": \"disgusted-nitwit-88\", \"DEPART\": 87, \"DUREE\": 9, \"PRIX\": 8 }, { \"VOL\": \"prickly-voyage-32\", \"DEPART\": 89, \"DUREE\": 1, \"PRIX\": 8 }, { \"VOL\": \"relieved-riverside-16\", \"DEPART\": 89, \"DUREE\": 7, \"PRIX\": 1 }, { \"VOL\": \"narrow-stethoscope-37\", \"DEPART\": 92, \"DUREE\": 9, \"PRIX\": 20 }, { \"VOL\": \"swift-cowhide-91\", \"DEPART\": 90, \"DUREE\": 4, \"PRIX\": 13 }, { \"VOL\": \"testy-refrigeration-8\", \"DEPART\": 94, \"DUREE\": 7, \"PRIX\": 6 }, { \"VOL\": \"outrageous-crumb-29\", \"DEPART\": 92, \"DUREE\": 4, \"PRIX\": 15 }, { \"VOL\": \"prickly-void-14\", \"DEPART\": 94, \"DUREE\": 19, \"PRIX\": 6 }, { \"VOL\": \"huge-stranger-36\", \"DEPART\": 96, \"DUREE\": 5, \"PRIX\": 30 }, { \"VOL\": \"clever-radar-36\", \"DEPART\": 97, \"DUREE\": 5, \"PRIX\": 7 }, { \"VOL\": \"successful-marshmallow-69\", \"DEPART\": 99, \"DUREE\": 4, \"PRIX\": 2 }, { \"VOL\": \"grieving-wildlife-47\", \"DEPART\": 98, \"DUREE\": 6, \"PRIX\": 8 }, { \"VOL\": \"aggressive-beam-47\", \"DEPART\": 98, \"DUREE\": 7, \"PRIX\": 2 }, { \"VOL\": \"teeny-tiny-flashlight-57\", \"DEPART\": 104, \"DUREE\": 5, \"PRIX\": 8 }, { \"VOL\": \"defiant-crossbow-15\", \"DEPART\": 102, \"DUREE\": 6, \"PRIX\": 18 }, { \"VOL\": \"shrill-barrier-23\", \"DEPART\": 101, \"DUREE\": 6, \"PRIX\": 7 }, { \"VOL\": \"thoughtless-elbow-60\", \"DEPART\": 102, \"DUREE\": 2, \"PRIX\": 14 }, { \"VOL\": \"tiny-trailer-71\", \"DEPART\": 103, \"DUREE\": 20, \"PRIX\": 6 }, { \"VOL\": \"motionless-form-38\", \"DEPART\": 105, \"DUREE\": 8, \"PRIX\": 22 }, { \"VOL\": \"puny-varsity-55\", \"DEPART\": 108, \"DUREE\": 7, \"PRIX\": 20 }, { \"VOL\": \"shy-sinker-12\", \"DEPART\": 106, \"DUREE\": 1, \"PRIX\": 4 }, { \"VOL\": \"average-tumor-17\", \"DEPART\": 107, \"DUREE\": 8, \"PRIX\": 6 }, { \"VOL\": \"frightened-condolence-23\", \"DEPART\": 108, \"DUREE\": 12, \"PRIX\": 6 }, { \"VOL\": \"annoyed-leaf-17\", \"DEPART\": 114, \"DUREE\": 4, \"PRIX\": 22 }, { \"VOL\": \"calm-cockatoo-86\", \"DEPART\": 114, \"DUREE\": 10, \"PRIX\": 15 }, { \"VOL\": \"hushed-nerd-7\", \"DEPART\": 110, \"DUREE\": 2, \"PRIX\": 3 }, { \"VOL\": \"miniature-bulb-53\", \"DEPART\": 114, \"DUREE\": 6, \"PRIX\": 11 }, { \"VOL\": \"fair-utensil-18\", \"DEPART\": 114, \"DUREE\": 9, \"PRIX\": 2 }, { \"VOL\": \"graceful-windpipe-41\", \"DEPART\": 118, \"DUREE\": 8, \"PRIX\": 27 }, { \"VOL\": \"mysterious-penguin-87\", \"DEPART\": 116, \"DUREE\": 3, \"PRIX\": 6 }, { \"VOL\": \"tense-stimulation-23\", \"DEPART\": 118, \"DUREE\": 4, \"PRIX\": 6 }, { \"VOL\": \"successful-munchies-74\", \"DEPART\": 119, \"DUREE\": 6, \"PRIX\": 14 }, { \"VOL\": \"handsome-partridge-46\", \"DEPART\": 118, \"DUREE\": 12, \"PRIX\": 3 }, { \"VOL\": \"harsh-rapper-54\", \"DEPART\": 122, \"DUREE\": 4, \"PRIX\": 26 }, { \"VOL\": \"careful-switchblade-39\", \"DEPART\": 124, \"DUREE\": 5, \"PRIX\": 16 }, { \"VOL\": \"awful-cement-54\", \"DEPART\": 124, \"DUREE\": 7, \"PRIX\": 10 }, { \"VOL\": \"old-architect-50\", \"DEPART\": 122, \"DUREE\": 1, \"PRIX\": 14 }, { \"VOL\": \"loud-wardroom-53\", \"DEPART\": 124, \"DUREE\": 9, \"PRIX\": 4 }, { \"VOL\": \"rapid-camel-55\", \"DEPART\": 126, \"DUREE\": 3, \"PRIX\": 10 }, { \"VOL\": \"breakable-nut-79\", \"DEPART\": 129, \"DUREE\": 3, \"PRIX\": 21 }, { \"VOL\": \"aggressive-cutoffs-10\", \"DEPART\": 127, \"DUREE\": 1, \"PRIX\": 4 }, { \"VOL\": \"hungry-stepparent-39\", \"DEPART\": 128, \"DUREE\": 4, \"PRIX\": 12 }, { \"VOL\": \"different-dollhouse-11\", \"DEPART\": 125, \"DUREE\": 13, \"PRIX\": 4 }, { \"VOL\": \"vivacious-typewriter-25\", \"DEPART\": 130, \"DUREE\": 7, \"PRIX\": 20 }, { \"VOL\": \"colossal-gateway-50\", \"DEPART\": 134, \"DUREE\": 7, \"PRIX\": 19 }, { \"VOL\": \"square-explorer-63\", \"DEPART\": 130, \"DUREE\": 5, \"PRIX\": 1 }, { \"VOL\": \"modern-mosquito-68\", \"DEPART\": 132, \"DUREE\": 1, \"PRIX\": 13 }, { \"VOL\": \"noisy-pillowcase-90\", \"DEPART\": 131, \"DUREE\": 10, \"PRIX\": 1 }, { \"VOL\": \"open-bassoon-21\", \"DEPART\": 137, \"DUREE\": 1, \"PRIX\": 17 }, { \"VOL\": \"terrible-switchboard-92\", \"DEPART\": 136, \"DUREE\": 2, \"PRIX\": 16 }, { \"VOL\": \"teeny-vet-10\", \"DEPART\": 136, \"DUREE\": 4, \"PRIX\": 7 }, { \"VOL\": \"condemned-pedometer-12\", \"DEPART\": 138, \"DUREE\": 5, \"PRIX\": 7 }, { \"VOL\": \"scrawny-limb-8\", \"DEPART\": 138, \"DUREE\": 12, \"PRIX\": 1 }, { \"VOL\": \"young-blob-9\", \"DEPART\": 142, \"DUREE\": 3, \"PRIX\": 2 }, { \"VOL\": \"dull-drill-13\", \"DEPART\": 144, \"DUREE\": 2, \"PRIX\": 13 }, { \"VOL\": \"weary-mason-71\", \"DEPART\": 142, \"DUREE\": 9, \"PRIX\": 1 }, { \"VOL\": \"uninterested-quark-29\", \"DEPART\": 142, \"DUREE\": 6, \"PRIX\": 13 }, { \"VOL\": \"clumsy-zenith-93\", \"DEPART\": 141, \"DUREE\": 13, \"PRIX\": 3 }, { \"VOL\": \"impossible-labyrinth-58\", \"DEPART\": 145, \"DUREE\": 10, \"PRIX\": 22 }, { \"VOL\": \"annoyed-tooth-1\", \"DEPART\": 147, \"DUREE\": 5, \"PRIX\": 19 }, { \"VOL\": \"helpful-pi-29\", \"DEPART\": 147, \"DUREE\": 4, \"PRIX\": 6 }, { \"VOL\": \"delightful-ballpark-37\", \"DEPART\": 148, \"DUREE\": 5, \"PRIX\": 14 }, { \"VOL\": \"fantastic-floor-87\", \"DEPART\": 147, \"DUREE\": 19, \"PRIX\": 4 } ]";
        ClientResponse response = service.path("/jajascript/optimize").type("application/x-www-form-urlencoded").post(ClientResponse.class, payload);

        String output = response.getEntity(String.class);
        System.out.println(output);
        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(output).isEqualTo("{\"gain\":513,\"path\":[\"panicky-sieve-17\",\"brainy-sorrow-85\",\"flat-shrubbery-7\",\"tall-plow-35\",\"disturbed-penniless-45\",\"inquisitive-drop-47\",\"brave-birdseed-29\",\"relieved-heart-12\",\"rich-guerilla-51\",\"sparkling-homemaker-54\",\"comfortable-rag-41\",\"clumsy-tailor-43\",\"cruel-vegetarian-7\",\"purring-void-40\",\"skinny-pink-63\",\"fantastic-queue-49\",\"motionless-cinnamon-66\",\"filthy-forehand-60\",\"huge-stranger-36\",\"thoughtless-elbow-60\",\"motionless-form-38\",\"annoyed-leaf-17\",\"tense-stimulation-23\",\"harsh-rapper-54\",\"rapid-camel-55\",\"breakable-nut-79\",\"modern-mosquito-68\",\"open-bassoon-21\",\"condemned-pedometer-12\",\"dull-drill-13\",\"annoyed-tooth-1\"]}");
    }

    @Test
    public void should_optimize_jajascript_payload2() {
        String payload = "[ { \"VOL\": \"aggressive-raspberry-47\", \"DEPART\": 3, \"DUREE\": 6, \"PRIX\": 7 }, { \"VOL\": \"nasty-sampler-41\", \"DEPART\": 0, \"DUREE\": 5, \"PRIX\": 19 }, { \"VOL\": \"ill-rant-69\", \"DEPART\": 4, \"DUREE\": 9, \"PRIX\": 3 }, { \"VOL\": \"bad-chairlift-44\", \"DEPART\": 3, \"DUREE\": 6, \"PRIX\": 6 }, { \"VOL\": \"bright-story-82\", \"DEPART\": 1, \"DUREE\": 16, \"PRIX\": 7 }, { \"VOL\": \"calm-fairy-65\", \"DEPART\": 6, \"DUREE\": 9, \"PRIX\": 18 }, { \"VOL\": \"shy-salesperson-94\", \"DEPART\": 9, \"DUREE\": 9, \"PRIX\": 14 }, { \"VOL\": \"wandering-dew-29\", \"DEPART\": 7, \"DUREE\": 6, \"PRIX\": 1 }, { \"VOL\": \"outrageous-file-60\", \"DEPART\": 5, \"DUREE\": 5, \"PRIX\": 10 }, { \"VOL\": \"uptight-wax-48\", \"DEPART\": 5, \"DUREE\": 8, \"PRIX\": 6 }, { \"VOL\": \"relieved-tiddlywinks-87\", \"DEPART\": 12, \"DUREE\": 10, \"PRIX\": 15 }, { \"VOL\": \"wicked-whale-52\", \"DEPART\": 12, \"DUREE\": 4, \"PRIX\": 4 }, { \"VOL\": \"lucky-bait-97\", \"DEPART\": 11, \"DUREE\": 10, \"PRIX\": 1 }, { \"VOL\": \"hollow-housetop-81\", \"DEPART\": 11, \"DUREE\": 7, \"PRIX\": 7 }, { \"VOL\": \"drab-seabed-76\", \"DEPART\": 13, \"DUREE\": 11, \"PRIX\": 4 } ]";
        ClientResponse response = service.path("/jajascript/optimize").type("application/x-www-form-urlencoded").post(ClientResponse.class, payload);

        String output = response.getEntity(String.class);
        System.out.println(output);
        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(output).isEqualTo("{\"gain\":44,\"path\":[\"nasty-sampler-41\",\"outrageous-file-60\",\"relieved-tiddlywinks-87\"]}");
    }

    @Test
    public void should_optimize_jajascript_payload3() {
        String payload = "[ { \"VOL\": \"colossal-cockpit-67\", \"DEPART\": 0, \"DUREE\": 6, \"PRIX\": 17 }, { \"VOL\": \"poised-physics-10\", \"DEPART\": 3, \"DUREE\": 1, \"PRIX\": 22 }, { \"VOL\": \"amused-violoncello-8\", \"DEPART\": 4, \"DUREE\": 10, \"PRIX\": 3 }, { \"VOL\": \"homeless-sterilization-46\", \"DEPART\": 1, \"DUREE\": 8, \"PRIX\": 6 }, { \"VOL\": \"colorful-spectacle-76\", \"DEPART\": 2, \"DUREE\": 18, \"PRIX\": 3 }, { \"VOL\": \"faithful-appetite-12\", \"DEPART\": 6, \"DUREE\": 7, \"PRIX\": 11 }, { \"VOL\": \"helpful-globe-95\", \"DEPART\": 5, \"DUREE\": 10, \"PRIX\": 8 }, { \"VOL\": \"hungry-chihuahua-18\", \"DEPART\": 8, \"DUREE\": 4, \"PRIX\": 9 }, { \"VOL\": \"healthy-rhinoceros-79\", \"DEPART\": 5, \"DUREE\": 10, \"PRIX\": 14 }, { \"VOL\": \"calm-percolator-15\", \"DEPART\": 5, \"DUREE\": 2, \"PRIX\": 3 }, { \"VOL\": \"scary-formula-54\", \"DEPART\": 11, \"DUREE\": 6, \"PRIX\": 22 }, { \"VOL\": \"impossible-sobriety-77\", \"DEPART\": 11, \"DUREE\": 5, \"PRIX\": 17 }, { \"VOL\": \"ugly-balance-51\", \"DEPART\": 13, \"DUREE\": 2, \"PRIX\": 9 }, { \"VOL\": \"famous-insecticide-92\", \"DEPART\": 14, \"DUREE\": 1, \"PRIX\": 10 }, { \"VOL\": \"helpless-spyglass-32\", \"DEPART\": 12, \"DUREE\": 7, \"PRIX\": 1 }, { \"VOL\": \"lively-infantryman-57\", \"DEPART\": 15, \"DUREE\": 6, \"PRIX\": 2 }, { \"VOL\": \"ugliest-stairway-32\", \"DEPART\": 15, \"DUREE\": 3, \"PRIX\": 5 }, { \"VOL\": \"tired-azimuth-96\", \"DEPART\": 16, \"DUREE\": 4, \"PRIX\": 9 }, { \"VOL\": \"bad-jade-86\", \"DEPART\": 17, \"DUREE\": 10, \"PRIX\": 13 }, { \"VOL\": \"helpful-quadruped-37\", \"DEPART\": 17, \"DUREE\": 5, \"PRIX\": 4 }, { \"VOL\": \"brief-nunnery-85\", \"DEPART\": 20, \"DUREE\": 6, \"PRIX\": 14 }, { \"VOL\": \"miniature-stalker-58\", \"DEPART\": 22, \"DUREE\": 9, \"PRIX\": 16 }, { \"VOL\": \"angry-family-9\", \"DEPART\": 21, \"DUREE\": 9, \"PRIX\": 1 }, { \"VOL\": \"aggressive-virtue-63\", \"DEPART\": 22, \"DUREE\": 1, \"PRIX\": 7 }, { \"VOL\": \"odd-cod-27\", \"DEPART\": 22, \"DUREE\": 9, \"PRIX\": 5 }, { \"VOL\": \"long-sympathizer-4\", \"DEPART\": 29, \"DUREE\": 9, \"PRIX\": 21 }, { \"VOL\": \"nice-letterhead-73\", \"DEPART\": 25, \"DUREE\": 2, \"PRIX\": 18 }, { \"VOL\": \"annoyed-strikeout-7\", \"DEPART\": 27, \"DUREE\": 3, \"PRIX\": 4 }, { \"VOL\": \"real-bassinet-46\", \"DEPART\": 27, \"DUREE\": 3, \"PRIX\": 7 }, { \"VOL\": \"distinct-cougar-11\", \"DEPART\": 27, \"DUREE\": 4, \"PRIX\": 4 }, { \"VOL\": \"old-whirlwind-49\", \"DEPART\": 34, \"DUREE\": 9, \"PRIX\": 26 }, { \"VOL\": \"sparkling-penny-9\", \"DEPART\": 33, \"DUREE\": 5, \"PRIX\": 21 }, { \"VOL\": \"successful-cheeseburger-84\", \"DEPART\": 33, \"DUREE\": 10, \"PRIX\": 10 }, { \"VOL\": \"scrawny-vest-68\", \"DEPART\": 30, \"DUREE\": 8, \"PRIX\": 6 }, { \"VOL\": \"handsome-goosestep-76\", \"DEPART\": 30, \"DUREE\": 5, \"PRIX\": 1 }, { \"VOL\": \"delightful-track-79\", \"DEPART\": 38, \"DUREE\": 6, \"PRIX\": 16 }, { \"VOL\": \"resonant-skirt-33\", \"DEPART\": 36, \"DUREE\": 8, \"PRIX\": 14 }, { \"VOL\": \"sleepy-vegan-95\", \"DEPART\": 37, \"DUREE\": 5, \"PRIX\": 3 }, { \"VOL\": \"defiant-semiconductor-1\", \"DEPART\": 39, \"DUREE\": 7, \"PRIX\": 6 }, { \"VOL\": \"charming-runner-21\", \"DEPART\": 37, \"DUREE\": 8, \"PRIX\": 7 }, { \"VOL\": \"puny-reactor-7\", \"DEPART\": 40, \"DUREE\": 2, \"PRIX\": 4 }, { \"VOL\": \"attractive-murderer-78\", \"DEPART\": 41, \"DUREE\": 8, \"PRIX\": 13 }, { \"VOL\": \"adorable-camouflage-45\", \"DEPART\": 42, \"DUREE\": 2, \"PRIX\": 5 }, { \"VOL\": \"husky-puppy-55\", \"DEPART\": 44, \"DUREE\": 4, \"PRIX\": 8 }, { \"VOL\": \"frantic-strangeness-89\", \"DEPART\": 42, \"DUREE\": 18, \"PRIX\": 4 }, { \"VOL\": \"short-barrel-23\", \"DEPART\": 45, \"DUREE\": 7, \"PRIX\": 15 }, { \"VOL\": \"obnoxious-armchair-34\", \"DEPART\": 46, \"DUREE\": 3, \"PRIX\": 9 }, { \"VOL\": \"graceful-key-63\", \"DEPART\": 49, \"DUREE\": 1, \"PRIX\": 4 }, { \"VOL\": \"innocent-shelter-85\", \"DEPART\": 49, \"DUREE\": 2, \"PRIX\": 14 }, { \"VOL\": \"purring-boss-59\", \"DEPART\": 45, \"DUREE\": 8, \"PRIX\": 7 }, { \"VOL\": \"wide-eyed-rule-15\", \"DEPART\": 50, \"DUREE\": 10, \"PRIX\": 8 }, { \"VOL\": \"plain-jackass-92\", \"DEPART\": 50, \"DUREE\": 5, \"PRIX\": 10 }, { \"VOL\": \"crowded-fossil-99\", \"DEPART\": 53, \"DUREE\": 7, \"PRIX\": 5 }, { \"VOL\": \"smoggy-cameraman-86\", \"DEPART\": 54, \"DUREE\": 9, \"PRIX\": 15 }, { \"VOL\": \"rich-chihuahua-67\", \"DEPART\": 52, \"DUREE\": 8, \"PRIX\": 3 }, { \"VOL\": \"magnificent-gourd-37\", \"DEPART\": 56, \"DUREE\": 1, \"PRIX\": 30 }, { \"VOL\": \"scary-snobbery-26\", \"DEPART\": 56, \"DUREE\": 9, \"PRIX\": 5 }, { \"VOL\": \"quaint-meat-9\", \"DEPART\": 59, \"DUREE\": 10, \"PRIX\": 4 }, { \"VOL\": \"jittery-earthquake-8\", \"DEPART\": 56, \"DUREE\": 8, \"PRIX\": 15 }, { \"VOL\": \"modern-theft-35\", \"DEPART\": 57, \"DUREE\": 16, \"PRIX\": 5 }, { \"VOL\": \"nice-revolution-22\", \"DEPART\": 61, \"DUREE\": 5, \"PRIX\": 17 }, { \"VOL\": \"square-lodge-48\", \"DEPART\": 60, \"DUREE\": 1, \"PRIX\": 19 }, { \"VOL\": \"thankful-temptation-37\", \"DEPART\": 61, \"DUREE\": 6, \"PRIX\": 7 }, { \"VOL\": \"modern-website-50\", \"DEPART\": 62, \"DUREE\": 8, \"PRIX\": 8 }, { \"VOL\": \"lonely-farmhouse-17\", \"DEPART\": 60, \"DUREE\": 15, \"PRIX\": 5 }, { \"VOL\": \"wide-duct-62\", \"DEPART\": 65, \"DUREE\": 1, \"PRIX\": 17 }, { \"VOL\": \"faint-lace-43\", \"DEPART\": 67, \"DUREE\": 7, \"PRIX\": 10 }, { \"VOL\": \"hilarious-army-69\", \"DEPART\": 69, \"DUREE\": 1, \"PRIX\": 3 }, { \"VOL\": \"calm-mouth-44\", \"DEPART\": 65, \"DUREE\": 9, \"PRIX\": 8 }, { \"VOL\": \"odd-cilantro-80\", \"DEPART\": 69, \"DUREE\": 13, \"PRIX\": 1 }, { \"VOL\": \"proud-gateway-68\", \"DEPART\": 73, \"DUREE\": 5, \"PRIX\": 14 }, { \"VOL\": \"naughty-drivel-69\", \"DEPART\": 71, \"DUREE\": 2, \"PRIX\": 6 }, { \"VOL\": \"eager-pilgrim-44\", \"DEPART\": 71, \"DUREE\": 6, \"PRIX\": 10 }, { \"VOL\": \"quick-searchlight-4\", \"DEPART\": 72, \"DUREE\": 7, \"PRIX\": 12 }, { \"VOL\": \"grotesque-board-36\", \"DEPART\": 72, \"DUREE\": 12, \"PRIX\": 7 }, { \"VOL\": \"expensive-hornet-23\", \"DEPART\": 75, \"DUREE\": 5, \"PRIX\": 10 }, { \"VOL\": \"fair-bagpipe-71\", \"DEPART\": 76, \"DUREE\": 6, \"PRIX\": 17 }, { \"VOL\": \"bad-mucous-83\", \"DEPART\": 78, \"DUREE\": 7, \"PRIX\": 4 }, { \"VOL\": \"lucky-salesperson-40\", \"DEPART\": 75, \"DUREE\": 9, \"PRIX\": 8 }, { \"VOL\": \"quaint-ore-43\", \"DEPART\": 75, \"DUREE\": 14, \"PRIX\": 2 }, { \"VOL\": \"energetic-surname-72\", \"DEPART\": 80, \"DUREE\": 2, \"PRIX\": 12 }, { \"VOL\": \"ashamed-universe-83\", \"DEPART\": 81, \"DUREE\": 5, \"PRIX\": 20 }, { \"VOL\": \"dull-cornbread-40\", \"DEPART\": 82, \"DUREE\": 8, \"PRIX\": 9 }, { \"VOL\": \"breakable-gnat-16\", \"DEPART\": 80, \"DUREE\": 4, \"PRIX\": 11 }, { \"VOL\": \"high-family-59\", \"DEPART\": 83, \"DUREE\": 11, \"PRIX\": 5 }, { \"VOL\": \"tall-rake-97\", \"DEPART\": 87, \"DUREE\": 7, \"PRIX\": 14 }, { \"VOL\": \"careful-speckle-70\", \"DEPART\": 89, \"DUREE\": 1, \"PRIX\": 22 }, { \"VOL\": \"light-product-74\", \"DEPART\": 89, \"DUREE\": 3, \"PRIX\": 4 }, { \"VOL\": \"puzzled-mint-80\", \"DEPART\": 85, \"DUREE\": 3, \"PRIX\": 8 }, { \"VOL\": \"broad-rafter-51\", \"DEPART\": 87, \"DUREE\": 7, \"PRIX\": 4 }, { \"VOL\": \"open-plow-26\", \"DEPART\": 94, \"DUREE\": 7, \"PRIX\": 12 }, { \"VOL\": \"short-smile-7\", \"DEPART\": 91, \"DUREE\": 1, \"PRIX\": 8 }, { \"VOL\": \"frantic-watt-65\", \"DEPART\": 91, \"DUREE\": 4, \"PRIX\": 1 }, { \"VOL\": \"modern-matchless-75\", \"DEPART\": 92, \"DUREE\": 3, \"PRIX\": 7 }, { \"VOL\": \"awful-flare-21\", \"DEPART\": 93, \"DUREE\": 2, \"PRIX\": 7 }, { \"VOL\": \"Early-racist-79\", \"DEPART\": 99, \"DUREE\": 1, \"PRIX\": 29 }, { \"VOL\": \"mammoth-spokesman-24\", \"DEPART\": 95, \"DUREE\": 7, \"PRIX\": 22 }, { \"VOL\": \"annoyed-cone-5\", \"DEPART\": 96, \"DUREE\": 1, \"PRIX\": 1 }, { \"VOL\": \"super-motor-53\", \"DEPART\": 95, \"DUREE\": 9, \"PRIX\": 6 }, { \"VOL\": \"encouraging-manicurist-62\", \"DEPART\": 98, \"DUREE\": 20, \"PRIX\": 7 }, { \"VOL\": \"courageous-pantyhose-83\", \"DEPART\": 100, \"DUREE\": 1, \"PRIX\": 8 }, { \"VOL\": \"relieved-sunfish-89\", \"DEPART\": 101, \"DUREE\": 1, \"PRIX\": 21 }, { \"VOL\": \"fancy-gypsy-51\", \"DEPART\": 103, \"DUREE\": 2, \"PRIX\": 5 }, { \"VOL\": \"lively-hyena-24\", \"DEPART\": 103, \"DUREE\": 9, \"PRIX\": 9 }, { \"VOL\": \"glamorous-glue-55\", \"DEPART\": 100, \"DUREE\": 18, \"PRIX\": 7 }, { \"VOL\": \"high-ukulele-12\", \"DEPART\": 109, \"DUREE\": 9, \"PRIX\": 13 }, { \"VOL\": \"energetic-springtime-89\", \"DEPART\": 109, \"DUREE\": 3, \"PRIX\": 5 }, { \"VOL\": \"crowded-skinhead-93\", \"DEPART\": 109, \"DUREE\": 8, \"PRIX\": 2 }, { \"VOL\": \"little-steamroller-72\", \"DEPART\": 106, \"DUREE\": 6, \"PRIX\": 12 }, { \"VOL\": \"dark-deodorant-78\", \"DEPART\": 107, \"DUREE\": 14, \"PRIX\": 4 }, { \"VOL\": \"bright-toothbrush-36\", \"DEPART\": 110, \"DUREE\": 1, \"PRIX\": 13 }, { \"VOL\": \"exuberant-turbine-21\", \"DEPART\": 112, \"DUREE\": 10, \"PRIX\": 10 }, { \"VOL\": \"agreeable-watchman-92\", \"DEPART\": 110, \"DUREE\": 8, \"PRIX\": 1 }, { \"VOL\": \"pleasant-geology-41\", \"DEPART\": 110, \"DUREE\": 5, \"PRIX\": 9 }, { \"VOL\": \"fragile-spider-31\", \"DEPART\": 110, \"DUREE\": 10, \"PRIX\": 4 }, { \"VOL\": \"thoughtless-gunpowder-74\", \"DEPART\": 117, \"DUREE\": 8, \"PRIX\": 4 }, { \"VOL\": \"annoyed-dustpan-80\", \"DEPART\": 117, \"DUREE\": 10, \"PRIX\": 15 }, { \"VOL\": \"poised-navy-1\", \"DEPART\": 115, \"DUREE\": 4, \"PRIX\": 7 }, { \"VOL\": \"expensive-mandrake-43\", \"DEPART\": 115, \"DUREE\": 10, \"PRIX\": 11 }, { \"VOL\": \"lovely-serfdom-58\", \"DEPART\": 116, \"DUREE\": 18, \"PRIX\": 7 }, { \"VOL\": \"nice-studio-14\", \"DEPART\": 123, \"DUREE\": 9, \"PRIX\": 29 }, { \"VOL\": \"hushed-illness-81\", \"DEPART\": 123, \"DUREE\": 5, \"PRIX\": 9 }, { \"VOL\": \"ill-jackrabbit-11\", \"DEPART\": 120, \"DUREE\": 2, \"PRIX\": 7 }, { \"VOL\": \"puzzled-wrench-94\", \"DEPART\": 123, \"DUREE\": 8, \"PRIX\": 7 }, { \"VOL\": \"bewildered-motor-71\", \"DEPART\": 124, \"DUREE\": 16, \"PRIX\": 7 }, { \"VOL\": \"big-popgun-33\", \"DEPART\": 126, \"DUREE\": 3, \"PRIX\": 30 }, { \"VOL\": \"steep-wimp-41\", \"DEPART\": 128, \"DUREE\": 7, \"PRIX\": 9 }, { \"VOL\": \"short-vestibule-50\", \"DEPART\": 129, \"DUREE\": 4, \"PRIX\": 5 }, { \"VOL\": \"high-concrete-97\", \"DEPART\": 125, \"DUREE\": 9, \"PRIX\": 13 }, { \"VOL\": \"victorious-lifesaver-95\", \"DEPART\": 128, \"DUREE\": 6, \"PRIX\": 6 }, { \"VOL\": \"grumpy-waterworks-1\", \"DEPART\": 130, \"DUREE\": 5, \"PRIX\": 30 }, { \"VOL\": \"young-yard-40\", \"DEPART\": 130, \"DUREE\": 4, \"PRIX\": 15 }, { \"VOL\": \"silent-snakebite-30\", \"DEPART\": 131, \"DUREE\": 5, \"PRIX\": 2 }, { \"VOL\": \"joyous-sty-20\", \"DEPART\": 132, \"DUREE\": 10, \"PRIX\": 15 }, { \"VOL\": \"ugliest-squaw-93\", \"DEPART\": 130, \"DUREE\": 8, \"PRIX\": 4 }, { \"VOL\": \"zealous-balcony-14\", \"DEPART\": 139, \"DUREE\": 5, \"PRIX\": 2 }, { \"VOL\": \"low-cylinder-26\", \"DEPART\": 135, \"DUREE\": 3, \"PRIX\": 5 }, { \"VOL\": \"fat-pill-40\", \"DEPART\": 135, \"DUREE\": 7, \"PRIX\": 7 }, { \"VOL\": \"witty-sacrifice-82\", \"DEPART\": 137, \"DUREE\": 2, \"PRIX\": 11 }, { \"VOL\": \"dizzy-victor-10\", \"DEPART\": 138, \"DUREE\": 6, \"PRIX\": 2 }, { \"VOL\": \"naughty-crab-97\", \"DEPART\": 140, \"DUREE\": 4, \"PRIX\": 14 }, { \"VOL\": \"encouraging-crayon-18\", \"DEPART\": 140, \"DUREE\": 10, \"PRIX\": 7 }, { \"VOL\": \"easy-railway-10\", \"DEPART\": 140, \"DUREE\": 2, \"PRIX\": 1 }, { \"VOL\": \"deep-knight-96\", \"DEPART\": 140, \"DUREE\": 3, \"PRIX\": 10 }, { \"VOL\": \"frantic-hanger-66\", \"DEPART\": 143, \"DUREE\": 20, \"PRIX\": 7 }, { \"VOL\": \"poised-leader-28\", \"DEPART\": 148, \"DUREE\": 8, \"PRIX\": 24 }, { \"VOL\": \"arrogant-rainy-45\", \"DEPART\": 145, \"DUREE\": 10, \"PRIX\": 5 }, { \"VOL\": \"grumpy-poem-7\", \"DEPART\": 149, \"DUREE\": 4, \"PRIX\": 6 }, { \"VOL\": \"fast-raid-90\", \"DEPART\": 148, \"DUREE\": 3, \"PRIX\": 8 }, { \"VOL\": \"square-archery-66\", \"DEPART\": 147, \"DUREE\": 10, \"PRIX\": 6 }, { \"VOL\": \"silly-quail-10\", \"DEPART\": 153, \"DUREE\": 1, \"PRIX\": 4 }, { \"VOL\": \"evil-guano-60\", \"DEPART\": 153, \"DUREE\": 9, \"PRIX\": 12 }, { \"VOL\": \"disgusted-bookcase-16\", \"DEPART\": 150, \"DUREE\": 6, \"PRIX\": 5 }, { \"VOL\": \"scary-dart-63\", \"DEPART\": 150, \"DUREE\": 4, \"PRIX\": 11 }, { \"VOL\": \"difficult-tag-38\", \"DEPART\": 152, \"DUREE\": 10, \"PRIX\": 4 }, { \"VOL\": \"foolish-party-39\", \"DEPART\": 159, \"DUREE\": 4, \"PRIX\": 6 }, { \"VOL\": \"cloudy-box-60\", \"DEPART\": 156, \"DUREE\": 4, \"PRIX\": 18 }, { \"VOL\": \"tame-watch-93\", \"DEPART\": 155, \"DUREE\": 10, \"PRIX\": 1 }, { \"VOL\": \"harsh-marrow-70\", \"DEPART\": 157, \"DUREE\": 5, \"PRIX\": 7 }, { \"VOL\": \"massive-muddy-94\", \"DEPART\": 156, \"DUREE\": 7, \"PRIX\": 6 }, { \"VOL\": \"annoyed-mulch-13\", \"DEPART\": 163, \"DUREE\": 3, \"PRIX\": 11 }, { \"VOL\": \"fine-viola-10\", \"DEPART\": 163, \"DUREE\": 1, \"PRIX\": 21 }, { \"VOL\": \"crowded-scoop-43\", \"DEPART\": 163, \"DUREE\": 5, \"PRIX\": 6 }, { \"VOL\": \"clear-typographer-23\", \"DEPART\": 163, \"DUREE\": 9, \"PRIX\": 9 }, { \"VOL\": \"nutty-smartass-47\", \"DEPART\": 160, \"DUREE\": 19, \"PRIX\": 5 }, { \"VOL\": \"angry-streetlight-51\", \"DEPART\": 168, \"DUREE\": 4, \"PRIX\": 28 }, { \"VOL\": \"rich-killer-51\", \"DEPART\": 166, \"DUREE\": 6, \"PRIX\": 22 }, { \"VOL\": \"great-sucker-74\", \"DEPART\": 165, \"DUREE\": 8, \"PRIX\": 9 }, { \"VOL\": \"huge-juice-30\", \"DEPART\": 169, \"DUREE\": 3, \"PRIX\": 11 }, { \"VOL\": \"real-attire-21\", \"DEPART\": 167, \"DUREE\": 9, \"PRIX\": 5 }, { \"VOL\": \"lucky-seat-17\", \"DEPART\": 174, \"DUREE\": 8, \"PRIX\": 23 }, { \"VOL\": \"fancy-disk-96\", \"DEPART\": 174, \"DUREE\": 4, \"PRIX\": 16 }, { \"VOL\": \"enchanting-accountant-43\", \"DEPART\": 172, \"DUREE\": 5, \"PRIX\": 9 }, { \"VOL\": \"ill-nomad-85\", \"DEPART\": 172, \"DUREE\": 6, \"PRIX\": 13 }, { \"VOL\": \"cheerful-stake-97\", \"DEPART\": 173, \"DUREE\": 5, \"PRIX\": 1 }, { \"VOL\": \"panicky-confetti-32\", \"DEPART\": 177, \"DUREE\": 4, \"PRIX\": 15 }, { \"VOL\": \"fair-newlywed-20\", \"DEPART\": 175, \"DUREE\": 4, \"PRIX\": 7 }, { \"VOL\": \"defiant-plane-47\", \"DEPART\": 177, \"DUREE\": 8, \"PRIX\": 2 }, { \"VOL\": \"skinny-smoker-22\", \"DEPART\": 177, \"DUREE\": 8, \"PRIX\": 15 }, { \"VOL\": \"sore-rhinestone-79\", \"DEPART\": 178, \"DUREE\": 9, \"PRIX\": 7 }, { \"VOL\": \"muddy-quitter-43\", \"DEPART\": 181, \"DUREE\": 8, \"PRIX\": 21 }, { \"VOL\": \"obnoxious-rhinestone-16\", \"DEPART\": 181, \"DUREE\": 10, \"PRIX\": 14 }, { \"VOL\": \"doubtful-trachea-16\", \"DEPART\": 182, \"DUREE\": 7, \"PRIX\": 8 }, { \"VOL\": \"bored-smorgasbord-70\", \"DEPART\": 181, \"DUREE\": 5, \"PRIX\": 12 }, { \"VOL\": \"motionless-jawbone-94\", \"DEPART\": 182, \"DUREE\": 16, \"PRIX\": 5 }, { \"VOL\": \"late-shoplifter-21\", \"DEPART\": 188, \"DUREE\": 8, \"PRIX\": 26 }, { \"VOL\": \"vivacious-fowl-62\", \"DEPART\": 186, \"DUREE\": 9, \"PRIX\": 17 }, { \"VOL\": \"homely-boy-29\", \"DEPART\": 189, \"DUREE\": 2, \"PRIX\": 4 }, { \"VOL\": \"excited-aerosol-29\", \"DEPART\": 188, \"DUREE\": 9, \"PRIX\": 15 }, { \"VOL\": \"sparkling-wingspan-57\", \"DEPART\": 187, \"DUREE\": 13, \"PRIX\": 1 }, { \"VOL\": \"fierce-tarp-60\", \"DEPART\": 192, \"DUREE\": 6, \"PRIX\": 27 }, { \"VOL\": \"poised-composer-46\", \"DEPART\": 190, \"DUREE\": 1, \"PRIX\": 9 }, { \"VOL\": \"ugliest-bassist-18\", \"DEPART\": 194, \"DUREE\": 5, \"PRIX\": 5 }, { \"VOL\": \"lovely-chisel-9\", \"DEPART\": 190, \"DUREE\": 8, \"PRIX\": 15 }, { \"VOL\": \"disgusted-gorilla-79\", \"DEPART\": 193, \"DUREE\": 16, \"PRIX\": 2 }, { \"VOL\": \"nutty-grey-59\", \"DEPART\": 195, \"DUREE\": 6, \"PRIX\": 12 }, { \"VOL\": \"fragile-grommet-89\", \"DEPART\": 196, \"DUREE\": 2, \"PRIX\": 22 }, { \"VOL\": \"witty-shinbone-73\", \"DEPART\": 199, \"DUREE\": 3, \"PRIX\": 10 }, { \"VOL\": \"wonderful-flea-73\", \"DEPART\": 197, \"DUREE\": 6, \"PRIX\": 9 }, { \"VOL\": \"anxious-grimace-14\", \"DEPART\": 197, \"DUREE\": 1, \"PRIX\": 3 }, { \"VOL\": \"curious-showdown-7\", \"DEPART\": 202, \"DUREE\": 2, \"PRIX\": 25 }, { \"VOL\": \"huge-apathetic-94\", \"DEPART\": 203, \"DUREE\": 3, \"PRIX\": 6 }, { \"VOL\": \"weary-campground-13\", \"DEPART\": 200, \"DUREE\": 9, \"PRIX\": 4 }, { \"VOL\": \"depressed-stew-54\", \"DEPART\": 204, \"DUREE\": 10, \"PRIX\": 12 }, { \"VOL\": \"gifted-missile-24\", \"DEPART\": 202, \"DUREE\": 14, \"PRIX\": 5 }, { \"VOL\": \"prickly-ballpark-29\", \"DEPART\": 208, \"DUREE\": 6, \"PRIX\": 27 }, { \"VOL\": \"proud-drivel-61\", \"DEPART\": 208, \"DUREE\": 2, \"PRIX\": 13 }, { \"VOL\": \"joyous-condor-75\", \"DEPART\": 205, \"DUREE\": 6, \"PRIX\": 4 }, { \"VOL\": \"cooperative-gofer-93\", \"DEPART\": 207, \"DUREE\": 7, \"PRIX\": 9 }, { \"VOL\": \"vast-muzzle-79\", \"DEPART\": 207, \"DUREE\": 16, \"PRIX\": 1 }, { \"VOL\": \"rapid-moccasin-44\", \"DEPART\": 211, \"DUREE\": 1, \"PRIX\": 21 }, { \"VOL\": \"faithful-organ-21\", \"DEPART\": 210, \"DUREE\": 9, \"PRIX\": 21 }, { \"VOL\": \"fantastic-iceberg-58\", \"DEPART\": 214, \"DUREE\": 2, \"PRIX\": 1 }, { \"VOL\": \"kind-alfalfa-48\", \"DEPART\": 210, \"DUREE\": 8, \"PRIX\": 12 }, { \"VOL\": \"frail-theft-71\", \"DEPART\": 214, \"DUREE\": 20, \"PRIX\": 6 }, { \"VOL\": \"petite-scraper-71\", \"DEPART\": 218, \"DUREE\": 3, \"PRIX\": 2 }, { \"VOL\": \"breakable-fish-79\", \"DEPART\": 217, \"DUREE\": 2, \"PRIX\": 21 }, { \"VOL\": \"calm-salmonella-22\", \"DEPART\": 216, \"DUREE\": 9, \"PRIX\": 6 }, { \"VOL\": \"clean-slider-7\", \"DEPART\": 216, \"DUREE\": 9, \"PRIX\": 9 }, { \"VOL\": \"outrageous-trunk-8\", \"DEPART\": 217, \"DUREE\": 12, \"PRIX\": 3 }, { \"VOL\": \"awful-lodge-27\", \"DEPART\": 223, \"DUREE\": 2, \"PRIX\": 20 }, { \"VOL\": \"embarrassed-octopus-49\", \"DEPART\": 223, \"DUREE\": 1, \"PRIX\": 17 }, { \"VOL\": \"broad-forefinger-54\", \"DEPART\": 220, \"DUREE\": 3, \"PRIX\": 3 }, { \"VOL\": \"enthusiastic-guppy-2\", \"DEPART\": 223, \"DUREE\": 7, \"PRIX\": 7 }, { \"VOL\": \"eager-chipmunk-84\", \"DEPART\": 224, \"DUREE\": 20, \"PRIX\": 6 }, { \"VOL\": \"sparkling-gong-19\", \"DEPART\": 226, \"DUREE\": 10, \"PRIX\": 26 }, { \"VOL\": \"famous-rationalization-97\", \"DEPART\": 226, \"DUREE\": 7, \"PRIX\": 5 }, { \"VOL\": \"thankful-meadowlark-5\", \"DEPART\": 225, \"DUREE\": 4, \"PRIX\": 10 }, { \"VOL\": \"curved-railing-88\", \"DEPART\": 229, \"DUREE\": 5, \"PRIX\": 7 }, { \"VOL\": \"cheerful-brass-4\", \"DEPART\": 229, \"DUREE\": 16, \"PRIX\": 7 }, { \"VOL\": \"homely-sirloin-55\", \"DEPART\": 232, \"DUREE\": 2, \"PRIX\": 29 }, { \"VOL\": \"magnificent-thanks-94\", \"DEPART\": 233, \"DUREE\": 9, \"PRIX\": 4 }, { \"VOL\": \"tough-vice-19\", \"DEPART\": 230, \"DUREE\": 7, \"PRIX\": 3 }, { \"VOL\": \"vivacious-cupcake-56\", \"DEPART\": 233, \"DUREE\": 1, \"PRIX\": 6 }, { \"VOL\": \"anxious-nag-71\", \"DEPART\": 231, \"DUREE\": 19, \"PRIX\": 6 }, { \"VOL\": \"envious-milkman-35\", \"DEPART\": 239, \"DUREE\": 7, \"PRIX\": 30 }, { \"VOL\": \"ancient-fiddle-92\", \"DEPART\": 237, \"DUREE\": 3, \"PRIX\": 4 }, { \"VOL\": \"selfish-fruit-50\", \"DEPART\": 237, \"DUREE\": 5, \"PRIX\": 1 }, { \"VOL\": \"old-fashioned-album-80\", \"DEPART\": 239, \"DUREE\": 6, \"PRIX\": 8 }, { \"VOL\": \"deep-urination-58\", \"DEPART\": 239, \"DUREE\": 3, \"PRIX\": 3 }, { \"VOL\": \"defeated-veggie-45\", \"DEPART\": 240, \"DUREE\": 10, \"PRIX\": 26 }, { \"VOL\": \"screeching-thundercloud-70\", \"DEPART\": 242, \"DUREE\": 5, \"PRIX\": 5 }, { \"VOL\": \"glamorous-rayon-2\", \"DEPART\": 244, \"DUREE\": 6, \"PRIX\": 5 }, { \"VOL\": \"dull-bathroom-87\", \"DEPART\": 244, \"DUREE\": 3, \"PRIX\": 8 }, { \"VOL\": \"grumpy-transmission-22\", \"DEPART\": 240, \"DUREE\": 15, \"PRIX\": 6 }, { \"VOL\": \"different-pinhead-34\", \"DEPART\": 245, \"DUREE\": 10, \"PRIX\": 23 }, { \"VOL\": \"deep-magnate-42\", \"DEPART\": 247, \"DUREE\": 8, \"PRIX\": 20 }, { \"VOL\": \"outstanding-interrogator-36\", \"DEPART\": 249, \"DUREE\": 8, \"PRIX\": 10 }, { \"VOL\": \"motionless-pumpkin-4\", \"DEPART\": 247, \"DUREE\": 2, \"PRIX\": 11 }, { \"VOL\": \"tender-visa-17\", \"DEPART\": 245, \"DUREE\": 5, \"PRIX\": 3 } ]";
        ClientResponse response = service.path("/jajascript/optimize").type("application/x-www-form-urlencoded").post(ClientResponse.class, payload);

        String output = response.getEntity(String.class);
        System.out.println(output);
        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(output).isEqualTo("{\"gain\":846,\"path\":[\"poised-physics-10\",\"calm-percolator-15\",\"hungry-chihuahua-18\",\"famous-insecticide-92\",\"tired-azimuth-96\",\"aggressive-virtue-63\",\"nice-letterhead-73\",\"real-bassinet-46\",\"sparkling-penny-9\",\"delightful-track-79\",\"obnoxious-armchair-34\",\"graceful-key-63\",\"plain-jackass-92\",\"magnificent-gourd-37\",\"square-lodge-48\",\"nice-revolution-22\",\"hilarious-army-69\",\"naughty-drivel-69\",\"proud-gateway-68\",\"energetic-surname-72\",\"puzzled-mint-80\",\"careful-speckle-70\",\"short-smile-7\",\"modern-matchless-75\",\"annoyed-cone-5\",\"Early-racist-79\",\"courageous-pantyhose-83\",\"relieved-sunfish-89\",\"fancy-gypsy-51\",\"bright-toothbrush-36\",\"poised-navy-1\",\"ill-jackrabbit-11\",\"big-popgun-33\",\"grumpy-waterworks-1\",\"witty-sacrifice-82\",\"naughty-crab-97\",\"poised-leader-28\",\"cloudy-box-60\",\"fine-viola-10\",\"angry-streetlight-51\",\"enchanting-accountant-43\",\"panicky-confetti-32\",\"bored-smorgasbord-70\",\"late-shoplifter-21\",\"fragile-grommet-89\",\"witty-shinbone-73\",\"curious-showdown-7\",\"proud-drivel-61\",\"rapid-moccasin-44\",\"fantastic-iceberg-58\",\"breakable-fish-79\",\"broad-forefinger-54\",\"awful-lodge-27\",\"thankful-meadowlark-5\",\"homely-sirloin-55\",\"envious-milkman-35\",\"motionless-pumpkin-4\",\"outstanding-interrogator-36\"]}");
    }


    @Test
    public void should_optimize_jajascript_payload4() {
        String payload =  "[ { \"VOL\": \"noisy-abacus-37\", \"DEPART\": 3, \"DUREE\": 4, \"PRIX\": 9 }, { \"VOL\": \"jolly-beaver-88\", \"DEPART\": 1, \"DUREE\": 8, \"PRIX\": 23 }, { \"VOL\": \"better-mace-24\", \"DEPART\": 3, \"DUREE\": 8, \"PRIX\": 5 }, { \"VOL\": \"wild-rabblerouser-19\", \"DEPART\": 3, \"DUREE\": 10, \"PRIX\": 8 }, { \"VOL\": \"obedient-striker-27\", \"DEPART\": 4, \"DUREE\": 11, \"PRIX\": 5 }, { \"VOL\": \"better-ignoramus-69\", \"DEPART\": 6, \"DUREE\": 2, \"PRIX\": 22 }, { \"VOL\": \"bloody-wax-15\", \"DEPART\": 7, \"DUREE\": 10, \"PRIX\": 8 }, { \"VOL\": \"mushy-bus-51\", \"DEPART\": 6, \"DUREE\": 9, \"PRIX\": 3 }, { \"VOL\": \"skinny-strapless-32\", \"DEPART\": 8, \"DUREE\": 6, \"PRIX\": 8 }, { \"VOL\": \"alert-orate-8\", \"DEPART\": 5, \"DUREE\": 13, \"PRIX\": 4 } ]";
        ClientResponse response = service.path("/jajascript/optimize").type("application/x-www-form-urlencoded").post(ClientResponse.class, payload);

        String output = response.getEntity(String.class);
        System.out.println(output);
        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(output).isEqualTo("{\"gain\":30,\"path\":[\"better-ignoramus-69\",\"skinny-strapless-32\"]}");
    }

  @Test
  public void should_optimize_jajascript_biiig_payload() throws IOException {
      BufferedReader in = new BufferedReader(new FileReader(getClass().getResource( "/bigPayload").getFile()));  // 50 000 Flights

      String payload =  in.readLine();
      ClientResponse response = service.path("/jajascript/optimize").type("application/x-www-form-urlencoded").post(ClientResponse.class, payload);

      String output = response.getEntity(String.class);
      Candidate c = new ObjectMapper().readValue(output, Candidate.class);

      assertThat(response.getStatus()).isEqualTo(201);
      assertThat(c.getGain()).isEqualTo(166953);
  }


}




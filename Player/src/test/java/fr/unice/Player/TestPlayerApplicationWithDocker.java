package fr.unice.Player;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.platform.commons.annotation.Testable;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;

@Ignore
@Testable
public class TestPlayerApplicationWithDocker {

    @ClassRule
    public static Network network = Network.newNetwork();

    @ClassRule
    public static GenericContainer gameServer =
            new GenericContainer("7wonders/server:0.1")
                    .withExposedPorts(8080)
                    .withNetwork(network)
                    ;

    public static GenericContainer player =
            new GenericContainer("7wonders/player:0.1")
                    .withExposedPorts(8092)
                    .withNetwork(network)
                    .withCommand("curl localhost:8080/register","curl localhost:8080/join")
            ;

    public static GenericContainer stats =
            new GenericContainer("7wonders/stats:0.1")
                    .withExposedPorts(8081)
                    .withNetwork(network)
            ;


    @Test
    public void testPlayerConnected(){
        final String logs = player.getLogs();
        Assertions.assertEquals("Registred to the server.",logs);


    }


}

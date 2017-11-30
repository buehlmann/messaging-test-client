package ch.puzzle.messaging.testcase1;

import org.jboss.ejb3.annotation.ResourceAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.*;

@ResourceAdapter("hornetq-ra")
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "/jms/queue/queue-1"),
        @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "1"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Client-acknowledge")
})
public class Testcase1 implements MessageListener {
    private final Logger logger = LoggerFactory.getLogger("Testcase#1");

    public void onMessage(Message message) {
        logger.info("Received Message: " + message.toString());
        logger.info("Throwing RuntimeException - message must be still in the queue.");

        // RE results in a rollback of the jta transaction
        throw new RuntimeException();
    }
}

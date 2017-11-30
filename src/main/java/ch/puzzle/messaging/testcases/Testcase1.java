package ch.puzzle.messaging.testcases;

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
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
})
/**
 * Message must be still in the queue after read by client but with RuntimeException during TX.
 */
public class Testcase1 implements MessageListener {
    private final Logger logger = LoggerFactory.getLogger("Testcase#1");

    public void onMessage(Message message) {
        logger.info("Received Message: " + message.toString());
        logger.info("Throwing RuntimeException - message must be still in the queue.");

        // RE results in a rollback of the jta transaction
        throw new RuntimeException();
    }
}

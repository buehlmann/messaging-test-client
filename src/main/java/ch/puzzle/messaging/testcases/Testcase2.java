package ch.puzzle.messaging.testcases;


import ch.puzzle.messaging.Prettifier;
import org.jboss.ejb3.annotation.ResourceAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.*;

@ResourceAdapter("hornetq-ra")
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "/jms/queue/queue-2"),
        @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "1"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
})
/**
 * Message must be redelivered by broker for the configured times and delay.
 */
public class Testcase2 implements MessageListener {
    private final Logger logger = LoggerFactory.getLogger("Testcase#2");

    @Inject
    Prettifier prettifier;

    public void onMessage(Message message) {
        logger.info("Received {}", prettifier.toString(message));
        prettifier.printHeaders(message);
        logger.info("Throwing RuntimeException");
        throw new RuntimeException("Should rollback the TX bound to this thread");
    }
}

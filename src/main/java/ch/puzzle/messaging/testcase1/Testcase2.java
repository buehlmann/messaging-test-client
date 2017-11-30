package ch.puzzle.messaging.testcase1;


import org.jboss.ejb3.annotation.ResourceAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.*;

@ResourceAdapter("hornetq-ra")
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "/jms/queue/queue-2"),
        @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "1"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
})
/**
 * Message must be still in the queue after read by client but with RuntimeException during TX.
 */
public class Testcase2 implements MessageListener {
    private final Logger logger = LoggerFactory.getLogger("Testcase#2");

    public void onMessage(Message message) {
        try {
            int redeliveryCount = message.getIntProperty("JMSXDeliveryCount");
            logger.info("Received Message: {}, Redelivery Count: {}" + message, redeliveryCount);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        logger.info("Throwing RuntimeException");
        throw new RuntimeException("Should rollback the TX bound to this thread");
    }
}

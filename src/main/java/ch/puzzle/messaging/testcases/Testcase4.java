package ch.puzzle.messaging.testcases;


import ch.puzzle.messaging.Prettifier;
import ch.puzzle.messaging.Record;
import ch.puzzle.messaging.RecordService;
import org.jboss.ejb3.annotation.ResourceAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.*;

@ResourceAdapter("hornetq-ra")
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "/jms/queue/queue-4"),
        @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "1"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
})
/**
 * Reading message from queue and writing to database with XA should lead in exception case to a consistent state.
 */
public class Testcase4 implements MessageListener {
    private final Logger logger = LoggerFactory.getLogger("Testcase#3");

    @Inject
    RecordService recordService;
    @Inject
    Prettifier prettifier;

    public void onMessage(Message message) {
        try {
            logger.info("Received {}", prettifier.toString(message));
            if(!message.getJMSRedelivered()) {
                Record record = recordService.persistRecord("Testcase#4: Record must not be persisted due to XA rollback");
                logger.info("Persisting {}", record);
                logger.info("Throwing RuntimeException for not yet redelivered message");
                throw new RuntimeException("Should rollback the TX bound to this thread");
            } else {
                Record record = recordService.persistRecord("Testcase#4: Record must be persisted");
                logger.info("Persisting {}", record);
                logger.info("Message is in redelivery-mode. Don't throw Exception anymore");
            }
        } catch (JMSException e) {
        }
    }
}

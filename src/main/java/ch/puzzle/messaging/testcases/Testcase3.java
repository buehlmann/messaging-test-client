package ch.puzzle.messaging.testcases;


import ch.puzzle.messaging.Prettifier;
import ch.puzzle.messaging.Record;
import ch.puzzle.messaging.RecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.*;
import javax.ws.rs.*;

/**
 * Writing message message to a queue and writing to database with XA should lead in exception case to a consistent state.
 */
@Stateless // tx demarcation
@Path("/testcase3")
public class Testcase3 {
    private final Logger logger = LoggerFactory.getLogger("Testcase#3");

    @Inject
    RecordService recordService;
    @Inject
    Prettifier prettifier;
    @Resource(lookup = "java:/JmsXA")
    ConnectionFactory connectionFactory;
    @Resource(lookup = "java:/jms/queue/queue-3")
    Queue replyQueue;

    @GET
    @Produces("text/plain")
    public String sendMessageToQueue3AndWriteToDatabase(@QueryParam("rollback") String rb) {
        boolean rollback = "true".equalsIgnoreCase(rb);

        try {
            String result = process(rollback);
            return result;
        } finally {
            if (rollback) {
                throw new RuntimeException("Rollback");
            }
        }
    }

    private String process(boolean rollback) {
        StringBuffer result = new StringBuffer();
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            connection.start();

            MessageProducer producer = session.createProducer(replyQueue);
            String messagePayload = rollback ? "Testcase#3: Message sent with rollback - must not be in queue" : "Testcase#3: Message sent without rollback - must be in queue";
            TextMessage message = session.createTextMessage(messagePayload);
            producer.send(message);
            logger.info("Sent {} ", message);
            result.append(messagePayload).append("\n");

            String recordPayload = rollback ? "Testcase#3: Record written with rollback - must not be in db!" : "Testcase#3: Record written without rollback - must be persisted in db";
            Record record = recordService.persistRecord(recordPayload);
            logger.info("Persisting {}", record);
            result.append(recordPayload);

        } catch (Exception e) {
            logger.error("Exception occurred during sending of message", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                }
            }
        }
        return result.toString();
    }
}

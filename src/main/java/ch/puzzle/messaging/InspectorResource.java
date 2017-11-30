package ch.puzzle.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static javax.jms.Session.AUTO_ACKNOWLEDGE;

@Path("/inspect")
public class InspectorResource {
    private final Logger logger = LoggerFactory.getLogger(InspectorResource.class);

    @Inject
    Prettifier prettifier;
    @Resource(lookup = "java:/JmsXA")
    ConnectionFactory connectionFactory;

    @GET
    @Produces("text/plain")
    public List<String> lookup(@QueryParam("queue") String queueName) {
        List<String> result = new ArrayList<>();
        if(queueName == null || "".equals(queueName)) return result;

        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, AUTO_ACKNOWLEDGE);

            Queue queue = session.createQueue(queueName);
            QueueBrowser browser = session.createBrowser(queue);
            Enumeration e = browser.getEnumeration();
            while (e.hasMoreElements()) {
                Object o = e.nextElement();
                result.add(prettifier.toString((Message) o));
            }
            logger.info("Found {} elements in Queue {}", result.size(), queueName);
        } catch (JMSException e) {
            logger.error("Error while looking up queue: {}", e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (JMSException e) { e.printStackTrace();}
        }
        return result;
    }
}

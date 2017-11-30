package ch.puzzle.messaging.testcase1;


import ch.puzzle.messaging.RecordService;
import org.jboss.ejb3.annotation.ResourceAdapter;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.*;

@ResourceAdapter("hornetq-ra")
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "/jms/queue/queue-3"),
        @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "1"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
})
public class Testcase3 implements MessageListener {
    @Resource(lookup = "java:/JmsXA")
    ConnectionFactory connectionFactory;

    //@Resource(lookup = "java:/jms/queue/reply-queue-2")
    Queue replyQueue;

    @Inject
    RecordService recordService;

    public void onMessage(Message message) {
        System.out.println("Received Message: " + message.toString());
        recordService.writeRecord();

        sendMessage();

    }

    private void sendMessage() {
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            connection.start();

            MessageProducer producer = session.createProducer(replyQueue);
            TextMessage textMessage = session.createTextMessage("Reply from Testcase1 MDB!");
            producer.send(textMessage);
            System.out.println("Sent Message: " + textMessage);

        } catch (Exception e) {
            System.out.println("Exception occurred during sending of message" + e.getMessage());
            e.printStackTrace();
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) { }
            }
        }
    }
}

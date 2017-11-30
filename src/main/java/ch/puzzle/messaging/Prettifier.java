package ch.puzzle.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.Collections;
import java.util.Enumeration;

public class Prettifier {
    private final Logger logger = LoggerFactory.getLogger(Prettifier.class);
    public void printHeaders(Message message) {
        try {
            for(String key : Collections.list((Enumeration<String>) message.getPropertyNames())) {
                Object value = message.getObjectProperty(key);
                logger.info("Property read: {}={}", key, value);
            }
        } catch (JMSException e) {
            logger.warn("Error while reading property from message: {}", e.getMessage());
        }
    }
    public String toString(Message message) {
        try {
            int deliveryCount = message.getIntProperty("JMSXDeliveryCount");
            return new FormattingTuple("Message [MessageID={}, CorrelationID={}, Redelivered={}, DeliveryCount={}, Priority={}]",
                    new Object[]{ message.getJMSMessageID(), message.getJMSCorrelationID(), message.getJMSRedelivered(), deliveryCount, message.getJMSPriority()}, null).getMessage();
        } catch (JMSException e) {
            logger.warn("Error while reading properties from message: {}", e.getMessage());
        }
        return message.toString();
    }
}

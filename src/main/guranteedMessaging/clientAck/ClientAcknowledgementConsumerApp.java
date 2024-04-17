package main.guranteedMessaging.clientAck;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ClientAcknowledgementConsumerApp {
    public static void main(String[] args) throws NamingException {
        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/requestQueue");
        Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");

        try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
            JMSContext jmsContext = cf.createContext(JMSContext.CLIENT_ACKNOWLEDGE)) {
            // JMSContext.CLIENT_ACKNOWLEDGE tells the JMS server that consumer will give the
            // acknowledgment separately and JMS server doesn't have to blocked for the acknowledgment
            // message will get removed from the queue when the consumer acknowledgment received
            JMSConsumer consumer = jmsContext.createConsumer(queue);
            TextMessage message = (TextMessage) consumer.receive();
            message.acknowledge();
            // this acknowledgment is important if not provided then message remains in JMS Server
            System.out.println(message.getText());
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}

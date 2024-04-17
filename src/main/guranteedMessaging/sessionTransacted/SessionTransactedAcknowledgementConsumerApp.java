package main.guranteedMessaging.sessionTransacted;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SessionTransactedAcknowledgementConsumerApp {
    public static void main(String[] args) throws NamingException {
        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/requestQueue");

        try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
            JMSContext jmsContext = cf.createContext(JMSContext.SESSION_TRANSACTED)) {
            // we can provide the Session Transacted ack in consumer JMS context as well
            // on receiving the queue will not be cleaned until JMS context from the
            // consumer gets commit()
            JMSConsumer consumer = jmsContext.createConsumer(queue);
            TextMessage message = (TextMessage) consumer.receive();

            System.out.println(message.getText());
            jmsContext.commit(); // consumer will take the message from the queue.
            // message getting removed once received and committed.
            // check Artemis UI console Browse Queue section

        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}

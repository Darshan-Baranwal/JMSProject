package main.messageStructure;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;

public class MessageExpirationDemo {
    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/myQueue");
        Queue ExpiryQueue = (Queue) initialContext.lookup("queue/expiryQueue");

        // using try with resources all classes implementing AutoCloseable will get closed by JVM
        try (
                ActiveMQConnectionFactory amqCf = new ActiveMQConnectionFactory();
                JMSContext jmsContext = amqCf.createContext()
        ) {
            JMSProducer producer = jmsContext.createProducer();
            // setTimeToLive(milliseconds)-> tells till what time the producer's message
            // stays in the queue or goes to ExpiryQueue
            producer.setTimeToLive(200);
            producer.send(queue, "Arise, Awake and Stop not till the goal is reached");

            Thread.sleep(5000);

            JMSConsumer consumer = jmsContext.createConsumer(queue);
            TextMessage receivedMessage = (TextMessage) consumer.receive(5000);
            System.out.println(receivedMessage);
            // Data is not coming correctly close session of the queue from Artemis broker UI
            System.out.println(jmsContext.createConsumer(ExpiryQueue).receiveBody(String.class));
        }
    }
}

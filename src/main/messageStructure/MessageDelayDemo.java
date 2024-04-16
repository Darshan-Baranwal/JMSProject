package main.messageStructure;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;

public class MessageDelayDemo {
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
            // setDeliveryDelay(milliseconds)-> Delayed msg delivery by broker to consumer
            producer.setDeliveryDelay(2000);
            producer.send(queue, "Arise, Awake and Stop not till the goal is reached");

            JMSConsumer consumer = jmsContext.createConsumer(queue);
            TextMessage receivedMessage = (TextMessage) consumer.receive(1000);
            System.out.println(receivedMessage); // null if receive timeout less than delayed time
        }
    }
}

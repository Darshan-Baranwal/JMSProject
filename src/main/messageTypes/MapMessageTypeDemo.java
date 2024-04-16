package main.messageTypes;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.MapMessage;
import javax.jms.Queue;
import javax.jms.StreamMessage;
import javax.naming.InitialContext;

public class MapMessageTypeDemo {
    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/myQueue");
        // using try with resources all classes implementing AutoCloseable will get closed by JVM
        try (
                ActiveMQConnectionFactory amqCf = new ActiveMQConnectionFactory();
                JMSContext jmsContext = amqCf.createContext()
        ) {
            /*
            allows to store a stream of Java numbers and String values. When receives the values must be read in exactly
            the same order as they have been written
             */
            MapMessage mapMessage = jmsContext.createMapMessage();
            mapMessage.setBoolean("isCreditAvailable",true);
            jmsContext.createProducer()
                    .send(queue, mapMessage);

            MapMessage received = (MapMessage) jmsContext.createConsumer(queue).receive();
            System.out.println(received.getBoolean("isCreditAvailable"));
        }
    }
}

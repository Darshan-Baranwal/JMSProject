package main.messageStructure;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;

public class MessagePropertiesDemo {
    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/myQueue");

        // using try with resources all classes implementing AutoCloseable will get closed by JVM
        try (
                ActiveMQConnectionFactory amqCf = new ActiveMQConnectionFactory();
                JMSContext jmsContext = amqCf.createContext()
        ) {
            JMSProducer producer = jmsContext.createProducer();
            // setXXXProperty(milliseconds)-> allows to send a property or a meta data in a message
            TextMessage message = jmsContext.createTextMessage("Arise, Awake and Stop not till the goal is reached");
            message.setBooleanProperty("isLoggedIn", true);
            message.setStringProperty("userToken", "124");
            producer.send(queue, message);

            JMSConsumer consumer = jmsContext.createConsumer(queue);
            TextMessage receivedMessage = (TextMessage) consumer.receive(5000);
            System.out.println(receivedMessage);
            System.out.println(receivedMessage.getBooleanProperty("isLoggedIn"));
            System.out.println(receivedMessage.getStringProperty("userToken"));
        }
    }

}

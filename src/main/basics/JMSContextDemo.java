package main.basics;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.activemq.artemis.jndi.ActiveMQInitialContextFactory;

import javax.jms.JMSContext;
import javax.jms.JMSPasswordCredential;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Arrays;

/*
JMS 2.x API usage
No need to maintain connection separately as JMS context take care of everything
 */
public class JMSContextDemo {
    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/myQueue");
        // using try with resources all classes implementing AutoCloseable will get closed by JVM
        try (
                ActiveMQConnectionFactory amqCf = new ActiveMQConnectionFactory();
                JMSContext jmsContext = amqCf.createContext()
                ) {
            jmsContext.createProducer()
                    .send(queue, "Arise, Awake and Stop not till the goal is reached");
            Arrays.stream(
                    jmsContext.createConsumer(queue)
                    .receiveBody(String.class).split(""))
                    .forEach(System.out::print);
        }
    }
}

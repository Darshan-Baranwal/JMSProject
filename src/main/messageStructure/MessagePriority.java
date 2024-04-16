package main.messageStructure;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.InitialContext;
import java.util.Arrays;

public class MessagePriority {

    public static void main(String[] args) throws Exception {
        {
            InitialContext initialContext = new InitialContext();
            Queue queue = (Queue) initialContext.lookup("queue/myQueue");
            // using try with resources all classes implementing AutoCloseable will get closed by JVM
            try (
                    ActiveMQConnectionFactory amqCf = new ActiveMQConnectionFactory();
                    JMSContext jmsContext = amqCf.createContext()
            ) {
                JMSProducer producer = jmsContext.createProducer();
                producer.setPriority(3)
                        .send(queue, "Arise, Awake and Stop not till the goal is reached");
                producer.setPriority(9)
                        .send(queue, "Winner never quit and quitter never wins");
                producer.setPriority(5)
                        .send(queue, "Understand the depth of the concept most of the things are interlinked");

                JMSConsumer consumer = jmsContext.createConsumer(queue);
                for (int i = 0; i < 3; i++) {
                    System.out.println(consumer
                            .receiveBody(String.class));
                }
            }
        }
    }
}

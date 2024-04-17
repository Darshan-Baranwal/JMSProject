package main.jmsPubSub.HR.SharedSubscriber;

import main.jmsPubSub.HR.Employee;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/*
Shared subscribers/consumers (created with same id)
makes the consumer to handle multiple message asynchronously
 */
public class SharedConsumerApp {
    public static void main(String[] args) throws NamingException {
        InitialContext initialContext = new InitialContext();
        Topic empTopic = (Topic) initialContext.lookup("topic/empTopic");

        try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
            JMSContext jmsContext = cf.createContext()) {

            JMSConsumer consumer1 =
                    jmsContext.createSharedConsumer(empTopic, "sharedConsumer");
            JMSConsumer consumer2 =
                    jmsContext.createSharedConsumer(empTopic, "sharedConsumer");

            // load balancing between the consumers
            for (int i = 0; i < 10; i+=2) {
                System.out.println("Consumer 1: "+consumer1.receiveBody(Employee.class).toString());
                System.out.println("Consumer 2: "+consumer2.receiveBody(Employee.class).toString());
            }
        }
    }
}

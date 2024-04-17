package main.jmsPubSub.HR.DurableSubscriber;

import main.jmsPubSub.HR.Employee;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/*
Durable subscriber (created with clientId and Subscription name)
makes the JMS broker
MOM(Messaging middleware) to send messages again which were not delivered
when the subscriber was down.

It takes guarantee that the message will not be lost and sent to subscriber again
 */
public class DurableSecurityApp {
    public static void main(String[] args) throws NamingException {
        InitialContext initialContext = new InitialContext();
        Topic empTopic = (Topic) initialContext.lookup("topic/empTopic");

        try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
            JMSContext jmsContext = cf.createContext()) {

            jmsContext.setClientID("DurableSecurityApp");

            JMSConsumer consumer =
                    jmsContext.createDurableConsumer(empTopic, "subscription1");
            consumer.close(); // only for demo purpose
            Thread.sleep(10000);

            // once consumer up again the previous message will be retrieved in Durable consumers
            consumer = jmsContext.createDurableConsumer(empTopic, "subscription1");
            Employee employee = consumer.receiveBody(Employee.class);
            System.out.print(employee.toString());

            consumer.close();
            jmsContext.unsubscribe("subscription1");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

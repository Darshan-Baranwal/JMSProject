package main.jmsPubSub.HR.SharedSubscriber;

import main.jmsPubSub.HR.Employee;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;
/*
Produces multiple messages on a topic
 */
public class MessagesProducer {
    public static void main(String[] args) throws NamingException {
        InitialContext initialContext = new InitialContext();
        Topic empTopic = (Topic) initialContext.lookup("topic/empTopic");

        try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
            JMSContext jmsContext = cf.createContext()) {
            for (int i = 0; i < 10; i++) {
                Employee employee = new Employee(
                        "123"+i, "Darshan", "Baranwal", "dd@gmail.com",
                        "SSE", "987654321");
                jmsContext.createProducer().send(empTopic, employee);
            }
        }
    }
}

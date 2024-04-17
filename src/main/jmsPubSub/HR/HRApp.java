package main.jmsPubSub.HR;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class HRApp {
    public static void main(String[] args) throws NamingException {
        InitialContext initialContext = new InitialContext();
        Topic empTopic = (Topic) initialContext.lookup("topic/empTopic");

        try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
            JMSContext jmsContext = cf.createContext()) {
            Employee employee = new Employee(
                    "123", "Darshan", "Baranwal", "dd@gmail.com",
                    "SSE", "987654321");
            jmsContext.createProducer().send(empTopic, employee);
        }
    }
}

package main.jmsSecureQueues;

import main.jmsP2P.EligibilityCheck.listeners.EligibilityCheckListener;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SecuredEligibilityCheckerApp {
    public static void main(String[] args) throws NamingException {
        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/securedRequestQueue");
        try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
            JMSContext jmsContext = cf.createContext("eligibilityUser", "eligibilityPass")) {
            JMSConsumer consumer = jmsContext.createConsumer(queue);
            // handle reading the message asynchronously
            consumer.setMessageListener(new SecuredEligibilityCheckListener());

//            jmsContext.createProducer().send(queue, "trying to send in unauthorised queue");

            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

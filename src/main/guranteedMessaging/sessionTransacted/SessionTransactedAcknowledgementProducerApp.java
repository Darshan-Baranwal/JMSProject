package main.guranteedMessaging.sessionTransacted;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SessionTransactedAcknowledgementProducerApp {
    public static void main(String[] args) throws NamingException {
        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/requestQueue");
        // type of Acknowledgment is provided when we create JMS context from connection factory
        try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
            JMSContext jmsContext = cf.createContext(JMSContext.SESSION_TRANSACTED)) {
            JMSProducer producer = jmsContext.createProducer();
            producer.send(queue,"Message 1");
            jmsContext.commit();
            producer.send(queue,"Message 3");
//            jmsContext.commit();
            jmsContext.rollback();


        }
    }
}

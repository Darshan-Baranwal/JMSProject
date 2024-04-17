package main.guranteedMessaging.dups_ok_ack;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class DupsOKAcknowledgementProducerApp {
    public static void main(String[] args) throws NamingException {
        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/requestQueue");
        // type of Acknowledgment is provided when we create JMS context from connection factory
        try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
            JMSContext jmsContext = cf.createContext(JMSContext.DUPS_OK_ACKNOWLEDGE)) {
            JMSProducer producer = jmsContext.createProducer();
            producer.send(queue,"Message 1");
        }
    }
}

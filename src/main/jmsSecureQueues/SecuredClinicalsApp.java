package main.jmsSecureQueues;

import main.jmsP2P.PatientModel;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SecuredClinicalsApp {
    public static void main(String[] args) throws NamingException {
        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/securedRequestQueue");
        Queue replyQueue = (Queue) initialContext.lookup("queue/securedReplyQueue");

        try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
            JMSContext jmsContext = cf.createContext("clinicalUser", "clinicalPass")) {
            JMSProducer producer = jmsContext.createProducer();
            ObjectMessage objectMessage = jmsContext.createObjectMessage();
            PatientModel patientModel = new PatientModel();
            patientModel.setId(123);
            patientModel.setName("Bob");
            patientModel.setInsuranceProvider("Hdfc");
            patientModel.setCoPay(30d);
            patientModel.setAmountTobePaid(500d);

            objectMessage.setObject(patientModel);

            producer.send(queue,objectMessage);

            MapMessage received = (MapMessage) jmsContext.createConsumer(replyQueue).receive();
            System.out.print(received.getInt("PatientId") +"'s eligibility is :"+ received.getBoolean("eligible"));
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}

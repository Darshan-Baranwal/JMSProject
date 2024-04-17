package main.jmsP2P.EligibilityCheck.listeners;

import main.jmsP2P.PatientModel;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EligibilityCheckListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
            JMSContext jmsContext = cf.createContext()) {
            InitialContext initialContext = new InitialContext();
            Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");
            MapMessage mapMessage = jmsContext.createMapMessage();
            PatientModel patient = (PatientModel) objectMessage.getObject();
            if(patient.getInsuranceProvider().equalsIgnoreCase("hdfc") ||
            patient.getInsuranceProvider().equalsIgnoreCase("sbiLife")) {
                System.out.print(patient.toString());
                if(patient.getCoPay()<40d && patient.getAmountTobePaid()<1000) {
                    mapMessage.setBoolean("eligible", true);
                    mapMessage.setInt("PatientId", patient.getId());
                }
            }else {
                mapMessage.setBoolean("eligible", false);
            }
            jmsContext.createProducer().send(replyQueue, mapMessage);

        } catch (JMSException | NamingException e) {
            throw new RuntimeException(e);
        }
    }
}

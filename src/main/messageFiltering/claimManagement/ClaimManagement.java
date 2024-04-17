package main.messageFiltering.claimManagement;

import main.jmsPubSub.HR.Employee;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ClaimManagement {
    public static void main(String[] args) throws NamingException {
        InitialContext initialContext = new InitialContext();
        Queue claimQueue = (Queue) initialContext.lookup("queue/claimQueue");

        try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
            JMSContext jmsContext = cf.createContext()) {
            JMSProducer producer = jmsContext.createProducer();
            String filteringCriteria = "";

            Claim claim = new Claim(1, "John",
                    "Ortho", "HDFC", 1000);
//            filteringCriteria = "hospitalId=1";
//            filteringCriteria = "claimAmount BETWEEN 900 AND 1500";
//            filteringCriteria = "doctorName='John'";
//            filteringCriteria = "doctorName Like 'J%'";
//            filteringCriteria = "doctorType IN ('Gyna', 'Ortho', 'Neuro')";
            filteringCriteria = "doctorType IN ('Gyna', 'Neuro') OR JMSPriority BETWEEN 3 AND 6"; // default JMSPriority is 4
            // for filter criteria we can use SQL arithmetic operation
            // either on objectMessage properties or on JMSHeaders

            JMSConsumer consumer = jmsContext.createConsumer(claimQueue, filteringCriteria);

            ObjectMessage objectMessage = jmsContext.createObjectMessage();

//            objectMessage.setIntProperty("hospitalId", 2);
//            objectMessage.setDoubleProperty("claimAmount", 1000);
//            objectMessage.setStringProperty("doctorName","John");
            objectMessage.setStringProperty("doctorType", "Ortho");

            objectMessage.setObject(claim);

            producer.send(claimQueue, objectMessage);

            Claim claimReceived = consumer.receiveBody(Claim.class);
            System.out.println(claimReceived.toString());
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}

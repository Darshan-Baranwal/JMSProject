package main.messageTypes;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.MapMessage;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;

public class ObjectMessageTypeDemo {
    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/myQueue");
        // using try with resources all classes implementing AutoCloseable will get closed by JVM
        try (
                ActiveMQConnectionFactory amqCf = new ActiveMQConnectionFactory();
                JMSContext jmsContext = amqCf.createContext()
        ) {
            /*
            allows to store a Serialized state of Java object
             */
            ObjectMessage objectMessage = jmsContext.createObjectMessage();
            Patient patient = new Patient();
            patient.setId(1);
            patient.setName("Darshan");
            objectMessage.setObject(patient);
//            jmsContext.createProducer()
//                    .send(queue, objectMessage);
            jmsContext.createProducer().send(queue, patient);

//            ObjectMessage received = (ObjectMessage) jmsContext.createConsumer(queue).receive();
//            Patient patientReceived = (Patient) received.getObject();

            Patient patientReceived = jmsContext.createConsumer(queue).receiveBody(Patient.class);
            System.out.println(patientReceived.getId() + " " +patientReceived.getName());
        }
    }
}

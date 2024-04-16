package main.messageTypes;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.BytesMessage;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.Queue;
import javax.naming.InitialContext;
import java.util.Arrays;

public class ByteMessageTypeDemo {
    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/myQueue");
        // using try with resources all classes implementing AutoCloseable will get closed by JVM
        try (
                ActiveMQConnectionFactory amqCf = new ActiveMQConnectionFactory();
                JMSContext jmsContext = amqCf.createContext()
        ) {
            /*
            ByteMessage is used to send the bytes array mostly in case of
            sending blob in form of byte[]
             */
            BytesMessage bytesMessage = jmsContext.createBytesMessage();
            bytesMessage.writeUTF("John");
            bytesMessage.writeLong(123l);
            jmsContext.createProducer()
                    .send(queue, bytesMessage);

            BytesMessage received = (BytesMessage) jmsContext.createConsumer(queue).receive();
            System.out.println(received.readUTF());
            System.out.println(received.readLong());
        }
    }
}

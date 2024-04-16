package main.basics;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FirstQueue {
    public static void main(String[] args) {
        InitialContext initialContext = null;
        Connection connection = null;

        try {
            initialContext = new InitialContext();
            ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            connection = cf.createConnection();
            Session session = connection.createSession();
            Queue queue = (Queue) initialContext.lookup("queue/myQueue");
            MessageProducer producer = session.createProducer(queue);
            TextMessage textMessage = session.createTextMessage("Hi from receiver");
            producer.send(textMessage);
            System.out.println("Message Sent: "+ textMessage.getText());

            MessageConsumer consumer = session.createConsumer(queue);
            connection.start();
            TextMessage received = (TextMessage) consumer.receive(5000);
            System.out.println("Message Received: "+ textMessage.getText());



        } catch (NamingException e) {
            throw new RuntimeException(e);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        } finally {
            if(initialContext!=null) {
                try {
                    initialContext.close();
                } catch (NamingException e) {
                    throw new RuntimeException(e);
                }
            }
            if(connection!=null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
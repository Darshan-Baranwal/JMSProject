package main.basics;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Enumeration;

public class QueueBrowserDemo {
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
            TextMessage textMessage1 = session.createTextMessage("Hi from receiver");
            TextMessage textMessage2 = session.createTextMessage("Hello from receiver");
            producer.send(textMessage1);
            producer.send(textMessage2);

            // reading queue message without consuming it
            QueueBrowser queueBrowser = session.createBrowser(queue);
            Enumeration msgEnum = queueBrowser.getEnumeration();
            while (msgEnum.hasMoreElements()) {
                TextMessage eachMsg = (TextMessage) msgEnum.nextElement();
                System.out.println("Browsing "+ eachMsg.getText());
            }

            MessageConsumer consumer = session.createConsumer(queue);
            connection.start();
            TextMessage received = (TextMessage) consumer.receive(5000);
            System.out.println("Message Received: "+ received.getText());
            TextMessage received1 = (TextMessage) consumer.receive(5000);
            System.out.println("Message Received: "+ received1.getText());



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
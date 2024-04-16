package main.basics;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FirstTopic {
    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
        Connection connection = cf.createConnection();
        Session session = connection.createSession();
        Topic topic = (Topic) initialContext.lookup("topic/myTopic");
        MessageConsumer consumer = session.createConsumer(topic);
        MessageConsumer consumer1 = session.createConsumer(topic);
        MessageProducer producer = session.createProducer(topic);
        TextMessage textMessage = session.createTextMessage("Sent from Topic");
        producer.send(textMessage);


        connection.start();
        TextMessage received = (TextMessage) consumer.receive();
        System.out.println(received.getText());
        TextMessage received2 = (TextMessage) consumer1.receive();
        System.out.println(received2.getText());
        connection.close();
        initialContext.close();


    }
}

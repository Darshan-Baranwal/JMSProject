package main.jmsGrouping;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageGroupingDemo {
    public static void main(String[] args) throws NamingException {
        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/myQueue");
        Map<String, String> map = new ConcurrentHashMap<>();
        try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
            JMSContext jmsContext = cf.createContext();
            JMSContext jmsContext2 = cf.createContext()) {
            JMSProducer producer = jmsContext.createProducer();

            JMSConsumer consumer2 = jmsContext2.createConsumer(queue);
            consumer2.setMessageListener(new MyListener("Consumer-2", map));
            JMSConsumer consumer1 = jmsContext2.createConsumer(queue);
            consumer1.setMessageListener(new MyListener("Consumer-1", map));
            int count = 10;
            TextMessage[] messages = new TextMessage[count];
            for (int i = 0; i < 10; i++) {
                messages[i] = jmsContext.createTextMessage("Group-0 message"+i);
                messages[i].setStringProperty("JMSXGroupID", "Group-0");
                producer.send(queue,messages[i]);
            }
            Thread.sleep(2000);

        } catch (JMSException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
class MyListener implements MessageListener {
private String name;
private Map<String, String> receivedMessages;

    public MyListener(String name, Map<String, String> receivedMessages) {
        this.name = name;
        this.receivedMessages = receivedMessages;
    }

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            System.out.println(name+ " Consuming "+ textMessage.getText());
            receivedMessages.put(textMessage.getText(), name);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}

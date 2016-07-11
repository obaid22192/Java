/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmsgateway;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author obaid92
 */
public class MessageReceiverGateway {
    Connection connection;
    Session session;
    Destination destination;
    MessageConsumer consumer;
    
    public MessageReceiverGateway(String channelName) {
        StartConnection(channelName);
    }
    
    private void StartConnection(String channelName) {
        try {
            Properties props = new Properties();
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                    "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
            
            Context jndiContext;
            jndiContext = new InitialContext(props);
            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue(channelName);
            this.consumer = session.createConsumer(destination);
            connection.start();
            
        } catch (NamingException | JMSException ex) {
            Logger.getLogger(MessageSenderGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
    
    public void setMessageListner(MessageListener ml) {
        try {
            this.consumer.setMessageListener(ml);
        } catch (JMSException ex) {
            Logger.getLogger(MessageReceiverGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gateways;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author obaid92
 */
public class MessageSenderGateway {
     
    Connection connection;
    Session session;
    Destination destination;
    MessageProducer producer;
    
    public MessageSenderGateway(String channelName) {
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
            this.producer = session.createProducer(destination);
            connection.start();
            
        } catch (NamingException | JMSException ex) {
            Logger.getLogger(MessageSenderGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
    
    public TextMessage createTextMessage(String body) {
        try {
            return this.session.createTextMessage(body);
        } catch (JMSException ex) {
            Logger.getLogger(MessageSenderGateway.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public String send(TextMessage msg) {
        try {
            this.producer.send(msg);
            return msg.getJMSMessageID();
        } catch (JMSException ex) {
            Logger.getLogger(MessageSenderGateway.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}

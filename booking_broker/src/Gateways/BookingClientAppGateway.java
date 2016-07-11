/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gateways;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import booking.model.client.ClientBookingRequest;
import booking.model.client.ClientBookingReply;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author Abdullah
 */

public abstract class BookingClientAppGateway {
    MessageSenderGateway sender;
    MessageReceiverGateway clientReceiver;
    BookingSerializer serializer;
    private Map<String, TextMessage> messageHolder;
    
    
    public BookingClientAppGateway() {
        
        this.clientReceiver = new MessageReceiverGateway("BookingkRequestQueqe");
        this.serializer = new BookingSerializer();
        messageHolder = new HashMap<>();
        
        
        this.clientReceiver.setMessageListner(new MessageListener() {
            @Override
            public void onMessage(Message msg) {
                
//                new Thread(
//                    new Runnable() {
//                        @Override
//                        public void run() {
                            try {
                                TextMessage req = (TextMessage) msg;
                                sender = new MessageSenderGateway(msg.getJMSReplyTo().toString().split("://")[1]);
                                ClientBookingRequest lreq = serializer.requestFromString(req.getText());
                                
                                messageHolder.put(msg.getJMSMessageID(), req);

                                onBookingRequest(lreq, msg.getJMSMessageID());
                                        } catch (JMSException ex) {
                                Logger.getLogger(BookingClientAppGateway.class.getName()).log(Level.SEVERE, null, ex);
                            }            
//                        }
//                    }
//                ).start();
            }
        });
    }

    public Map<String, TextMessage> getMessageHolder() {
        return messageHolder;
    }

    public void setMessageHolder(Map<String, TextMessage> messageHolder) {
        this.messageHolder = messageHolder;
    }
    
    public abstract void onBookingRequest(ClientBookingRequest request, String requestID);
    
    public String replyToClient(ClientBookingReply bookingreply, String corelationID){
        TextMessage reply =  sender.createTextMessage(serializer.replyToString(bookingreply));
        try {
            reply.setJMSCorrelationID(corelationID);
        } catch (JMSException ex) {
            Logger.getLogger(BookingClientAppGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sender.send(reply);
    }
}

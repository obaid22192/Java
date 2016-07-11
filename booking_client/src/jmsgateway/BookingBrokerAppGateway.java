/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmsgateway;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import booking.model.client.ClientBookingRequest;
import booking.model.client.ClientBookingReply;

/**
 *
 * @author obaid92
 */

public abstract class BookingBrokerAppGateway {
    MessageSenderGateway sender;
    MessageReceiverGateway clientReceiver;
    BookingSerializer serializer;
    
    public BookingBrokerAppGateway() {
        this.sender = new MessageSenderGateway("BookingkRequestQueqe");
        this.clientReceiver = new MessageReceiverGateway("BookingReplyQueue");
        this.serializer = new BookingSerializer();
        this.clientReceiver.setMessageListner(new MessageListener() {
            @Override
            public void onMessage(Message msg) {
                try {
                    TextMessage message = (TextMessage) msg;
                    ClientBookingReply reply = serializer.replyFromString(message.getText());
                    
                    onBookingReply(reply, message.getJMSCorrelationID());
                            } catch (JMSException ex) {
                    Logger.getLogger(BookingBrokerAppGateway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    public abstract void onBookingReply(ClientBookingReply reply, String returnId);
    
    public String  submitBookingRequest(ClientBookingRequest bookingrequest){
        TextMessage request =  sender.createTextMessage(serializer.requestToString(bookingrequest));
        try {
            request.setJMSReplyTo(clientReceiver.destination);
        } catch (JMSException ex) {
            Logger.getLogger(BookingBrokerAppGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sender.send(request);
    }
}

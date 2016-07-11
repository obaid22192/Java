/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gateways;

import javax.jms.TextMessage;

import booking.model.agency.AgencyReply;
import booking.model.agency.AgencyRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 *
 * @author obaid92
 */
public abstract class BookingAgenceyAppGateway {
    MessageSenderGateway sender;
    MessageReceiverGateway Receiver;
    BookingSerializer serializer;
    AgencySerializer agencySerializer;
    private static Map<String, TextMessage> holder;
    
    public BookingAgenceyAppGateway() {
//    this.sender = new MessageSenderGateway("bookFastQueue");
    this.Receiver = new MessageReceiverGateway("AgencyReplyQueqe");
        this.serializer = new BookingSerializer();
        agencySerializer = new AgencySerializer();
        holder = new HashMap<>();
        this.Receiver.setMessageListner(new MessageListener() {
            @Override
            public void onMessage(Message msg) {
                try {
                    TextMessage reply = (TextMessage) msg;
                    AgencyReply lreq = agencySerializer.replyFromString(reply.getText());
                    System.out.println(reply.getJMSCorrelationID());
                    onBookingRequest(lreq, reply.getJMSCorrelationID(), reply.getIntProperty("aggregatotionID"));
                            } catch (JMSException ex) {
                    Logger.getLogger(BookingClientAppGateway.class.getName()).log(Level.SEVERE, null, ex);
                }             
//                        }
            }
        });
    }
    public abstract void onBookingRequest(AgencyReply reply, String returnId, int aggregatotionID);
    
    public String sendBookingrequest(AgencyRequest bookingrequest, String corelationId, String queue, int aggId){
        this.sender = new MessageSenderGateway(queue);
        TextMessage request =  sender.createTextMessage(agencySerializer.requestToString(bookingrequest));
        try {
            request.setJMSCorrelationID(corelationId);
            request.setJMSReplyTo(this.Receiver.destination);
            request.setIntProperty("aggregatotionID", aggId);
        } catch (JMSException ex) {
            Logger.getLogger(BookingAgenceyAppGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sender.send(request);
    }
}

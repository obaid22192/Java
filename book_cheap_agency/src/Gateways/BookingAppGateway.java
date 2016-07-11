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
import java.util.HashMap;
import java.util.Map;
import Gateways.*;

import booking.model.agency.AgencyReply;
import booking.model.agency.AgencyRequest;


/**
 *
 * @author Abdullah
 */

public abstract class BookingAppGateway {
    MessageSenderGateway sender;
    MessageReceiverGateway clientReceiver;
    AgencySerializer serializer;
    
    
    public BookingAppGateway(String agencyRequestQueue) {
        
        this.clientReceiver = new MessageReceiverGateway(agencyRequestQueue);
        this.serializer = new AgencySerializer();
        
        
        this.clientReceiver.setMessageListner(new MessageListener() {
            @Override
            public void onMessage(Message msg) {
                try {
                    TextMessage req = (TextMessage) msg;
                    sender = new MessageSenderGateway(msg.getJMSReplyTo().toString().split("://")[1]);
                    AgencyRequest lreq = serializer.requestFromString(req.getText());

                    onBookingRequest(lreq, msg.getJMSMessageID(), msg.getIntProperty("aggregatotionID"));
                            } catch (JMSException ex) {
                    Logger.getLogger(BookingAppGateway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public abstract void onBookingRequest(AgencyRequest request, String requestID, int aggregatotionID);
    
    public String replyToBroker(AgencyReply bookingreply, String corelationId, int aggregationId){
        TextMessage reply =  sender.createTextMessage(serializer.replyToString(bookingreply));
        try {
            reply.setJMSCorrelationID(corelationId);
            reply.setIntProperty("aggregatotionID", aggregationId);
        } catch (JMSException ex) {
            Logger.getLogger(BookingAppGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sender.send(reply);
    }
}

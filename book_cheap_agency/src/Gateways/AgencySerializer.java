/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gateways;

import com.google.gson.Gson;
import booking.model.agency.AgencyReply;
import booking.model.agency.AgencyRequest;
/**
 *
 * @author obaid92
 */
public class AgencySerializer {
    Gson gson;
    
    public AgencySerializer() {
        gson = new Gson();
    }
    
    public String requestToString(AgencyRequest request){
        return this.gson.toJson(request);
    }
    
    public AgencyRequest requestFromString(String request){
        return (AgencyRequest) this.gson.fromJson(request, AgencyRequest.class);
    }
    
    public String replyToString(AgencyReply reply){
        return this.gson.toJson(reply);
    }
    
    public AgencyReply replyFromString(String reply){
        return (AgencyReply) this.gson.fromJson(reply, AgencyReply.class);
    } 

    String requestToString(AgencyReply reply) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

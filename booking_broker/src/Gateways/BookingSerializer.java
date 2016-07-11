/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gateways;

import com.google.gson.Gson;
import booking.model.client.ClientBookingRequest;
import booking.model.client.ClientBookingReply;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 *
 * @author obaid92
 */
public class BookingSerializer {
    Gson gson;
    
    public BookingSerializer() {
        gson = new Gson();
    }
    
    public String requestToString(ClientBookingRequest request){
        return this.gson.toJson(request);
    }
    
    public ClientBookingRequest requestFromString(String request){
        return (ClientBookingRequest) this.gson.fromJson(request, ClientBookingRequest.class);
    }
    
    public String replyToString(ClientBookingReply reply){
        return this.gson.toJson(reply);
    }
    
    public ClientBookingReply replyFromString(String reply){
        return (ClientBookingReply) this.gson.fromJson(reply, ClientBookingReply.class);
    }
    
    public JsonObject stringToJson(String json) {
        JsonElement jelem = gson.fromJson(json, JsonElement.class);
        return jelem.getAsJsonObject();
    }
}

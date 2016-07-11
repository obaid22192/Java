/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookin_broker;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import booking.model.client.ClientBookingRequest;
import booking.model.client.ClientBookingReply;
import booking.model.agency.AgencyReply;
import booking.model.agency.AgencyRequest;

import Gateways.*;
import Proxies.*;
import com.google.gson.JsonObject;
import com.sun.deploy.util.StringUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author obaid92
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label label;
    
    @FXML
    private ListView listViewTRansactions;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    
    ObservableList<ClientListLine> view;
    private BookingClientAppGateway bookingClientAppGateway;
    private BookingAgenceyAppGateway bookingAgenceyAppGateway;
    private Map<String, ClientBookingRequest> holder;
    private Map<String, String> idHolder;
    private GoogleMatrixApis matrixApi;
    private BookingSerializer serializer;
    private int aggregatorId = 0;
    private List<AggregatorHolder> requestsHolder; 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        view = listViewTRansactions.getItems();
        holder = new HashMap<>();
        idHolder = new HashMap<>();
        matrixApi = new GoogleMatrixApis();
        serializer = new BookingSerializer();
        requestsHolder = new ArrayList<AggregatorHolder>();
        bookingAgenceyAppGateway = new BookingAgenceyAppGateway() {
            public void onBookingRequest(AgencyReply reply, String returnId, int aggregatotionID) {
                
                for(AggregatorHolder a: requestsHolder) {
                    if(a.getAggregationId() == aggregatotionID){
                        a.setNumberofMessagesReceived(a.getNumberofMessagesReceived() + 1);
                        a.addRequest(reply);
                        
                        double min = 0;
                        if(a.getNumberofMessagesReceived() == a.getNumberofMessagesSent()) {
                            for(AgencyReply rep: a.getRequests()) {
                                if(rep.getTotalPrice() <= min) {
                                    reply = rep;
                                }
                                else {
                                    min = rep.getTotalPrice();
                                }
                            }
                            for (ClientListLine req: view) {
                                if(holder.get(returnId) == req.getRequest()) {
                                    req.setReply(reply);
                                    bookingClientAppGateway.replyToClient(new ClientBookingReply(
                                            reply.getNameAgency(), reply.getTotalPrice()), idHolder.get(returnId));
                                    listViewTRansactions.getSelectionModel().select(req);
                                    listViewTRansactions.refresh();
                                }
                            }
                        }
                    }
                }
            }
        };
        
        bookingClientAppGateway = new BookingClientAppGateway() {
            public void onBookingRequest(ClientBookingRequest request, String requestID) {
                        
                view.add(new ClientListLine(request, null));
                
                try {
                // check if transfer is defined then call goolge destination api to get distance\
                    AggregatorHolder tmp = new AggregatorHolder();
                    String apiResponse = "";
                    int aggId = generateaggregatorId();
                    if(request.getTransferToAddress() != null) {
                        apiResponse = matrixApi.run(
                            URLEncoder.encode(request.getDestinationAirport(), "UTF-8"),
                            URLEncoder.encode(request.getTransferToAddress().toString(), "UTF-8")
                        );          
                    }
                    double distance = (!"".equals(apiResponse)) ? stringJsonToDouble(apiResponse): 0;
                    int count = 0;
                    if(distance >= 10 && distance <= 50) {
                        String id = sendRequetToAgency(new AgencyRequest(request.getDestinationAirport(), request.getOriginAirport(), distance), requestID, "bookCheapQueue", aggId);
                        holder.put(id, request);
                        idHolder.put(id, requestID);
                        count++;
                    }
                    if(distance <=40) {
                        String id = sendRequetToAgency(new AgencyRequest(request.getDestinationAirport(), request.getOriginAirport(), distance), requestID, "bookGoodServiceQueue", aggId);
                        holder.put(id, request);
                        idHolder.put(id, requestID);
                        count++;
                    }
                    if(distance == 0) {
                        String id = sendRequetToAgency(new AgencyRequest(request.getDestinationAirport(), request.getOriginAirport(), distance), requestID, "bookFastQueue", aggId);
                        holder.put(id, request);
                        idHolder.put(id, requestID);
                        count++;
                    }
                    tmp.setAggregationId(aggId);
                    tmp.setNumberofMessagesSent(count);
                    requestsHolder.add(tmp);
                } catch (IOException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    }            
            }         
        };  
    }
    
    public String sendRequetToAgency(AgencyRequest request, String requestID, String queue, int aggId) {
        String id = bookingAgenceyAppGateway.sendBookingrequest(request, requestID, queue, aggId);
        return id;
    }
    
    public double stringJsonToDouble(String str) {
        JsonObject obj =  serializer.stringToJson(str);
        String raw = obj.getAsJsonArray("rows").get(0).getAsJsonObject()
                .get("elements").getAsJsonArray().get(0).getAsJsonObject()
                .get("distance").getAsJsonObject().get("text").toString();
        return Double.parseDouble(raw.split("\\s+")[0].replace("\"", ""));
    }
    
    public int generateaggregatorId() {
        return aggregatorId++;
    }
}

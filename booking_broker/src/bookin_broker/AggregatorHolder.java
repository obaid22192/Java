/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookin_broker;

import booking.model.agency.AgencyReply;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author obaid92
 */
public class AggregatorHolder {
    private List<AgencyReply> requests;
    private int aggregationId;
    private int numberofMessagesSent = 0;
    private int numberofMessagesReceived = 0;
    
    public AggregatorHolder() {
        requests = new ArrayList<AgencyReply>();
    }

    public List<AgencyReply> getRequests() {
        return requests;
    }

    public void addRequest(AgencyReply requests) {
        this.requests.add(requests);
    }

    public int getAggregationId() {
        return aggregationId;
    }

    public void setAggregationId(int aggregationId) {
        this.aggregationId = aggregationId;
    }

    public int getNumberofMessagesSent() {
        return numberofMessagesSent;
    }

    public void setNumberofMessagesSent(int numberofMessagesSent) {
        this.numberofMessagesSent = numberofMessagesSent;
    }

    public int getNumberofMessagesReceived() {
        return numberofMessagesReceived;
    }

    public void setNumberofMessagesReceived(int numberofMessagesReceived) {
        this.numberofMessagesReceived = numberofMessagesReceived;
    }
}

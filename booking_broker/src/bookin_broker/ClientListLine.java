package bookin_broker;

import booking.model.client.ClientBookingRequest;
import booking.model.agency.AgencyReply;

class ClientListLine {

    private ClientBookingRequest request;
    private AgencyReply reply;

    public ClientListLine(ClientBookingRequest request, AgencyReply reply) {
        this.request = request;
        this.reply = reply;
    }

    public ClientBookingRequest getRequest() {
        return request;
    }

    private void setRequest(ClientBookingRequest request) {
        this.request = request;
    }

    public AgencyReply getReply() {
        return reply;
    }

    public void setReply(AgencyReply reply) {
        this.reply = reply;
    }

    @Override
    public String toString() {
        return ((request != null) ? request.toString() : "waiting...") + "  --->  " + ((reply != null) ? reply.toString() : "waiting...");
    }

}

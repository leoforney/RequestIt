package tk.leoforney.requestit.websocket;

public class PayloadMessage {

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public PayloadMessage() {}
    public PayloadMessage(String sessionId) {
        this.sessionId = sessionId;
    }

    public String sessionId;

}

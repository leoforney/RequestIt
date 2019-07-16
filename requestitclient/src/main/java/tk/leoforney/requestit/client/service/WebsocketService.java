package tk.leoforney.requestit.client.service;

import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import tk.leoforney.requestit.client.stomp.SessionHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class WebsocketService {

    private static final Logger logger = Logger.getLogger(WebsocketService.class.getName());
    public SessionHandler sessionHandler;

    public WebsocketService() {
        logger.log(Level.INFO, "Websocket Service started");
        WebSocketClient client = new StandardWebSocketClient();

        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new StringMessageConverter());

        sessionHandler = new SessionHandler();
        stompClient.connect("ws://localhost:8080/sessionSocketJs", sessionHandler);

    }

}

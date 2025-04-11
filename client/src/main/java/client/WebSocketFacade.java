package client;

import com.google.gson.Gson;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.net.URI;

public class WebSocketFacade extends Endpoint {
    private final Session session;
    private final WebSocketObserver observer;

    public WebSocketFacade(String url, WebSocketObserver observer) throws Exception {
        url = url.replace("http", "ws");
        URI uri = new URI(url + "/ws");
        this.observer = observer;
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                observer.notify(message);
            }
        });
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }
    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }


}

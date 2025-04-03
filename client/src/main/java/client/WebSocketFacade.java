package client;

import javax.websocket.*;
import java.net.URI;

public class WebSocketFacade extends Endpoint {
    private WebSocketObserver observer;
    private final Session session;

    public WebSocketFacade(String url, WebSocketObserver observer) throws Exception {
        url = url.replace("http", "ws");
        URI uri = new URI(url + "/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);
        this.observer = observer;

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                observer.notify();
            }
        });
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}

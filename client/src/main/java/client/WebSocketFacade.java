package client;

import javax.websocket.*;
import java.net.URI;

public class WebSocketFacade extends Endpoint {

    public Session session;

    public WebSocketFacade(String url) throws Exception {
        url = url.replace("http", "ws");
        URI uri = new URI(url + "/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                System.out.println(message);
            }
        });
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}

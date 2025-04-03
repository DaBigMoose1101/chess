package client;

import websocket.messages.ServerMessage;

public interface WebSocketObserver {
    void notify(ServerMessage message);
}

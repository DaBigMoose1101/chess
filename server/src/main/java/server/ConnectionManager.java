package server;

import spark.Session;

import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionManager {
    private final HashMap<Integer, ArrayList<Session>> connections;

    public ConnectionManager(){
        connections = new HashMap<>();
    }

    public void addConnection(int gameId, Session session){
        ArrayList<Session> sessions = getSessions(gameId);
        sessions.add(session);
        connections.put(gameId, sessions);
    }

    public ArrayList<Session> getSessions(int gameId){
        return connections.get(gameId);
    }

    public void removeSession(int gameId, Session session){
        ArrayList<Session> sessions = getSessions(gameId);
        sessions.remove(session);
        connections.remove(gameId, sessions);
    }
}

package client;

import client.ClientCommunicator;
import records.RegisterRequest;

public class ServerFacade {
    ClientCommunicator communicator;
    String url;
    public ServerFacade(){
        this.url = "example url";
        communicator = new ClientCommunicator();
    }

    public Object register(String username, String password, String email){
        RegisterRequest req = new RegisterRequest(username, password, email);


        return null;
    }
}

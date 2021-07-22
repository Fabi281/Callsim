package de.callsim;

import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

@ClientEndpoint
public class WebsocketClientEndpoint {

    Session userSession = null;

    public WebsocketClientEndpoint(URI endpointURI) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("opening websocket");
        this.userSession = userSession;
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("closing websocket");
        this.userSession = null;
    }

    @OnMessage
    public void onMessage(String message) {
        JsonReader reader = Json.createReader(new StringReader(message));
        JsonObject jsonMessage = reader.readObject();

        try{
            System.out.println(jsonMessage.getString("Statuscode"));
            System.out.println(jsonMessage.getString("Statusword"));
        }catch(Exception e){
            
        }

        try{
            JsonArray tmp = jsonMessage.getJsonArray("User");
            List<String> list = new ArrayList<String>();
            for(int i = 0; i < tmp.size(); i++){
                list.add(tmp.getString(i));
            }

            list.forEach(s -> System.out.println(s));
        }catch(Exception e){

        }
        

    }

    public void sendMessage(JsonObject json) {
        this.userSession.getAsyncRemote().sendObject(json);
    }

}

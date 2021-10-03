package de.callsim;

import java.awt.*;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.swing.*;
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
            client.popupMessage("Server is not active.\n" + e.toString());
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
    public void onMessage(String message) throws URISyntaxException {

        JsonReader reader = Json.createReader(new StringReader(message));
        JsonObject jsonMessage = reader.readObject();
        String action = jsonMessage.getString("Action");
        String value;

        switch (action) {
            case "PosLoginResponse":
                System.out.println(jsonMessage.getString("Value"));
                client.username = jsonMessage.getString("Value");
                client.nwPage = new NextWindow();
                client.showAppPage();
                break;

            case "NegLoginResponse":
                System.out.println(jsonMessage.getString("Value"));
                break;

            case "PosRegisterResponse":
                value = jsonMessage.getString("Value");
                System.out.println(value);
                client.popupMessage(value);
                break;

            case "NegRegisterResponse":
                value = jsonMessage.getString("Value");
                System.out.println(value);
                client.popupMessage(value);
                break;

            case "StatusResponse":
                JsonArray user = jsonMessage.getJsonArray("User");
                List<JsonObject> list = new ArrayList<JsonObject>();
                HashMap<String, String> userData = new HashMap();
                for(int i = 0; i < user.size(); i++){
                    if(!user.getJsonObject(i).containsKey(client.username)) list.add(user.getJsonObject(i));
                }

                list.forEach(object -> {
                    for (String key: object.keySet()){
                        System.out.println(key + ": " + object.get(key));
                        userData.put(key, object.get(key).toString());
                    }
                });
                client.nwPage.getListResponse(userData);
                break;
            case "NotFound":
                System.out.println(jsonMessage.getString("Value"));
                break;

            case "incomingCall":
                System.out.println(jsonMessage.getString("Value"));
                break;

            case "startedCall":
                System.out.println(jsonMessage.getString("Value"));
                break;

            case "RemoteCallEnded":
                System.out.println(jsonMessage.getString("Value"));
                break;

            case "SelfCallEnded":
                System.out.println(jsonMessage.getString("Value"));
                break;

            case "SelfCallAccepted":
                System.out.println(jsonMessage.getString("Value"));
                break;

            case "RemoteCallAccepted":
                System.out.println(jsonMessage.getString("Value"));
                break;

            case "SelfCallDeclined":
                System.out.println(jsonMessage.getString("Value"));
                break;

            case "RemoteCallDeclined":
                System.out.println(jsonMessage.getString("Value"));
                break;

            default:
                System.out.println("Keine gültige Rückmeldung");
        }
    }

    public void sendMessage(JsonObject json) {
        this.userSession.getAsyncRemote().sendObject(json);
    }

}

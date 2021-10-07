package de.callsim;

import java.awt.*;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
        String bbbserver;

        switch (action) {
            case "PosLoginResponse":
                System.out.println(jsonMessage.getString("Value"));
                client.clientUsername = jsonMessage.getString("Value");
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
                List<JsonObject> list = new ArrayList<>();
                HashMap<String, String> userData = new HashMap();
                for(int i = 0; i < user.size(); i++){
                    if(!user.getJsonObject(i).containsKey(client.clientUsername)) list.add(user.getJsonObject(i));
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
                bbbserver = jsonMessage.getString("Value");
                String username = jsonMessage.getString("Username");
                client.bbbserver = bbbserver;
                client.incomingCall("Call Incoming from " + username + "...", username);
                break;

            case "startedCall":
                System.out.println(jsonMessage.getString("Value"));
                System.out.println("Test Start Call");
                bbbserver = jsonMessage.getString("Value");
                client.bbbserver = bbbserver;
                break;

            case "RemoteCallEnded":
                System.out.println(jsonMessage.getString("Value"));
                client.nwPage.setCallDisplay(false);
                if(client.dialog != null){
                    client.dialog.dispose();
                    client.dialog = null;
                }
                break;

            case "SelfCallEnded":
                System.out.println(jsonMessage.getString("Value"));
                client.nwPage.setCallDisplay(false);
                break;

            case "SelfCallAccepted":
                System.out.println("SelfCallAccepted");
                openURL(jsonMessage.getString("Value"));
                break;

            case "RemoteCallAccepted":
                System.out.println("RemoteCallAccepted");
                openURL(jsonMessage.getString("Value"));
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

    public void openURL(String bbburl){
        Desktop desktop = java.awt.Desktop.getDesktop();
        try {
            //specify the protocol along with the URL
            URI oURL = new URI(bbburl);
            desktop.browse(oURL);
        } catch (URISyntaxException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

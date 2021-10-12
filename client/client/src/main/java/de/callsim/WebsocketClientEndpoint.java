package de.callsim;

import java.awt.*;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
        String bbbserver;

        switch (action) {
            case "PosLoginResponse":
                //Debugging
                System.out.println("case: PosLoginResponse");

                client.clientUsername = jsonMessage.getString("Value");
                client.nwPage = new NextWindow();
                client.showAppPage();
                break;

            case "NegLoginResponse":
                //Debugging
                System.out.println("case: NegLoginResponse");

                client.popupMessage(jsonMessage.getString("Value"));
                break;

            case "PosRegisterResponse":
                //Debugging
                System.out.println("case: PosRegisterResponse");

                value = jsonMessage.getString("Value");
                client.popupMessage(value);
                break;

            case "NegRegisterResponse":
                //Debugging
                System.out.println("case: NegRegisterResponse");

                value = jsonMessage.getString("Value");
                client.popupMessage(value);
                break;

            case "StatusResponse":
                //Debugging
                System.out.println("case: statusResponse");
                JsonArray user = jsonMessage.getJsonArray("User");
                ArrayList<JsonObject> list = new ArrayList<>();
                //HashMap does not guarantee iteration-order, while LinkedHashmap does
                LinkedHashMap<String, String> userData = new LinkedHashMap();
                for(int i = 0; i < user.size(); i++){
                    if(!user.getJsonObject(i).containsKey(client.clientUsername)) list.add(user.getJsonObject(i));
                }
                list.sort((o1, o2) -> {
                    String one = o1.keySet().toString().toUpperCase();
                    String two = o2.keySet().toString().toUpperCase();
                    return one.compareTo(two);
                });
                list.forEach(object -> {
                    for (String key: object.keySet()){
                        userData.put(key, object.get(key).toString());
                    }
                });
                client.nwPage.getListResponse(userData);
                break;

            case "incomingCall":
                //Debugging
                System.out.println("case: incomingCall");

                client.nwPage.log("New call incoming...");
                bbbserver = jsonMessage.getString("Value");
                String username = jsonMessage.getString("Username");
                client.bbbserver = bbbserver;
                client.incomingCall(username);
                break;

            case "startedCall":
                //Debugging
                System.out.println("case: startedCall");

                client.nwPage.setCallDisplay(true);
                client.nwPage.log("Starting new call...");
                System.out.println(jsonMessage.getString("Value"));
                bbbserver = jsonMessage.getString("Value");
                client.bbbserver = bbbserver;
                break;

            case "RemoteCallEnded":
                //Debugging
                System.out.println("case: RemoteCallEnded");
                if(client.dialog != null){
                    client.dialog.dispose();
                    client.dialog = null;
                }
                client.callPartnerUsername = null;
                client.nwPage.gettingCalled = false;
                client.nwPage.callAccepted = false;
                client.nwPage.setCallDisplay(false);
                client.nwPage.log("User ended the call...");
                break;

            case "SelfCallEnded":
                //Debugging
                System.out.println("case: selfCallEnded");

                client.nwPage.callAccepted = false;
                client.nwPage.setCallDisplay(false);
                client.nwPage.log("You ended the call...");
                break;

            case "SelfCallAccepted":
                //Debugging
                System.out.println("case: selfCallAccepted");

                openURL(jsonMessage.getString("Value"));
                client.nwPage.log("You accepted the call...");
                client.nwPage.callAccepted = true;
                client.nwPage.updateCallDisplay();
                client.ongoingCall();
                break;

            case "RemoteCallAccepted":
                //Debugging
                System.out.println("case: remoteCallAccepted");
                openURL(jsonMessage.getString("Value"));
                client.nwPage.log("Call got accepted...");
                client.nwPage.callAccepted = true;
                client.nwPage.updateCallDisplay();
                client.ongoingCall();
                break;

            case "SelfCallDeclined":
                //Debugging
                System.out.println("case: selfCallDeclined");
                client.nwPage.log("You declined the call...");
                client.nwPage.callAccepted = false;
                client.nwPage.setCallDisplay(false);
                break;

            case "RemoteCallDeclined":
                //Debugging
                System.out.println("case: remoteCallDeclined");

                if(client.dialog != null){
                    client.dialog.dispose();
                    client.dialog = null;
                }
                client.nwPage.callAccepted = false;
                client.nwPage.setCallDisplay(false);
                client.nwPage.log("User declined your call...");
                break;
            case "inCall":
                //Debugging
                System.out.println("case: inCall");

                client.nwPage.log("User is in a call...");
                client.popupMessage(jsonMessage.getString("Value"));
                break;
            case "Offline":
                //Debugging
                System.out.println("case: offline");

                client.nwPage.log("User is offline");
                client.popupMessage(jsonMessage.getString("Value"));
                break;


            default:
                //Debugging
                System.out.println("case: default");
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

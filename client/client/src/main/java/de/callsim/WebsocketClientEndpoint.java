package de.callsim;

import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import java.awt.Desktop;
import java.io.IOException;

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
            client.popupMessage("Server is not active.\n" + e.toString());
            System.err.println(e.toString());
            System.exit(1);
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

    //The Server triggers the onMessage Function
    @OnMessage
    public void onMessage(String message) throws URISyntaxException {

        JsonReader reader = Json.createReader(new StringReader(message));
        JsonObject jsonMessage = reader.readObject();
        String action = jsonMessage.getString("Action");
        String value;
        String bbbserver;

        switch (action) {
            case "PosLoginResponse": // On successful Login
                //Debugging
                System.out.println("case: PosLoginResponse");

                client.clientUsername = jsonMessage.getString("Value"); // save the username that has logged in
                client.nwPage = new NextWindow(); // creating the NextWindow Object and setting assigning it to nwPage in client
                client.showAppPage(); // Close the Login Page and show the NextWindow Page
                break;

            case "NegLoginResponse": // On unsuccessful Login
                //Debugging
                System.out.println("case: NegLoginResponse");

                client.popupMessage(jsonMessage.getString("Value")); // show a PopUp Message with the Server Message
                break;

            case "PosRegisterResponse": // On successful Register
                //Debugging
                System.out.println("case: PosRegisterResponse");
                client.popupMessage(jsonMessage.getString("Value")); // show a PopUp message with the Server Message
                break;

            case "NegRegisterResponse": // On unsuccessful Register
                //Debugging
                System.out.println("case: NegRegisterResponse");
                client.popupMessage(jsonMessage.getString("Value")); // show a PopUp message with the Server Message
                break;

            case "StatusResponse":
                //Debugging
                System.out.println("case: statusResponse");
                JsonArray user = jsonMessage.getJsonArray("User"); // create a Json Array
                ArrayList<JsonObject> list = new ArrayList<>(); // create a ArrayList
                //HashMap does not guarantee iteration-order, while LinkedHashmap does
                LinkedHashMap<String, String> userData = new LinkedHashMap();
                for(int i = 0; i < user.size(); i++){
                    if(!user.getJsonObject(i).containsKey(client.clientUsername)) list.add(user.getJsonObject(i)); // Add every User except current User to the list
                }
                // Sort every user in list
                list.sort((o1, o2) -> {
                    String one = o1.keySet().toString().toUpperCase();
                    String two = o2.keySet().toString().toUpperCase();
                    return one.compareTo(two);
                });
                // Add every User to the Linked Hashmap
                list.forEach(object -> {
                    for (String key: object.keySet()){
                        userData.put(key, object.get(key).toString());
                    }
                });
                // trigger the getListResponse function inside NextWindow();
                client.nwPage.getListResponse(userData);
                break;

            case "incomingCall":
                //Save the BBB Link and trigger the incomingCall() function inside client

                //Debugging
                System.out.println("case: incomingCall");

                client.nwPage.log("New call incoming...");
                bbbserver = jsonMessage.getString("Value");
                String username = jsonMessage.getString("Username");
                client.bbbserver = bbbserver;
                client.incomingCall(username);
                break;

            case "startedCall":
                // Set the Call Display and save the BBB Link

                //Debugging
                System.out.println("case: startedCall");

                client.nwPage.setCallDisplay(true);
                client.nwPage.log("Starting new call...");
                System.out.println(jsonMessage.getString("Value"));
                bbbserver = jsonMessage.getString("Value");
                client.bbbserver = bbbserver;
                break;

            case "RemoteCallEnded":
                //Update the Log inside NextWindow and set callPartner to null and gettingCalled, call Accapted and callInProgress to false

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
                //Update the Log inside NextWindow and set gettingCalled, call Accapted and callInProgress to false

                //Debugging
                System.out.println("case: selfCallEnded");

                client.nwPage.callAccepted = false;
                client.nwPage.setCallDisplay(false);
                client.nwPage.log("You ended the call...");
                break;

            case "SelfCallAccepted":
                //Open the BBB-Link in standard browser, update the Display and set callAccepted to true

                //Debugging
                System.out.println("case: selfCallAccepted");

                openURL(jsonMessage.getString("Value"));
                client.nwPage.log("You accepted the call...");
                client.nwPage.callAccepted = true;
                client.nwPage.updateCallDisplay();
                client.ongoingCall();
                break;

            case "RemoteCallAccepted":
                //Open the BBB-Link in standard browser, update the Display and set callAccepted to true

                //Debugging
                System.out.println("case: remoteCallAccepted");

                openURL(jsonMessage.getString("Value"));
                client.nwPage.log("Call got accepted...");
                client.nwPage.callAccepted = true;
                client.nwPage.updateCallDisplay();
                client.ongoingCall();
                break;

            case "SelfCallDeclined":
                //Open the BBB-Link in standard browser, update the Display and set callAccepted to true

                //Debugging
                System.out.println("case: selfCallDeclined");

                client.nwPage.log("You declined the call...");
                client.nwPage.callAccepted = false;
                client.nwPage.setCallDisplay(false);
                break;

            case "RemoteCallDeclined":
                //close the dialog and set callAccepted and callInProgress to false

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
                //Show a PopUp Message and set callPartnerUsername to null

                //Debugging
                System.out.println("case: inCall");
                client.callPartnerUsername = null;
                client.nwPage.log("User is in a call...");
                client.popupMessage(jsonMessage.getString("Value"));
                break;
            case "Offline":
                //Show a PopUp Message and set callPartnerUsername to null

                //Debugging
                System.out.println("case: offline");
                client.callPartnerUsername = null;
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

package de.callsim;

import java.net.URI;
import java.net.URISyntaxException;

import javax.json.Json;
import javax.json.JsonObject;

public class client {

    public static void main(String[] args) {
        try {
            // open websocket
            final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(
                    new URI("ws://localhost:8787/ws"));

            JsonObject value = Json.createObjectBuilder()
                .add("action", "register")
                .add("Username", "Fabi")
                .add("Password", "1234")
                .build();

            // send REGISTER message to websocket
            clientEndPoint.sendMessage(value);

            System.console().readLine();

            value = Json.createObjectBuilder()
                .add("action", "login")
                .add("Username", "Fabi")
                .add("Password", "1234")
                .build();

            // send LOGIN message to websocket
            clientEndPoint.sendMessage(value);

            System.console().readLine();

            value = Json.createObjectBuilder()
                .add("action", "register")
                .add("Username", "Fabi")
                .add("Password", "1234")
                .build();

            // send 2ND REGISTER message to websocket
            clientEndPoint.sendMessage(value);

            System.console().readLine();

            value = Json.createObjectBuilder()
                .add("action", "onlineUser")
                .build();

            // send ONLINEUSER message to websocket
            clientEndPoint.sendMessage(value);

            System.console().readLine();

        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        }
    }
}

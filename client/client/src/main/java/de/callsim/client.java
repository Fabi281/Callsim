package de.callsim;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.swing.*;

public class client {

    public static void main(String[] args) {

        final boolean showLogin = true;

        if(showLogin){
            JFrame frame = new JFrame("CallSim Login");
            frame.setContentPane(new Login().panel1);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setMinimumSize(new Dimension(500,400));
            frame.pack();
            frame.setVisible(true);
        }
        else{
            JFrame frame = new JFrame("App");

            frame.setContentPane(new NextWindow().panel1);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setMinimumSize(new Dimension(400, 450)); // ref: https://stackoverflow.com/a/2782041
            frame.pack();
            frame.setVisible(true);
        }
    }

    public void startConnection(){

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
                .add("action", "UserStatuses")
                .build();

            // send ONLINEUSER message to websocket
            clientEndPoint.sendMessage(value);

            System.console().readLine();

        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        }
    }
}

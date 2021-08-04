package de.callsim;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.*;

import javax.json.Json;
import javax.json.JsonObject;
import javax.swing.*;

public class client {
    private static JFrame frame;
    public static WebsocketClientEndpoint clientEndPoint;

    public static void main(String[] args) {

        if(ConnectionInit()) showLoginPage();

    }

    public static boolean ConnectionInit(){
        try {
            // open websocket
            clientEndPoint = new WebsocketClientEndpoint(new URI("ws://localhost:8787/ws"));
            return true;

        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
            return false;
        }
    }

    public static void showLoginPage(){
        frame = new JFrame("CallSim Login");
        frame.setContentPane(new Login().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(500,400));
        frame.pack();
        frame.setVisible(true);
    }

    public static void showAppPage(){
        frame.setTitle("CallSim App");
        frame.setContentPane(new NextWindow().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(400, 450)); // ref: https://stackoverflow.com/a/2782041
        frame.pack();
        frame.setVisible(true);
    }
}

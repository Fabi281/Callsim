package de.callsim;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

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
        frame.setContentPane(new Login().root_panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension size = new Dimension(600,500);
        frame.setMinimumSize(size);
        frame.setMaximumSize(size); /* so you cannot change size of panel, but should fit everything */
        frame.pack();
        frame.setVisible(true);
    }

    public static void showAppPage(HashMap<String, String> userData){
        if(frame == null) frame = new JFrame("CallSim"); /* just to make sure */
        frame.setTitle("CallSim App");
        frame.setContentPane(new NextWindow(userData).rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(400, 450)); // ref: https://stackoverflow.com/a/2782041
        frame.pack();
        frame.setVisible(true);
    }
}

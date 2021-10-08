package de.callsim;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import javax.json.Json;
import javax.json.JsonObject;
import javax.swing.*;

public class client {
    private static JFrame frame;
    public static WebsocketClientEndpoint clientEndPoint;
    public static Login loginPage;
    public static NextWindow nwPage;
    public static JDialog dialog;
    public static String clientUsername;
    public static String bbbserver;

    public static void main(String[] args) {
        if(ConnectionInit()) showLoginPage();
    }

    public static boolean ConnectionInit(){
        try {
            // open websocket
            clientEndPoint = new WebsocketClientEndpoint(new URI("ws://localhost:8787/ws"));
            loginPage =  new Login();
            return true;

        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
            return false;
        }
    }

    public static void popupMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    public static void incomingCall(String msg, String username){
        dialog = generateDialog(msg, username);
        /*
        int input = JOptionPane.showOptionDialog(null, msg, "Call", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
        if(input == JOptionPane.OK_OPTION)
        {
            System.out.println("Hello World");
            JsonObject value = Json.createObjectBuilder()
                    .add("action", "respondCall")
                    .add("Response", "accept")
                    .add("Username", username)
                    .add("BBBServer", bbbserver)
                    .build();
            // send respondCall message to websocket
            client.clientEndPoint.sendMessage(value);
        }
        else if (input == JOptionPane.CANCEL_OPTION){

        }
        */
    }

    public static JDialog generateDialog (String msg, String username){
        JDialog dialogPane = new JDialog();
        IncomingCall icPage = new IncomingCall();
        JLabel label = icPage.nameLabel;
        label.setText(username);
        JButton accept = icPage.acceptBtn;
        JButton decline = icPage.declineBtn;

        accept.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Hello World");
                JsonObject value = Json.createObjectBuilder()
                        .add("action", "respondCall")
                        .add("Response", "accept")
                        .add("Username", username)
                        .add("BBBServer", bbbserver)
                        .build();
                client.clientEndPoint.sendMessage(value);
                dialog.dispose();
                dialog = null;
            }
        });

        decline.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Hello World");
                JsonObject value = Json.createObjectBuilder()
                        .add("action", "endCall")
                        .add("Username", username)
                        .add("BBBServer", bbbserver)
                        .build();
                // send respondCall message to websocket
                client.clientEndPoint.sendMessage(value);
                dialog.dispose();
                dialog = null;
            }
        });
        dialogPane.add(icPage.rootPanel);
        dialogPane.setTitle("Incoming Call...");
        dialogPane.setSize(200, 220);
        dialogPane.setLocationRelativeTo(nwPage.rootPanel);
        dialogPane.setAutoRequestFocus(true);
        dialogPane.setVisible(true);
        return dialogPane;
    }

    public static void showLoginPage(){
        frame = new JFrame("CallSim Login");
        frame.setContentPane(loginPage.root_panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension size = new Dimension(600,500);
        frame.setMinimumSize(size);
        frame.setMaximumSize(size); /* so you cannot change size of panel, but should fit everything */
        frame.pack();
        frame.setVisible(true);
    }

    public static void showAppPage(){
        if(frame == null) frame = new JFrame("CallSim"); /* just to make sure */
        frame.setTitle("CallSim App - " + clientUsername);
        frame.setContentPane(nwPage.rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(400, 450)); // ref: https://stackoverflow.com/a/2782041
        frame.pack();
        frame.setVisible(true);
    }
}

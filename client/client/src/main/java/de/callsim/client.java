package de.callsim;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.net.URISyntaxException;

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
    public static String callPartnerUsername = null;
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
        JDialog dialogPane = new JDialog();
        IncomingCall icPage = new IncomingCall();
        dialogPane.add(icPage.rootPanel);
        dialogPane.setTitle("Incoming Call...");
        icPage.nameLabel.setText(username);
        icPage.acceptBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                callPartnerUsername = username;
                dialog.dispose();
                dialog = null;
                JsonObject value = Json.createObjectBuilder()
                        .add("action", "respondCall")
                        .add("Response", "accept")
                        .add("Username", username)
                        .add("BBBServer", bbbserver)
                        .build();
                client.clientEndPoint.sendMessage(value);
                nwPage.updateBigDisplay(username);
                nwPage.setCallDisplay(true);
            }
        });

        icPage.declineBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nwPage.callInProgress = false;
                dialog.dispose();
                dialog = null;
                JsonObject value = Json.createObjectBuilder()
                        .add("action", "respondCall")
                        .add("Response", "decline")
                        .add("Username", username)
                        .add("BBBServer", bbbserver)
                        .build();
                // send respondCall message to websocket
                client.clientEndPoint.sendMessage(value);
                callPartnerUsername = null;
            }
        });
        dialogPane.setSize(200, 220);
        dialogPane.setLocationRelativeTo(nwPage.rootPanel);
        dialogPane.setAutoRequestFocus(true);
        dialogPane.setVisible(true);
        dialogPane.setAlwaysOnTop(true);
        dialogPane.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                nwPage.callInProgress = false;
                dialog.dispose();
                dialog = null;
                JsonObject value = Json.createObjectBuilder()
                        .add("action", "respondCall")
                        .add("Response", "decline")
                        .add("Username", username)
                        .add("BBBServer", bbbserver)
                        .build();
                // send respondCall message to websocket
                client.clientEndPoint.sendMessage(value);
                callPartnerUsername = null;
            }
        });
        dialog = dialogPane;

    }

    public static void ongoingCall (){
        JDialog dialogPane = new JDialog();
        RunningCall rcPage = new RunningCall();
        dialogPane.add(rcPage.rootPanel);
        dialogPane.setTitle("Call...");

        rcPage.endcallBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                dialog = null;
                JsonObject value = Json.createObjectBuilder()
                        .add("action", "endCall")
                        .add("Username", callPartnerUsername)
                        .add("BBBServer", bbbserver)
                        .build();
                // send respondCall message to websocket
                client.clientEndPoint.sendMessage(value);
                callPartnerUsername = null;
            }
        });

        dialogPane.setSize(300, 220);
        dialogPane.setLocationRelativeTo(nwPage.rootPanel);
        dialogPane.setAutoRequestFocus(true);
        dialogPane.setVisible(true);
        dialogPane.setAlwaysOnTop(true);
        dialogPane.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dialog.dispose();
                dialog = null;
                JsonObject value = Json.createObjectBuilder()
                        .add("action", "endCall")
                        .add("Username", callPartnerUsername)
                        .add("BBBServer", bbbserver)
                        .build();
                // send respondCall message to websocket
                client.clientEndPoint.sendMessage(value);
                callPartnerUsername = null;
            }
        });
        dialog = dialogPane;
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

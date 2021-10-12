package de.callsim;

import java.awt.*;
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

    // Windows

    public static void popupMessage(String msg) {
        JOptionPane pane = new JOptionPane(msg);
        JDialog dialogPane = pane.createDialog((JFrame)null, "Pop Up");
        dialogPane.setLocationRelativeTo(frame);
        dialogPane.setVisible(true);
    }

    public static void incomingCall(String username){
        callPartnerUsername = username;
        nwPage.gettingCalled = true;

        JDialog dialogPane = new JDialog();
        IncomingCall icPage = new IncomingCall();
        dialogPane.add(icPage.rootPanel);
        dialogPane.setTitle("Incoming Call...");
        icPage.nameLabel.setText(username);
        icPage.acceptBtn.addActionListener(e -> {
            nwPage.selectedUser = username;
            dialog.dispose();
            dialog = null;
            sendRespondAcceptMessage();
            nwPage.updateBigDisplay(username);
            nwPage.setCallDisplay(true);
            nwPage.gettingCalled = false;
        });

        icPage.declineBtn.addActionListener(e -> dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING)));
        dialogPane.setSize(200, 220);
        dialogPane.setLocationRelativeTo(frame);
        dialogPane.setAutoRequestFocus(true);
        dialogPane.setVisible(true);
        dialogPane.setAlwaysOnTop(true);
        dialogPane.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                nwPage.callInProgress = false;
                dialog.dispose();
                dialog = null;
                nwPage.gettingCalled = false;
                sendRespondDeclineMessage();
            }
        });
        dialog = dialogPane;
    }

    public static void ongoingCall (){
        JDialog dialogPane = new JDialog();
        RunningCall rcPage = new RunningCall();
        dialogPane.add(rcPage.rootPanel);
        dialogPane.setTitle("Call...");
        rcPage.otherUserLabel.setText(callPartnerUsername);
        rcPage.endcallBtn.addActionListener(e -> dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING)));

        dialogPane.setSize(300, 220);
        dialogPane.setLocationRelativeTo(frame);
        dialogPane.setAutoRequestFocus(true);
        dialogPane.setVisible(true);
        dialogPane.setAlwaysOnTop(true);
        dialogPane.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                sendEndCallMessage();
                dialog.dispose();
                dialog = null;
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
        if(frame == null) frame = new JFrame("CallSim App - " + clientUsername);
        frame.setTitle("CallSim App - " + clientUsername);
        frame.setContentPane(nwPage.rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(400, 450)); // ref: https://stackoverflow.com/a/2782041
        frame.pack();
        frame.setVisible(true);
    }

    // connection handling

    public static void sendLoginMessage(String username, String password){
        JsonObject value = Json.createObjectBuilder()
                .add("action", "login")
                .add("Username", username)
                .add("Password", password)
                .build();
        // send LOGIN message to websocket
        clientEndPoint.sendMessage(value);
    }

    public static void sendRegisterMessage(String username, String password, String passwordCon){
        if (password.equals(passwordCon)) {
            JsonObject value = Json.createObjectBuilder()
                    .add("action", "register")
                    .add("Username", username)
                    .add("Password", password)
                    .build();
            // send REGISTER message to websocket
            clientEndPoint.sendMessage(value);
        }
        else {
            popupMessage("Passwords do not match!");
        }
    }

    public static void sendStartCallMessage(String username){
        callPartnerUsername = username;
        JsonObject json = Json.createObjectBuilder()
                .add("action", "startCall")
                .add("Username", callPartnerUsername)
                .build();
        // send startCall message to websocket
        clientEndPoint.sendMessage(json);
    }

    public static void sendRespondSelfDeclineMessage(){
        JsonObject value = Json.createObjectBuilder()
                .add("action", "respondCall")
                .add("Response", "selfdecline")
                .add("Username", callPartnerUsername)
                .add("BBBServer", client.bbbserver)
                .build();
        // send RESPONDCALL - SELFDECLINE message to websocket
        clientEndPoint.sendMessage(value);
        callPartnerUsername = null;
    }

    public static void sendRespondAcceptMessage(){
        JsonObject value = Json.createObjectBuilder()
                .add("action", "respondCall")
                .add("Response", "accept")
                .add("Username", callPartnerUsername)
                .add("BBBServer", bbbserver)
                .build();
        clientEndPoint.sendMessage(value);
    }

    public static void sendRespondDeclineMessage(){
        JsonObject value = Json.createObjectBuilder()
                .add("action", "respondCall")
                .add("Response", "decline")
                .add("Username", callPartnerUsername)
                .add("BBBServer", bbbserver)
                .build();
        // send RESPONDCALL - DECLINE message to websocket
        clientEndPoint.sendMessage(value);
        callPartnerUsername = null;
    }

    public static void sendEndCallMessage(){
        JsonObject value = Json.createObjectBuilder()
                .add("action", "endCall")
                .add("Username", callPartnerUsername)
                .add("BBBServer", bbbserver)
                .build();
        // send ENDCALL message to websocket
        clientEndPoint.sendMessage(value);
    }
}

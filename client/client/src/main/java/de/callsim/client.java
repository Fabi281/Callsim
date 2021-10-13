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
    private static JFrame frame; // JFrame that Displays all the Windows
    // Every variable has been made public in Order to be accessible inside WebsocketClientEndpoint
    public static WebsocketClientEndpoint clientEndPoint; // Websocket Object
    public static Login loginPage; //Login-Page Object
    public static NextWindow nwPage; // Next-Window-Page Object
    public static JDialog dialog; // Dialog Object
    public static String clientUsername; // Username of the client that is logged in
    public static String callPartnerUsername = null; // Username of the Person that has been called
    public static String bbbserver; // The BBB-Link

    // Main Function. This function will be executed upon starting the App
    public static void main(String[] args) {
        // Check whether a connection to the Websocket could be made. On true, show the Login-Page
        if(ConnectionInit()) showLoginPage();
    }

    // Connecting to the Web-Socket
    public static boolean ConnectionInit(){
        // Try to establish a connection. On Success, return true. Else return false
        try {
            // open websocket
            clientEndPoint = new WebsocketClientEndpoint(new URI("ws://localhost:8787/ws"));
            // Creating Login-Page Object
            loginPage =  new Login();
            return true;

        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
            return false;
        }
    }

    // Windows

    // Function to create PopUp Messages. Parameter is a String with a Message that should be displayed
    public static void popupMessage(String msg) {
        // Creating an Option Pane with the Text-Message
        JOptionPane pane = new JOptionPane(msg);
        // Creating the Dialog Window with the Title "Pop Up"
        JDialog dialogPane = pane.createDialog((JFrame)null, "Pop Up");
        // Center the PopUp inside the JFrame
        dialogPane.setLocationRelativeTo(frame);
        // Make the PopUp visible
        dialogPane.setVisible(true);
    }

    // Dialog Message that shows an incoming call from a person
    public static void incomingCall(String username){
        // Setting the call partner
        callPartnerUsername = username;
        // Set gettingCalled to true in Order to limit functionallity
        nwPage.gettingCalled = true;
        // Create new Dialog
        JDialog dialogPane = new JDialog();
        // Create new Incoming Call Object
        IncomingCall icPage = new IncomingCall();
        // Add the InComingCall rootPanel to the Dialog-Pane
        dialogPane.add(icPage.rootPanel);
        // Setting the Title
        dialogPane.setTitle("Incoming Call...");
        // Setting the displayed Username
        icPage.nameLabel.setText(username);
        // Add an actionListener to the accept button
        icPage.acceptBtn.addActionListener(e -> {
            nwPage.selectedUser = username;
            dialog.dispose();
            dialog = null;
            // Sends a Message to the Websocket
            sendRespondAcceptMessage();
            // Display Person you're in a call with
            nwPage.updateBigDisplay(username);
            nwPage.setCallDisplay(true);
            // No longer in gettingCalled state, but in callInProgress State
            nwPage.gettingCalled = false;
        });
        // Add an actionListener to the accept button
        icPage.declineBtn.addActionListener(e -> {
            // On decline, close window and trigger the Window Closing Operation
            dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
        });
        // Setting up some configurations like size, location, visibility, etc.
        dialogPane.setSize(200, 220);
        dialogPane.setLocationRelativeTo(frame);
        dialogPane.setAutoRequestFocus(true);
        dialogPane.setVisible(true);
        dialogPane.setAlwaysOnTop(true);
        dialogPane.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                /*
                On Closing, send a decline call response to the websocket and
                set gettingCalled and callInProgress to false
                 */
                nwPage.callInProgress = false;
                nwPage.gettingCalled = false;
                dialog.dispose();
                dialog = null;
                sendRespondDeclineMessage();
            }
        });
        dialog = dialogPane;
    }

    // Dialog Message that shows that a call is onGoing
    public static void ongoingCall (){
        /*
        Functions similar to the incomingCall Function
         */
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
                /*
                Sending an end call response to the websocket, close the Dialog Pane and set callPartner to null.
                callPartner null is important because on exiting the App, it checks if you're still in a call or not
                and sends a decline call or end call response to the websocket if callPartnerUsername not equal null
                 */
                sendEndCallMessage();
                dialog.dispose();
                dialog = null;
                callPartnerUsername = null;
            }
        });
        dialog = dialogPane;
    }

    // Shows the Login Page
    public static void showLoginPage(){
        frame = new JFrame("CallSim Login");
        frame.setContentPane(loginPage.root_panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension size = new Dimension(600,600);
        frame.setMinimumSize(size);
        frame.setMaximumSize(size); /* so you cannot change size of panel, but should fit everything */
        frame.pack();
        frame.setVisible(true);
    }

    // Shows the NextWindow Page
    public static void showAppPage(){
        if(frame == null) frame = new JFrame("CallSim App - " + clientUsername);
        frame.setTitle("CallSim App - " + clientUsername);
        frame.setContentPane(nwPage.rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(600, 600)); // ref: https://stackoverflow.com/a/2782041
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                /*
                When exiting the App, check whether you're getting called ot are in a call and send a response according
                to the state.
                 */
                if (nwPage.gettingCalled && callPartnerUsername != null){
                    sendRespondDeclineMessage();
                }
                else if(callPartnerUsername != null){
                    sendEndCallMessage();
                }
            }
        });
    }

    // Connection handling

    // Build a JSONObject and send it to the Websocket. LOGIN
    public static void sendLoginMessage(String username, String password){
        JsonObject value = Json.createObjectBuilder()
                .add("action", "login")
                .add("Username", username)
                .add("Password", password)
                .build();
        // send LOGIN message to websocket
        clientEndPoint.sendMessage(value);
    }

    // Build a JSONObject and send it to the Websocket. REGISTER
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

    // Build a JSONObject and send it to the Websocket. STARTCALL
    public static void sendStartCallMessage(String username){
        callPartnerUsername = username;
        JsonObject json = Json.createObjectBuilder()
                .add("action", "startCall")
                .add("Username", callPartnerUsername)
                .build();
        // send startCall message to websocket
        clientEndPoint.sendMessage(json);
    }

    // Build a JSONObject and send it to the Websocket. SELF DECLINE
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

    // Build a JSONObject and send it to the Websocket. ACCEPT
    public static void sendRespondAcceptMessage(){
        JsonObject value = Json.createObjectBuilder()
                .add("action", "respondCall")
                .add("Response", "accept")
                .add("Username", callPartnerUsername)
                .add("BBBServer", bbbserver)
                .build();
        clientEndPoint.sendMessage(value);
    }

    // Build a JSONObject and send it to the Websocket. DECLINE
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

    // Build a JSONObject and send it to the Websocket. ENDCALL
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

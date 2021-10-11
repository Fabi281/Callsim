package de.dhbwheidenheim.informatik.callsim.controller;

import de.dhbwheidenheim.informatik.callsim.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import java.io.IOException;
import java.io.StringReader;

@ServerEndpoint("/ws")
public class WebSocket {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocket.class);
    private static final String DataLocation = "./data/Account";

    @OnOpen
    public void onOpen(Session session) {
        LOGGER.info("onOpen " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {

        // Convert the incoming message into JSON and extract the action
        // the server should take
        JsonReader reader = Json.createReader(new StringReader(message));
        JsonObject jsonMessage = reader.readObject();
        String action = jsonMessage.getString("action");

        // Declare helper variables
        ArrayList<User> users = Utils.readFromFile(DataLocation);
        boolean exists;

        // Take action based on the message-content
        switch (action) {

            case "login":
                // Check if a user with the given parameters(username, password) exists
                exists = users.stream().filter(p -> (p.getUsername().equals(jsonMessage.getString("Username"))
                        && p.getPassword().equals(jsonMessage.getString("Password")))).findFirst().isPresent();

                // If one exists and isn´t already logged send a positive response and set the User 
                // as logged in
                if (exists && SessionHandler.checkLogin(jsonMessage.getString("Username"))) {
                    session.getBasicRemote()
                            .sendText(Utils.buildResponse("PosLoginResponse", jsonMessage.getString("Username")));
                    SessionHandler.addSession(session, jsonMessage.getString("Username"));
                } else {

                    // If one does not exist or is already logged in send a corresponding negative response
                    String msg;
                    if (!exists)
                        msg = "Username does not exist";
                    else
                        msg = "User is already logged in";
                    session.getBasicRemote().sendText(Utils.buildResponse("NegLoginResponse", msg));

                }
                break;

            case "register":
                // Check if the username is already taken
                exists = users.stream().filter(p -> (p.getUsername().equals(jsonMessage.getString("Username"))))
                        .findFirst().isPresent();
                
                // If it is taken send a negative response        
                if (exists) {
                    session.getBasicRemote()
                            .sendText(Utils.buildResponse("NegRegisterResponse", "Username already in use!"));
                } else {
                    // If it is not taken register the user and send a positive response
                    User registerUser = new User(jsonMessage.getString("Username"), jsonMessage.getString("Password"));
                    Utils.registerUser(registerUser, DataLocation, users);
                    session.getBasicRemote()
                            .sendText(Utils.buildResponse("PosRegisterResponse", "Successfully registered!"));
                }
                break;

            case "UserStatuses":
                // Send a Object containing all users and their corresponding status
                session.getBasicRemote().sendText(Utils.buildResponse(SessionHandler.userListWithStatus(users)));
                break;

            case "startCall":
                SessionHandler.startCall(session, jsonMessage.getString("Username"));
                break;

            case "endCall":
                SessionHandler.endCall(session, jsonMessage.getString("Username"), jsonMessage.getString("BBBServer"));
                break;

            case "respondCall":
                SessionHandler.respondToCall(session, jsonMessage.getString("Response"),
                        jsonMessage.getString("Username"), jsonMessage.getString("BBBServer"));
                break;

            default:
                // Send a default message if the client sends an unrecognizable action
                session.getBasicRemote().sendText(Utils.buildResponse("NotFound", "Not Found"));
                LOGGER.info("No corresponding action found");
        }
    }

    @OnClose
    public void onClose(Session session) {
        // Try to delete the session in the handler if the websocket is closed
        try {
            SessionHandler.deleteSession(session);
        } catch (Exception e) {
            LOGGER.info("Session war nicht im Handler" + e.toString());
        }
    }

    @OnError
    public void onError(Throwable e) {
        LOGGER.error(e.getMessage(), e);
    }
}
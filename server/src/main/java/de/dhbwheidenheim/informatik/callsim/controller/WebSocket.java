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
        JsonReader reader = Json.createReader(new StringReader(message));
        JsonObject jsonMessage = reader.readObject();
        String action = jsonMessage.getString("action");
        ArrayList<User> users = Utils.readFromFile(DataLocation);
        boolean exists;

        switch (action) {
            case "login":
                exists = users.stream().filter(p -> (p.getUsername().equals(jsonMessage.getString("Username"))
                        && p.getPassword().equals(jsonMessage.getString("Password")))).findFirst().isPresent();
                LOGGER.info("Login Bool: " + exists);

                if (exists && SessionHandler.checkLogin(jsonMessage.getString("Username"))) {
                    session.getBasicRemote()
                            .sendText(Utils.buildResponse("PosLoginResponse", "Succesfully logged in!"));
                    SessionHandler.addSession(session, jsonMessage.getString("Username"));
                } else {
                    String msg;
                    if (!exists)
                        msg = "Username does not exist";
                    else
                        msg = "User is already logged in";
                    session.getBasicRemote().sendText(Utils.buildResponse("NegLoginResponse", msg));
                }
                break;

            case "register":
                exists = users.stream().filter(p -> (p.getUsername().equals(jsonMessage.getString("Username"))))
                        .findFirst().isPresent();
                if (exists) {
                    session.getBasicRemote()
                            .sendText(Utils.buildResponse("NegRegisterResponse", "Username already in use!"));
                } else {
                    User registerUser = new User(jsonMessage.getString("Username"), jsonMessage.getString("Password"));
                    Utils.registerUser(registerUser, DataLocation);
                    session.getBasicRemote()
                            .sendText(Utils.buildResponse("PosRegisterResponse", "Successfully registered!"));
                }
                break;

            case "UserStatuses":
                session.getBasicRemote().sendText(Utils.buildResponse(SessionHandler.userListWithStatus(DataLocation)));
                break;

            case "startCall":
                SessionHandler.startCall(session, jsonMessage.getString("Username"));
                break;

            case "endCall":
                SessionHandler.endCall(session, jsonMessage.getString("Username"), jsonMessage.getString("BBBServer"));
                break;

            case "respondCall":
                SessionHandler.respondToCall(session, jsonMessage.getString("Response"),
                        jsonMessage.getString("Username"));
                break;

            default:
                session.getBasicRemote().sendText(Utils.buildResponse("NotFound", "Not Found"));
                LOGGER.info("Kein passendes Kommando");
        }
    }

    @OnClose
    public void onClose(Session session) {
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
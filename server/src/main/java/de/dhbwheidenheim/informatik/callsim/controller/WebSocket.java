package de.dhbwheidenheim.informatik.callsim.controller;

import de.dhbwheidenheim.informatik.callsim.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import javax.websocket.*;
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
    public void onMessage(String message, Session session) throws IOException{
        JsonReader reader = Json.createReader(new StringReader(message));
        JsonObject jsonMessage = reader.readObject();
        String action = jsonMessage.getString("action");

        switch(action){
            case "login":
                ArrayList<User> users = Utils.readFromFile(DataLocation);
                boolean exists = users.stream().filter(p -> (p.getUsername().equals(jsonMessage.getString("Username")) 
                    && p.getPassword().equals(jsonMessage.getString("Password")))).findFirst().isPresent();
                    LOGGER.info("Login Bool: " + exists);
                if(exists){
                    session.getBasicRemote().sendText(Utils.buildResponse("200", "OK"));
                }else{
                    session.getBasicRemote().sendText(Utils.buildResponse("401", "Unauthorized"));
                }
                break;
            case "register":
                User registerUser = new User(jsonMessage.getString("Username"), jsonMessage.getString("Password"));
                Utils.registerUser(registerUser, DataLocation);
                session.getBasicRemote().sendText(Utils.buildResponse("200", "OK"));
                break;
            default:
                session.getBasicRemote().sendText(Utils.buildResponse("404", "Not Found"));
                LOGGER.info("Kein passendes Kommando");
        }
    }

    @OnClose
    public void onClose(Session session) {
        LOGGER.info("onClose " + session.getId());
    }

    @OnError
    public void onError(Throwable e) {
        LOGGER.error(e.getMessage(), e);
    }
}
package de.dhbwheidenheim.informatik.callsim.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.Session;

public class SessionHandler {
    private final static Map<Session, ArrayList<String>> sessions = new HashMap<Session, ArrayList<String>>();

    public static void addSession(Session session, String Username){
        sessions.put(session, new ArrayList<String>());
        sessions.get(session).add(Username);
        sessions.get(session).add("Online");
    }

    public static void deleteSession(Session session){
        sessions.remove(session);
    }

    public static Collection<ArrayList<String>> userListWithStatus(){
        return sessions.values();
    }

    public static boolean checkLogin(String Username){
        for(ArrayList<String> user : sessions.values()){
            if(user.get(0).equals(Username)){
                return false;
            }
        }
        return true;
    }

    public static void startCall(Session initialSession, Session userToCall){
        sessions.get(initialSession).set(1, "inCall");
        sessions.get(userToCall).set(1, "inCall");
    }

    public static void endCall(Session initialSession, Session userToCall){
        sessions.get(initialSession).set(1, "Online");
        sessions.get(userToCall).set(1, "Online");
    }
}

package de.dhbwheidenheim.informatik.callsim.controller;

import java.util.Collection;
import java.util.HashMap;
import javax.websocket.Session;

public class SessionHandler {
    private final static HashMap<Session, String> sessions = new HashMap<Session, String>();

    public static void addSession(Session session, String Username){
        sessions.put(session, Username);
    }

    public static void deleteSession(Session session){
        sessions.remove(session);
    }

    public static Collection<String> getOnlineUser(){
        return sessions.values();
    }
}

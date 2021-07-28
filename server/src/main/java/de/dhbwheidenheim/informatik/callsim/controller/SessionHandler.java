package de.dhbwheidenheim.informatik.callsim.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.Session;

public class SessionHandler {

    private final static Map<Session, ArrayList<String>> sessions = new HashMap<Session, ArrayList<String>>();

    // You could use a BidiMap here but i decided on two seperate Hashmaps as we do not need the huge Apache Common Library for
    // this approach and do not need to iterate over the Map each time we want to check the Session by Username
    private final static Map<String, Session> activeUser = new HashMap<String, Session>();

    private final static Map<String, String> BBBServer  = new HashMap<String, String>() {{
        put("https://bbb.dhbw-heidenheim.de/?M=xzNW8jI0M2U3Mu862pnC", "available");
        put("https://bbb.dhbw-heidenheim.de/?M=eBtgjI0NDNlMushX31Zj", "available");
        put("https://bbb.dhbw-heidenheim.de/?M=MwIJjI0MmQwMgkoqcKIf", "available");
    }};

    public static void addSession(Session session, String Username){
        sessions.put(session, new ArrayList<String>());
        sessions.get(session).add(Username);
        sessions.get(session).add("Online");

        activeUser.put(Username, session);
    }

    public static void deleteSession(Session session){
        activeUser.remove(sessions.get(session).get(0));
        sessions.remove(session);
    }

    public static Collection<ArrayList<String>> userListWithStatus(){
        return sessions.values();
    }

    public static boolean checkLogin(String Username){
        if(activeUser.containsKey(Username)){
                return false;
            }
        return true;
    }

    public static void startCall(Session initialSession, String userToCall) throws IOException{

        sessions.get(initialSession).set(1, "inCall");
        sessions.get(activeUser.get(userToCall)).set(1, "inCall");

        activeUser.get(userToCall).getBasicRemote().sendText(Utils.buildResponse("incomingCall", "Got called respond now"));
        initialSession.getBasicRemote().sendText(Utils.buildResponse("startedCall", "Started Call waiting for response")); 

    }

    public static void endCall(Session initialSession, String userToReset, String usedServer) throws IOException{

        sessions.get(initialSession).set(1, "Online");
        sessions.get(activeUser.get(userToReset)).set(1, "Online");  

        BBBServer.put(usedServer, "available");
        activeUser.get(userToReset).getBasicRemote().sendText(Utils.buildResponse("RemoteCallEnded", "The other Person ended the Call"));
        initialSession.getBasicRemote().sendText(Utils.buildResponse("SelfCallEnded", "You ended the Call"));

    }

    public static void respondToCall(Session initialSession, String response, String user) throws IOException{
        
        if(response.equals("accept")){
            
            String availableServer = BBBServer.entrySet().stream().filter(link -> link.getValue().equals("available")).findFirst().get().getKey();

            BBBServer.put(availableServer, "inUse");

            initialSession.getBasicRemote().sendText(Utils.buildResponse("SelfCallAccepted", availableServer));
            activeUser.get(user).getBasicRemote().sendText(Utils.buildResponse("RemoteCallAccepted", availableServer));

        }else{

            initialSession.getBasicRemote().sendText(Utils.buildResponse("SelfCallDeclined", "You declined the incoming Call")); 
            activeUser.get(user).getBasicRemote().sendText(Utils.buildResponse("RemoteCallDeclined", "Your Call got declined"));
            
        }
    }
}

package de.dhbwheidenheim.informatik.callsim.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.Session;

import de.dhbwheidenheim.informatik.callsim.model.User;

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

    public static Map<String, String> userListWithStatus(String location) throws IOException{
        Map<String, String> allUserStatuses = new HashMap<String, String>();
        for(ArrayList<String> user : sessions.values()){
            allUserStatuses.put(user.get(0), user.get(1));
        }

        for(User user : Utils.readFromFile(location)){
            allUserStatuses.putIfAbsent(user.getUsername(), "Offline");
        }
        return allUserStatuses;
    }

    public static boolean checkLogin(String Username){
        if(activeUser.containsKey(Username)){
                return false;
            }
        return true;
    }

    public static void startCall(Session initialSession, String userToCall) throws IOException{

        
        var wrapper = new Object(){ String availableServer = ""; };
        BBBServer.entrySet().stream().filter(link -> link.getValue().equals("available")).findFirst().ifPresent(s -> wrapper.availableServer = s.getKey());
        
        if(wrapper.availableServer.equals("")){
            initialSession.getBasicRemote().sendText(Utils.buildResponse("CallFailed", "No Server available")); 
            return;
        }

        BBBServer.put(wrapper.availableServer, "inUse");

        sessions.get(initialSession).set(1, "inCall");
        sessions.get(activeUser.get(userToCall)).set(1, "inCall");

        activeUser.get(userToCall).getBasicRemote().sendText(Utils.buildResponse("incomingCall", wrapper.availableServer));
        initialSession.getBasicRemote().sendText(Utils.buildResponse("startedCall", wrapper.availableServer)); 

    }

    public static void endCall(Session initialSession, String userToReset, String usedServer) throws IOException{

        BBBServer.put(usedServer, "available");

        sessions.get(initialSession).set(1, "Online");
        sessions.get(activeUser.get(userToReset)).set(1, "Online");  
        
        activeUser.get(userToReset).getBasicRemote().sendText(Utils.buildResponse("RemoteCallEnded", "The other Person ended the Call"));
        initialSession.getBasicRemote().sendText(Utils.buildResponse("SelfCallEnded", "You ended the Call"));

    }

    public static void respondToCall(Session initialSession, String response, String user) throws IOException{
        
        if(response.equals("accept")){

            initialSession.getBasicRemote().sendText(Utils.buildResponse("SelfCallAccepted", "The call got accepted"));
            activeUser.get(user).getBasicRemote().sendText(Utils.buildResponse("RemoteCallAccepted", "The call got accepted"));

        }else{

            initialSession.getBasicRemote().sendText(Utils.buildResponse("SelfCallDeclined", "The call got declined")); 
            activeUser.get(user).getBasicRemote().sendText(Utils.buildResponse("RemoteCallDeclined", "The call got accepted"));
            
        }
    }
}

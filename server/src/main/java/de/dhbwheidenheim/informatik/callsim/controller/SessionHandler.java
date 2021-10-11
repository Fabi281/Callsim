package de.dhbwheidenheim.informatik.callsim.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.FileHandler;

import javax.websocket.Session;


import de.dhbwheidenheim.informatik.callsim.model.User;

public class SessionHandler {

    // Hashmap with a session as key and a combination of user and status
    private final static Map<Session, ArrayList<String>> sessions = new HashMap<Session, ArrayList<String>>();

    /* 
        You could combine the previous Hashmap with this one by using a BidiMap here but i decided on two seperate 
        Hashmaps as we do not need the huge Apache Common Library and do not need to iterate over the entire Map 
        each time we want to check for a Session by Username with this approach.
        This is more a helper variable which promotes efficiency in Time-Complexity (at the cost of Space-Complexity) 
        and negates the necessity of a library.
    */ 
    private final static Map<String, Session> activeUser = new HashMap<String, Session>();

    /*
        As we work in a private Git-Repository we can directly safe the available servers here
        in a bigger/public project with a growing count of servers you should at least save
        them in a seperate txt file outside the repository
    */
    private final static Map<String, String> BBBServer  = new HashMap<String, String>() {{
        put("https://bbb.dhbw-heidenheim.de/?M=xzNW8jI0M2U3Mu862pnC", "available");
        put("https://bbb.dhbw-heidenheim.de/?M=eBtgjI0NDNlMushX31Zj", "available");
        put("https://bbb.dhbw-heidenheim.de/?M=MwIJjI0MmQwMgkoqcKIf", "available");
    }};
    
    private static Logger LOGGER = Logger.getLogger(SessionHandler.class.getName());   

    public static void setLogger(){
        // Set the file and format for the file-logger for connection data
        try{
            SimpleFormatter formatter = new SimpleFormatter();
            FileHandler fh = new FileHandler("./LogFile.log", true);
            fh.setFormatter(formatter);
            LOGGER.addHandler(fh);
        }catch (IOException e){
            e.printStackTrace();
        }
         
    }

    public static void addSession(Session session, String Username){
        // Add the session to both hashmaps
        sessions.put(session, new ArrayList<String>());
        sessions.get(session).add(Username);
        sessions.get(session).add("Online");

        activeUser.put(Username, session);
    }

    public static void deleteSession(Session session){
        // Delete the session in both hashmaps
        activeUser.remove(sessions.get(session).get(0));
        sessions.remove(session);
    }

    public static Map<String, String> userListWithStatus(ArrayList<User> users) throws IOException{
        // Create a map with all users and their statuses (add Offline if none is present)
        Map<String, String> allUserStatuses = new HashMap<String, String>();
        for(ArrayList<String> user : sessions.values()){
            allUserStatuses.put(user.get(0), user.get(1));
        }

        for(User user : users){
            allUserStatuses.putIfAbsent(user.getUsername(), "Offline");
        }
        return allUserStatuses;
    }

    public static boolean checkLogin(String Username){
        // Check if a Person using the Username is already logged in
        if(activeUser.containsKey(Username)){
                return false;
            }
        return true;

    }

    public static void startCall(Session initialSession, String userToCall) throws IOException{

        // Check if a the user which is being called is offline with corresponding response
        if(!activeUser.containsKey(userToCall)){

            LOGGER.info(sessions.get(initialSession).get(0) + " tried to call " + userToCall + " but he/she is offline");
            initialSession.getBasicRemote().sendText(Utils.buildResponse("Offline", userToCall + " is Offline"));
            return;

        }

        // Check if at least one user is already in a call with corresponding response
        if(sessions.get(initialSession).get(1).equals("inCall") || sessions.get(activeUser.get(userToCall)).get(1).equals("inCall")){

            LOGGER.info(sessions.get(initialSession).get(0) + " tried to call " + userToCall + " but someone is already in a call");
            initialSession.getBasicRemote().sendText(Utils.buildResponse("inCall", "You or the other Person ist already in a Call"));
            return;

        }

        // Check if there is a Server available with a check in place if it is not with corresponding response.
        // (The .get()-Method is sensitive to non-existent data as such is not usable in this case)
        var wrapper = new Object(){ String availableServer = ""; };
        BBBServer.entrySet().stream().filter(link -> link.getValue().equals("available")).findFirst().ifPresent(s -> wrapper.availableServer = s.getKey());
        
        if(wrapper.availableServer.equals("")){

            LOGGER.info(sessions.get(initialSession).get(0) + " tried to call " + userToCall + " but there is no unused server");
            initialSession.getBasicRemote().sendText(Utils.buildResponse("CallFailed", "No Server available")); 
            return;

        }

        // Set all users to inCall and the server inUse as to not call/use them again until the current call is finished
        // and send response
        BBBServer.put(wrapper.availableServer, "inUse");

        sessions.get(initialSession).set(1, "inCall");
        sessions.get(activeUser.get(userToCall)).set(1, "inCall");

        activeUser.get(userToCall).getBasicRemote().sendText(Utils.buildResponse("incomingCall", wrapper.availableServer, sessions.get(initialSession).get(0)));
        initialSession.getBasicRemote().sendText(Utils.buildResponse("startedCall", wrapper.availableServer, userToCall)); 

        LOGGER.info(sessions.get(initialSession).get(0) + " started a call with " + userToCall);
    }

    public static void endCall(Session initialSession, String userToReset, String usedServer) throws IOException{

        // Set all users to Online and the server available as the call now ended and called/used again
        BBBServer.put(usedServer, "available");

        sessions.get(initialSession).set(1, "Online");
        sessions.get(activeUser.get(userToReset)).set(1, "Online");  
        
        activeUser.get(userToReset).getBasicRemote().sendText(Utils.buildResponse("RemoteCallEnded", "The other Person ended the Call"));
        initialSession.getBasicRemote().sendText(Utils.buildResponse("SelfCallEnded", "You ended the Call"));
        LOGGER.info("The call between " + sessions.get(initialSession).get(0) + " and " + userToReset
         + " ended and the Server is available again");

    }

    public static void respondToCall(Session initialSession, String response, String user, String usedServer) throws IOException{
        
        if(response.equals("accept")){
            // The action if the call got accpeted
            initialSession.getBasicRemote().sendText(Utils.buildResponse("SelfCallAccepted", usedServer));
            activeUser.get(user).getBasicRemote().sendText(Utils.buildResponse("RemoteCallAccepted", usedServer));
            LOGGER.info(sessions.get(initialSession).get(0) + " accepted the call from " + user);

        }else if(response.equals("selfdecline")){
            // The action if the caller declines before the user which is being called can accept/decline
            endCall(initialSession, user, usedServer);

        }else{
            // The action if the call got declined by the person being called
            initialSession.getBasicRemote().sendText(Utils.buildResponse("SelfCallDeclined", "The call got declined")); 
            activeUser.get(user).getBasicRemote().sendText(Utils.buildResponse("RemoteCallDeclined", "The call got accepted"));
            LOGGER.info(sessions.get(initialSession).get(0) + " declined the call from " + user);
            
        }
    }
}

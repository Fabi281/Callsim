package de.callsim;

import javax.json.Json;
import javax.json.JsonObject;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class NextWindow {
    JPanel rootPanel;
    JButton call;
    JPanel listPanel;
    JPanel userPanel;
    JPanel blueBg;
    JPanel dataContainer;
    JPanel firstNamePanel;
    JPanel actionPanel;
    JPanel whiteBg;
    JLabel firstNameLabelBig;
    JLabel numberOfUsers;
    JPanel callContainer;
    JPanel callProgressPanel;
    JProgressBar progressBar1;
    JPanel namePanelContainer;
    JProgressBar userStateBar;
    JLabel userStatebar2;
    JButton hangUpBtn;
    JPanel contentPanel;
    JPanel statePanel;
    JLabel stateDisplay;
    private JScrollPane ScrollPane;

    public ArrayList<String> log;
    public String selectedUser = null;
    boolean gettingCalled = false;
    boolean callInProgress = false;
    boolean callAccepted = false;
    boolean updating = false;

    HashMap<String, String> userData = new HashMap<>();
    HashMap<String, String> stateColors = new HashMap<String, String>() {{
        put("\"Online\"", "0,166,0;This user is online and available");
        put("\"inCall\"", "249,179,96;This user is online but currently in another call");
        put("\"Offline\"", "111,3,30;This user is not online");

    }};

    /* ref: https://www.baeldung.com/java-initialize-hashmap#the-static-initializer-for-a-static-hashmap*/

    public NextWindow() {
        log = new ArrayList<String>();
        log(client.clientUsername + " logged in");
        callContainer.setVisible(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(
                new TimerTask(){
                    @Override
                    public void run(){
                        sentListRequest();
                    }
                }, 0, 5000
        );

        call.addActionListener(e -> {
            if(!callInProgress && !gettingCalled) startACall(selectedUser);
        });
        hangUpBtn.addActionListener(e -> cancelCall());
    }

    public void sentListRequest(){
        updating = true;
        JsonObject json = Json.createObjectBuilder()
                .add("action", "UserStatuses")
                .build();
        // send UserStatuses message to websocket
        client.clientEndPoint.sendMessage(json);
    }

    public void getListResponse(HashMap<String, String> userData){
        this.userData = userData;
        for(Component comp : listPanel.getComponents()){
            listPanel.remove(comp);
        }
        listPanel.setLayout(new GridLayout(0,1));
        listPanel.setPreferredSize(new Dimension(150,userData.size()*50));

        for (String key : userData.keySet()){
            UserItem item = new UserItem(key);
            item.getUserBtn().addActionListener(e -> {
                if (!callInProgress && !gettingCalled && !updating){
                    selectedUser = key;
                    updateCallDisplay();
                    updateBigDisplay(selectedUser);
                }
            });
            listPanel.add(item);

        }
        listPanel.add(numberOfUsers);
        numberOfUsers.setText(userData.size() + " user" + (userData.size() != 1 ? "s" : "") + " registered");
        if (selectedUser != null) updateBigDisplay(selectedUser);
        updating = false;
        updateCallDisplay();
        listPanel.revalidate();
        ScrollPane.getVerticalScrollBar().setUnitIncrement(5);
    }

    public void startACall(String targetUser) {
        sentListRequest();
        if(!callInProgress && !gettingCalled) client.sendStartCallMessage(targetUser);
    }

    public void cancelCall() {

        log("Cancelling call...");
        setCallDisplay(false);
        client.sendRespondSelfDeclineMessage();
        log("Call has been successfully cancelled");
    }

    public void updateBigDisplay(String newUsername) {
        if (userData == null) return;
        String nameOfUser = newUsername;
        String stateOfUser = stateColors.get(userData.get(newUsername));

        String color = stateOfUser.split(";")[0];
        int colorR = Integer.parseInt(color.split(",")[0]),
                colorG = Integer.parseInt(color.split(",")[1]),
                colorB = Integer.parseInt(color.split(",")[2]);

        String stateTooltip = stateOfUser.split(";")[1];

        firstNameLabelBig.setText(nameOfUser);
        userStatebar2.setForeground(new Color(colorR, colorG, colorB));
        userStatebar2.setToolTipText(stateTooltip);
    }

    public void log(String msg) {
        log.add(0, msg); /* always add to beginning */
        stateDisplay.setText(msg);
    }

    public void setCallDisplay(boolean vis) {
        callInProgress = vis;
        updateCallDisplay();
    }

    public void updateCallDisplay() {
        callContainer.setVisible(!callInProgress && selectedUser != null);
        hangUpBtn.setVisible(!callAccepted);
        callProgressPanel.setVisible(callInProgress);
        listPanel.setEnabled(!callInProgress);
    }
}
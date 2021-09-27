package de.callsim;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Locale;
import java.util.HashMap;

public class NextWindow {
    JPanel rootPanel;
    JLabel name1;
    JLabel surname1;
    JButton call;
    JPanel listPanel;
    JButton callUserDirectlyBtn;
    JPanel userPanel;
    JPanel listUserTemplate;
    JPanel blueBg;
    JPanel dataContainer;
    JPanel firstNamePanel;
    JPanel surnamePanel;
    JPanel actionPanel;
    JPanel furtherInfoPanel;
    JPanel whiteBg;
    JLabel usernameLabelBig;
    JLabel usernamePrefix;
    JLabel firstNameLabelBig;
    JLabel surnameLabelBig;
    JPanel namePanel;
    JButton userBtn1;
    JButton userBtn2;
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
    int currentUserId = -1;
    String userNamePrefix = "@";

    ArrayList<String> log;
    final int maxLogs = 10;

    boolean callInProgress = false;

    HashMap<String, String> userData = new HashMap<>();

    HashMap<String, String> stateColors = new HashMap<String, String>() {{
        put("online", "0,166,0;This user is online and available");
        put("busy", "249,179,96;This user is online but currently in another call");
        put("offline", "111,3,30;This user is not online");

    }};

    /* ref: https://www.baeldung.com/java-initialize-hashmap#the-static-initializer-for-a-static-hashmap*/


    // no idea (yet) how dynamic rendering can be realized

    public NextWindow() {
        userData.put("der-bernd", "Mayinger,Bernd,online");
        userData.put("theoneandonly", "Assfalg,Rolf,busy");
        numberOfUsers.setText(userData.size() + " user" + (userData.size() != 1 ? "s" : "") + " registered");
        updateCallDisplay();

        log = new ArrayList<String>();

        call.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // code here will be executed after clicking on CALL

                /*JOptionPane.showMessageDialog(null, "lorem");*/

                String user = "der-bernd"; /* propably better way: giving userId as param */
                startACall(user);
            }
        });
        callUserDirectlyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = "der-bernd"; /* propably better way: giving userId as param */
                startACall(user);
            }
        });
        namePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String targetUser = "theoneandonly";
                updateBigDisplay(targetUser);
            }
        });
        userBtn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String targetUser = "theoneandonly";
                updateBigDisplay(targetUser);
            }
        });
        userBtn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String targetUser = "der-bernd";
                updateBigDisplay(targetUser);
            }
        });
        hangUpBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelCall();
            }
        });
    }

    public void updateBigDisplay(String newUsername) {
        if (callInProgress) return; /* if a call is in progress, abort */
        String dataOfUser = userData.get(newUsername);
        if (dataOfUser == null) return;

        String firstNameOfUser = dataOfUser.split(",")[1];
        String surnameOfUser = dataOfUser.split(",")[0];
        String stateOfUser = stateColors.get(dataOfUser.split(",")[2]);

        String color = stateOfUser.split(";")[0];
        int colorR = Integer.parseInt(color.split(",")[0]),
                colorG = Integer.parseInt(color.split(",")[1]),
                colorB = Integer.parseInt(color.split(",")[2]);


        String stateTooltip = stateOfUser.split(";")[1];


        firstNameLabelBig.setText(firstNameOfUser);
        surnameLabelBig.setText(surnameOfUser);
        usernameLabelBig.setText(usernamePrefix.getText() + newUsername);
        userStatebar2.setForeground(new Color(colorR, colorG, colorB));
        userStatebar2.setToolTipText(stateTooltip);
    }

    public void startACall(String targetUser) {
        log("Starting new call...");
        setCallDisplay(true);

        /* request logic here */
        boolean browserOpen = true; /* simulating establishment of connection */
        if (browserOpen) log("Connection established");
    }

    public void cancelCall() {
        log("Cancelling call...");
        /* request logic here */

        setCallDisplay(false);
        log("Call has been successfully cancelled");
    }

    private void log(String msg) {
        log.add(0, msg); /* always add to beginning */
        if(log.size() > maxLogs){
            /* here you could reduce the list so it doensn't mess up the storage */
        }

        stateDisplay.setText(msg);
    }


    public void triggerCallDisplay() {
        callInProgress = !callInProgress;
        updateCallDisplay();
    }

    public void setCallDisplay(boolean vis) {
        callInProgress = vis;
        updateCallDisplay();
    }

    private void updateCallDisplay() {
        callContainer.setVisible(!callInProgress);
        callProgressPanel.setVisible(callInProgress);

        listPanel.setEnabled(!callInProgress); /* when call in progress, disable the sidebar */
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new BorderLayout(0, 0));
        rootPanel.setBackground(new Color(-1));
        rootPanel.setMaximumSize(new Dimension(600, 1000));
        rootPanel.setMinimumSize(new Dimension(550, 500));
        rootPanel.setOpaque(true);
        rootPanel.setPreferredSize(new Dimension(650, 600));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setBackground(new Color(-16446877));
        rootPanel.add(scrollPane1, BorderLayout.WEST);
        listPanel = new JPanel();
        listPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 1, new Insets(0, 10, 0, 5), -1, -1));
        listPanel.setAutoscrolls(false);
        listPanel.setBackground(new Color(-16775320));
        listPanel.setEnabled(true);
        scrollPane1.setViewportView(listPanel);
        listUserTemplate = new JPanel();
        listUserTemplate.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        listUserTemplate.setBackground(new Color(-16775320));
        listPanel.add(listUserTemplate, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        namePanel = new JPanel();
        namePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        namePanel.setBackground(new Color(-16775320));
        listUserTemplate.add(namePanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        name1 = new JLabel();
        Font name1Font = this.$$$getFont$$$("Arial Nova Light", Font.PLAIN, 16, name1.getFont());
        if (name1Font != null) name1.setFont(name1Font);
        name1.setForeground(new Color(-1));
        name1.setText("Rolf");
        name1.setVisible(false);
        namePanel.add(name1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        surname1 = new JLabel();
        Font surname1Font = this.$$$getFont$$$("Arial Nova Light", Font.PLAIN, 16, surname1.getFont());
        if (surname1Font != null) surname1.setFont(surname1Font);
        surname1.setForeground(new Color(-1));
        surname1.setText("Assfalg");
        surname1.setVisible(false);
        namePanel.add(surname1, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        userBtn1 = new JButton();
        userBtn1.setBackground(new Color(-16775320));
        Font userBtn1Font = this.$$$getFont$$$("Arial Nova Light", -1, 16, userBtn1.getFont());
        if (userBtn1Font != null) userBtn1.setFont(userBtn1Font);
        userBtn1.setForeground(new Color(-1));
        userBtn1.setText("Rolf Assfalg");
        namePanel.add(userBtn1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        callUserDirectlyBtn = new JButton();
        callUserDirectlyBtn.setActionCommand("");
        callUserDirectlyBtn.setBackground(new Color(-16775320));
        callUserDirectlyBtn.setBorderPainted(false);
        Font callUserDirectlyBtnFont = this.$$$getFont$$$("Arial Nova Cond Light", -1, 22, callUserDirectlyBtn.getFont());
        if (callUserDirectlyBtnFont != null) callUserDirectlyBtn.setFont(callUserDirectlyBtnFont);
        callUserDirectlyBtn.setForeground(new Color(-1));
        callUserDirectlyBtn.setHorizontalAlignment(4);
        callUserDirectlyBtn.setLabel("\uD83D\uDCDE");
        callUserDirectlyBtn.setText("\uD83D\uDCDE");
        listUserTemplate.add(callUserDirectlyBtn, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("theoneandonly");
        label1.setVisible(false);
        listUserTemplate.add(label1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setBackground(new Color(-16775320));
        listPanel.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel2.setBackground(new Color(-16775320));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$("Arial Nova Light", Font.PLAIN, 16, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setForeground(new Color(-1));
        label2.setText("Bernd");
        label2.setVisible(false);
        panel2.add(label2, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$("Arial Nova Light", Font.PLAIN, 16, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setForeground(new Color(-1));
        label3.setText("Mayinger");
        label3.setVisible(false);
        panel2.add(label3, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        userBtn2 = new JButton();
        userBtn2.setBackground(new Color(-16775320));
        Font userBtn2Font = this.$$$getFont$$$("Arial Nova Light", -1, 16, userBtn2.getFont());
        if (userBtn2Font != null) userBtn2.setFont(userBtn2Font);
        userBtn2.setForeground(new Color(-1));
        userBtn2.setText("Bernd Mayinger");
        panel2.add(userBtn2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JButton button1 = new JButton();
        button1.setActionCommand("");
        button1.setBackground(new Color(-16775320));
        button1.setBorderPainted(false);
        Font button1Font = this.$$$getFont$$$("Arial Nova Cond Light", -1, 22, button1.getFont());
        if (button1Font != null) button1.setFont(button1Font);
        button1.setForeground(new Color(-1));
        button1.setHorizontalAlignment(4);
        button1.setLabel("\uD83D\uDCDE");
        button1.setText("\uD83D\uDCDE");
        panel1.add(button1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("der-bernd");
        label4.setVisible(false);
        panel1.add(label4, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        listPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        numberOfUsers = new JLabel();
        numberOfUsers.setBackground(new Color(-16775320));
        Font numberOfUsersFont = this.$$$getFont$$$("Arial Nova Light", -1, 12, numberOfUsers.getFont());
        if (numberOfUsersFont != null) numberOfUsers.setFont(numberOfUsersFont);
        numberOfUsers.setForeground(new Color(-1));
        numberOfUsers.setText("12 users registered");
        listPanel.add(numberOfUsers, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        userPanel = new JPanel();
        userPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        userPanel.setBackground(new Color(-1));
        userPanel.setForeground(new Color(-16775320));
        userPanel.setVisible(true);
        rootPanel.add(userPanel, BorderLayout.CENTER);
        contentPanel = new JPanel();
        contentPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(50, 45, 0, 20), -1, -1));
        contentPanel.setBackground(new Color(-1));
        contentPanel.setForeground(new Color(-14408668));
        contentPanel.setVisible(true);
        userPanel.add(contentPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        blueBg = new JPanel();
        blueBg.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(10, 5, 5, 0), -1, -1));
        blueBg.setBackground(new Color(-1314561));
        contentPanel.add(blueBg, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 1, false));
        whiteBg = new JPanel();
        whiteBg.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        whiteBg.setBackground(new Color(-1));
        blueBg.add(whiteBg, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        dataContainer = new JPanel();
        dataContainer.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 3, new Insets(20, 50, 30, 0), -1, -1));
        dataContainer.setBackground(new Color(-1));
        whiteBg.add(dataContainer, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        actionPanel = new JPanel();
        actionPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        actionPanel.setBackground(new Color(-1));
        dataContainer.add(actionPanel, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        callContainer = new JPanel();
        callContainer.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        actionPanel.add(callContainer, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        call = new JButton();
        call.setBackground(new Color(-1));
        Font callFont = this.$$$getFont$$$("Arial Nova Light", -1, 26, call.getFont());
        if (callFont != null) call.setFont(callFont);
        call.setForeground(new Color(-16056233));
        call.setHorizontalTextPosition(0);
        call.setLabel("\uD83D\uDCDE");
        call.setText("\uD83D\uDCDE");
        callContainer.add(call, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        callProgressPanel = new JPanel();
        callProgressPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        callProgressPanel.setBackground(new Color(-1));
        actionPanel.add(callProgressPanel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.setBackground(new Color(-1));
        callProgressPanel.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        progressBar1 = new JProgressBar();
        progressBar1.setForeground(new Color(-16775320));
        progressBar1.setIndeterminate(true);
        panel3.add(progressBar1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$("Arial Nova Light", -1, 16, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setHorizontalAlignment(0);
        label5.setHorizontalTextPosition(2);
        label5.setText("CallSim tries to start the call");
        panel3.add(label5, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        hangUpBtn = new JButton();
        hangUpBtn.setBackground(new Color(-1));
        Font hangUpBtnFont = this.$$$getFont$$$("Arial Nova Light", -1, 26, hangUpBtn.getFont());
        if (hangUpBtnFont != null) hangUpBtn.setFont(hangUpBtnFont);
        hangUpBtn.setForeground(new Color(-65536));
        hangUpBtn.setText("\uD83D\uDD7D");
        hangUpBtn.setToolTipText("Hang up");
        callProgressPanel.add(hangUpBtn, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        furtherInfoPanel = new JPanel();
        furtherInfoPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        furtherInfoPanel.setAutoscrolls(false);
        furtherInfoPanel.setBackground(new Color(-1));
        dataContainer.add(furtherInfoPanel, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        usernamePrefix = new JLabel();
        Font usernamePrefixFont = this.$$$getFont$$$("Arial Nova Light", Font.ITALIC, 16, usernamePrefix.getFont());
        if (usernamePrefixFont != null) usernamePrefix.setFont(usernamePrefixFont);
        usernamePrefix.setForeground(new Color(-12763843));
        usernamePrefix.setText("@");
        usernamePrefix.setVisible(false);
        furtherInfoPanel.add(usernamePrefix, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        usernameLabelBig = new JLabel();
        Font usernameLabelBigFont = this.$$$getFont$$$("Arial Nova Light", Font.ITALIC, 16, usernameLabelBig.getFont());
        if (usernameLabelBigFont != null) usernameLabelBig.setFont(usernameLabelBigFont);
        usernameLabelBig.setText("@theoneandonly");
        furtherInfoPanel.add(usernameLabelBig, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        dataContainer.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        namePanelContainer = new JPanel();
        namePanelContainer.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        namePanelContainer.setBackground(new Color(-1));
        dataContainer.add(namePanelContainer, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        firstNamePanel = new JPanel();
        firstNamePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        firstNamePanel.setBackground(new Color(-1));
        namePanelContainer.add(firstNamePanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        firstNameLabelBig = new JLabel();
        Font firstNameLabelBigFont = this.$$$getFont$$$("Arial Nova Light", -1, 36, firstNameLabelBig.getFont());
        if (firstNameLabelBigFont != null) firstNameLabelBig.setFont(firstNameLabelBigFont);
        firstNameLabelBig.setForeground(new Color(-13224394));
        firstNameLabelBig.setText("Rolf");
        firstNamePanel.add(firstNameLabelBig, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        firstNamePanel.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        surnamePanel = new JPanel();
        surnamePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        surnamePanel.setBackground(new Color(-1));
        namePanelContainer.add(surnamePanel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        surnameLabelBig = new JLabel();
        Font surnameLabelBigFont = this.$$$getFont$$$("Arial Nova Light", Font.BOLD, 36, surnameLabelBig.getFont());
        if (surnameLabelBigFont != null) surnameLabelBig.setFont(surnameLabelBigFont);
        surnameLabelBig.setText("Assfalg");
        surnamePanel.add(surnameLabelBig, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        userStateBar = new JProgressBar();
        userStateBar.setBackground(new Color(-1));
        userStateBar.setForeground(new Color(-9501922));
        userStateBar.setOrientation(1);
        userStateBar.setStringPainted(false);
        userStateBar.setToolTipText("This user is currently online");
        userStateBar.setValue(100);
        userStateBar.setVisible(false);
        dataContainer.add(userStateBar, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        userStatebar2 = new JLabel();
        userStatebar2.setAlignmentY(0.0f);
        userStatebar2.setBackground(new Color(-1));
        Font userStatebar2Font = this.$$$getFont$$$("Arial Nova Cond Light", -1, 72, userStatebar2.getFont());
        if (userStatebar2Font != null) userStatebar2.setFont(userStatebar2Font);
        userStatebar2.setText("|");
        dataContainer.add(userStatebar2, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTHWEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        statePanel = new JPanel();
        statePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        statePanel.setBackground(new Color(-1));
        userPanel.add(statePanel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        stateDisplay = new JLabel();
        stateDisplay.setText("this is the first state");
        statePanel.add(stateDisplay, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
package de.callsim;

import javax.json.Json;
import javax.json.JsonObject;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Locale;

public class Login {
    JTextField usernameField;
    JPanel loginPanel;
    JPasswordField passwordField;
    JButton loginBtn;
    JButton toRegister;
    JTabbedPane tabpane;
    JPanel root_panel;
    JButton toLogin;
    JPanel registerPanel;
    JPanel passPanel;
    JPanel titlePanel;
    JPanel actionPanel;
    JPanel userPanel;
    JPanel passContainer;
    JButton registerBtn;
    JPanel nameContainer;
    JPanel usernamePanel;
    private JTextField usernameRegField;
    private JPasswordField passwordRegField;
    private JPasswordField passwordRegConField;
    Boolean isLogin = true;

    public Login() {
        registerPanel.setVisible(false); /* set this to false on program start, just to be sure */
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                triggerLogin();
            }
        });
        toRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleLoginView();
            }
        });

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                keyHandler(e);
            }
        });
        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                keyHandler(e);
            }
        });
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                triggerRegister();
            }
        });
        toLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleLoginView();
            }
        });
    }

    /* this method will just switch between loginBtn and registering panel */
    public void toggleLoginView() {

        if (isLogin) {
            registerPanel.setVisible(true);
            loginPanel.setVisible(false);
        } else {
            loginPanel.setVisible(true);
            registerPanel.setVisible(false);
        }
        isLogin = !isLogin;
    }

    public void keyHandler(KeyEvent e) { // better way of handling, but still not the best solution: has to be applied to each new field of form
        int keycode = (int) e.getKeyChar(); // ref: https://stackoverflow.com/a/16939321, will also handle ENTER: ENTER's char will be empty when you print it because it is a newline.
        switch (keycode) {
            case 10: // ENTER
                triggerLogin();
            default:
                // if key not handled, please do NOT trigger popups, that will lead to horrible UX
        }
    }

    public void triggerLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        JsonObject value = Json.createObjectBuilder()
                .add("action", "login")
                .add("Username", username)
                .add("Password", password)
                .build();
        // send LOGIN message to websocket
        client.clientEndPoint.sendMessage(value);
    }

    public void triggerRegister() {
        String username = usernameRegField.getText();
        String password = passwordRegField.getText();
        String passwordCon = passwordRegConField.getText();
        if (password.equals(passwordCon)) {
            JsonObject value = Json.createObjectBuilder()
                    .add("action", "register")
                    .add("Username", username)
                    .add("Password", password)
                    .build();
            // send LOGIN message to websocket
            client.clientEndPoint.sendMessage(value);
        }

    }

    public void popupMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

}

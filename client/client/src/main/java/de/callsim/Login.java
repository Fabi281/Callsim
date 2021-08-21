package de.callsim;

import javax.json.Json;
import javax.json.JsonObject;
import javax.swing.*;
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
    JPanel panel1;
    JPasswordField passwordField;
    JButton login;
    JLabel usernameLabel;
    JLabel passLabel;
    JButton registerButton;
    JTabbedPane tabpane;

    public Login() {
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                triggerLogin();
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = tabpane.getSelectedIndex();
                JOptionPane.showMessageDialog(null, idx);
                if (idx == 0) {
                    tabpane.setSelectedIndex(1); // according to https://stackoverflow.com/a/4157492, this should actually work
                } else {
                    tabpane.setSelectedIndex(0);
                }
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

    public void popupMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }


}

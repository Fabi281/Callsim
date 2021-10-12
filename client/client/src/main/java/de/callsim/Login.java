package de.callsim;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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
        loginBtn.addActionListener(e -> triggerLogin());
        toRegister.addActionListener(e -> toggleLoginView());

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
        registerBtn.addActionListener(e -> triggerRegister());
        toLogin.addActionListener(e -> toggleLoginView());
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
    public void triggerLogin(){
        client.sendLoginMessage(usernameField.getText(), passwordField.getText());
    }
    public void triggerRegister() {
        client.sendRegisterMessage(usernameRegField.getText(), passwordRegField.getText(), passwordRegConField.getText());
    }

}

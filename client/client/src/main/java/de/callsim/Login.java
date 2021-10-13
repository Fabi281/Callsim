package de.callsim;


import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
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
        registerPanel.setVisible(false); // Set this to false on program start. Login Page should be shown first
        loginBtn.addActionListener(e -> triggerLogin()); // On clicking Login Button, trigger a login action
        toRegister.addActionListener(e -> toggleLoginView()); // Show the register Page

        // On pressing Enter whilst in Username or Password, trigger the Login event
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
        registerBtn.addActionListener(e -> triggerRegister()); // On clicking Register Button, trigger a register action
        toLogin.addActionListener(e -> toggleLoginView()); // Show the Login Page
    }

    // This method will just switch between login and register panel
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

    public void keyHandler(KeyEvent e) {
        // ref: https://stackoverflow.com/a/16939321
        int keycode = (int) e.getKeyChar();
        switch (keycode) {
            case 10: // ENTER
                triggerLogin();
            default:
                // If key not handled, please do NOT trigger popups, that will lead to horrible UX
        }
    }
    // Trigger Register Action to send a Message to the Websocket
    public void triggerRegister() {
        /*
        We used getText() on the passwordFields instead of getPassword() because of 2 reasons:
        1.) The Server needs a String and getPassword() returns a char[]
        2.) Upon parsing char[] into a String via the toString() method, those two Fields return different values even when given the same text.
            This makes comparing and saving the password extremely difficult.
         */
        client.sendRegisterMessage(usernameRegField.getText(), passwordRegField.getText(), passwordRegConField.getText());
    }
    
    // Trigger Login Action to send a Message to the Websocket
    public void triggerLogin(){
        client.sendLoginMessage(usernameField.getText(), passwordField.getText());
    }

}

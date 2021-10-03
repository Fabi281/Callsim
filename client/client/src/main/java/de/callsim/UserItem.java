package de.callsim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserItem extends JPanel {

    private JButton userBtn;
    private JButton callBtn;
    private String name;

    public UserItem(String name){
        this.name = name;
        userBtn = new JButton(this.name);
        userBtn.setOpaque(false);
        userBtn.setContentAreaFilled(false);
        userBtn.setBorderPainted(false);
        userBtn.setForeground(Color.white);

        callBtn = new JButton("\uD83D\uDCDE");
        callBtn.setOpaque(false);
        callBtn.setContentAreaFilled(false);
        callBtn.setBorderPainted(false);
        callBtn.setForeground(Color.white);

        this.setOpaque(false);
        this.setLayout(new GridLayout(1, 2));
        this.add(userBtn);
        this.add(callBtn);
    }

    public JButton getUserBtn(){
        return userBtn;
    }

    public JButton getCallBtn(){
        return callBtn;
    }

    public String getName(){
        return name;
    }
}

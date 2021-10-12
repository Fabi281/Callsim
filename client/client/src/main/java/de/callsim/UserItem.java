package de.callsim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserItem extends JPanel {

    private JButton userBtn;
    private String name;

    public UserItem(String name){
        this.name = name;
        userBtn = new JButton(this.name);
        userBtn.setHorizontalAlignment(SwingConstants.LEFT);
        userBtn.setOpaque(false);
        userBtn.setContentAreaFilled(false);
        userBtn.setBorderPainted(false);
        userBtn.setForeground(Color.white);

        this.setOpaque(false);
        this.setLayout(new GridLayout(1, 2));
        this.add(userBtn);
    }

    public JButton getUserBtn(){
        return userBtn;
    }

    public String getName(){
        return name;
    }
}

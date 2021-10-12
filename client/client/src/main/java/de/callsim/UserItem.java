package de.callsim;

import javax.swing.*;
import java.awt.*;

public class UserItem extends JPanel {

    private JButton userBtn;
    private String name;

    public UserItem(String name){
        this.name = name;
        userBtn = new JButton(this.name);
        userBtn.setOpaque(false);
        userBtn.setContentAreaFilled(false);
        userBtn.setBorderPainted(false);
        userBtn.setForeground(Color.white);
        this.setOpaque(false);
        this.setLayout(new GridLayout(1, 0));
        this.add(userBtn);
    }

    public JButton getUserBtn(){
        return userBtn;
    }

    public String getName(){
        return name;
    }
}

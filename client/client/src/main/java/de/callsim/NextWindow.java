package de.callsim;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

public class NextWindow {
    JPanel panel1;
    JLabel name1;
    JLabel surname1;
    JButton call;
    JPanel one;
    JLabel name2;
    JLabel surname2;
    JButton button1;

    // no idea (yet) how dynamic rendering can be realized

    public NextWindow() {
        call.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // code here will be executed after clicking on CALL

                JOptionPane.showMessageDialog(null, "lorem");
            }
        });
    }

}
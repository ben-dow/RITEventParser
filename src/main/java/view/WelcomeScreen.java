package view;

import controller.ApplicationController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeScreen extends JPanel {

    JLabel welcomeLabel;
    JButton startButton;

    ApplicationController applicationController;

    WelcomeScreen(ApplicationController applicationController){
        this.applicationController = applicationController;

        SpringLayout layout = new SpringLayout();
        setLayout(layout);

        welcomeLabel = new JLabel("RIT Events Confirmation Emails to Google Calendar Event");
        add(welcomeLabel);

        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
               applicationController.advanceWindows();
            }
        });
        add(startButton);


        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, welcomeLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, startButton, 0, SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, startButton, 0, SpringLayout.VERTICAL_CENTER, this);



    }
}

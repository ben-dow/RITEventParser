package view;

import controller.ApplicationController;
import controller.GoogleController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class GoogleAuthentication extends JPanel {

    JLabel welcomeLabel;
    JButton startButton;

    // Controllers
    ApplicationController applicationController;
    GoogleController googleController;

    GoogleAuthentication(ApplicationController applicationController){
        this.applicationController = applicationController;
        this.googleController = applicationController.getGoogleController();

        SpringLayout layout = new SpringLayout();
        setLayout(layout);

        welcomeLabel = new JLabel("Authenticate with Google");
        add(welcomeLabel);

        startButton = new JButton("Click to Authenticate With Google");
        add(startButton);

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    googleController.authenticateUser();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (GeneralSecurityException e1) {
                    e1.printStackTrace();
                }
            }
        });


        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, welcomeLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, startButton, 0, SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, startButton, 0, SpringLayout.VERTICAL_CENTER, this);



    }
}

package view;

import javax.swing.*;

public class CreationComplete extends JPanel {

    CreationComplete(){

        SpringLayout layout = new SpringLayout();
        setLayout(layout);

        JLabel welcomeLabel = new JLabel("Success!");
        add(welcomeLabel);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, welcomeLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, welcomeLabel, 0, SpringLayout.VERTICAL_CENTER, this);

    }
}

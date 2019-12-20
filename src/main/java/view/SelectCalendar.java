package view;

import controller.ApplicationController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.HashMap;

public class SelectCalendar extends JPanel implements PropertyChangeListener {

    private JLabel welcomeLabel;
    private JComboBox calendars;
    private JButton previous;
    private JButton submitButton;
    private JButton loadCalendars;

    ApplicationController applicationController;

    SelectCalendar(ApplicationController applicationController){
        this.applicationController = applicationController;

        this.applicationController.getCreationConfiguration().addPropertyChangeListener(this);

        SpringLayout layout = new SpringLayout();
        setLayout(layout);

        welcomeLabel = new JLabel("Select Desired Calendar to Create Events in");
        add(welcomeLabel);

        JPanel calendarControls = new JPanel();
        calendarControls.setLayout(new BoxLayout(calendarControls, BoxLayout.X_AXIS));

        calendars = new JComboBox(this.applicationController.getCreationConfiguration().getCalendars().keySet().toArray());

        calendars.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                String selectedCalendar = (String)cb.getSelectedItem();
                applicationController.getCreationConfiguration().setSelectedCalendar(selectedCalendar);
            }
        });
        calendarControls.add(calendars);

        loadCalendars = new JButton("Load Calendars");
        loadCalendars.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    applicationController.getGoogleController().fetchCalendars();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        calendarControls.add(loadCalendars);

        add(calendarControls);

        JPanel controlButtons = new JPanel();
        controlButtons.setLayout(new BoxLayout(controlButtons, BoxLayout.X_AXIS));

        previous = new JButton("Back");
        previous.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                applicationController.previousWindow();
            }
        });
        controlButtons.add(previous);

        submitButton = new JButton("Verify Configuration");
        submitButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                applicationController.advanceWindows();
            }
        });
        controlButtons.add(submitButton);

        add(controlButtons);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, welcomeLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, calendarControls, 0, SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, calendarControls, 0, SpringLayout.VERTICAL_CENTER, this);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, controlButtons, 0, SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.SOUTH, controlButtons, 0, SpringLayout.SOUTH, this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("calendars")){
            calendars.removeAllItems();
            for(String s : ((HashMap<String,String>) evt.getNewValue()).keySet()){
                calendars.addItem(s);
            }
            calendars.updateUI();
        }
    }
}

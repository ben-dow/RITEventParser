package view;

import controller.ApplicationController;
import model.CalendarEvent;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class CreatingEvents extends JPanel implements PropertyChangeListener {

    JLabel statusLabel;

    ApplicationController applicationController;
    CreatingEvents(ApplicationController applicationController){
        this.applicationController = applicationController;
        applicationController.getCreationConfiguration().addPropertyChangeListener(this);
        SpringLayout layout = new SpringLayout();
        setLayout(layout);

        statusLabel = new JLabel("Creating Events");
        add(statusLabel);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, statusLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, statusLabel, 0, SpringLayout.VERTICAL_CENTER, this);

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("successfulCreation")){
            statusLabel.setText("Creating Events " + (((List<CalendarEvent>) evt.getNewValue()).size() + applicationController.getCreationConfiguration().getFailedCreation().size()) + "/" + applicationController.getCreationConfiguration().getSelectedEvents().size());
            statusLabel.updateUI();
        }

        if(evt.getPropertyName().equals("failedCreation")){
            statusLabel.setText("Creating Events " + (((List<CalendarEvent>) evt.getNewValue()).size() + applicationController.getCreationConfiguration().getSuccessfulCreation().size()) + "/" + applicationController.getCreationConfiguration().getSelectedEvents().size());
            statusLabel.updateUI();
        }

        if("eventsCreated".equals(evt.getPropertyName()) && (boolean) evt.getNewValue()){
            applicationController.advanceWindows();
        }
    }
}

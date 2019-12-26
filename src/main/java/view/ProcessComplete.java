package view;

import controller.ApplicationController;
import model.CalendarEvent;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class ProcessComplete extends JPanel implements PropertyChangeListener {
    ApplicationController applicationController;

    JList successfulEvents;
    JList failureEvents;

    JLabel endText;

    public ProcessComplete(ApplicationController applicationController){
        this.applicationController = applicationController;
        applicationController.getCreationConfiguration().addPropertyChangeListener(this);

        SpringLayout layout = new SpringLayout();
        setLayout(layout);

        endText = new JLabel("Creation Results");
        add(endText);

        JPanel success = new JPanel();
        success.setLayout(new BoxLayout(success, BoxLayout.Y_AXIS));

        successfulEvents = new JList(applicationController.getCreationConfiguration().getSuccessfulCreation().toArray());
        JScrollPane successfulEventsScroller = new JScrollPane(successfulEvents);
        successfulEventsScroller.setPreferredSize(new Dimension(400, 200));
        success.add(successfulEventsScroller);

        JLabel successListName = new JLabel("Successes");
        success.add(successListName);

        add(success);


        JPanel collisions = new JPanel();
        collisions.setLayout(new BoxLayout(collisions, BoxLayout.Y_AXIS));


        JPanel failures = new JPanel();
        failures.setLayout(new BoxLayout(failures, BoxLayout.Y_AXIS));

        failureEvents = new JList(applicationController.getCreationConfiguration().getSuccessfulCreation().toArray());
        JScrollPane failureEventsScroller = new JScrollPane(failureEvents);
        failureEventsScroller.setPreferredSize(new Dimension(400, 200));
        failures.add(failureEventsScroller);

        JLabel failureListName = new JLabel("Failures");
        failures.add(failureListName);

        add(failures);


        layout.putConstraint(SpringLayout.NORTH, endText, 0, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, endText, 0, SpringLayout.HORIZONTAL_CENTER, this);

        layout.putConstraint(SpringLayout.WEST, success, 0, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, success, 0, SpringLayout.VERTICAL_CENTER, this);

        layout.putConstraint(SpringLayout.EAST, failures, 0, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, failures, 0, SpringLayout.VERTICAL_CENTER, this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if("successfulCreation".equals(evt.getPropertyName())){
            successfulEvents.setListData(((List<CalendarEvent>)evt.getNewValue()).toArray());
            successfulEvents.updateUI();
        }
        if("failedCreation".equals(evt.getPropertyName())){
            failureEvents.setListData(((List<CalendarEvent>)evt.getNewValue()).toArray());
            failureEvents.updateUI();
        }

    }
}

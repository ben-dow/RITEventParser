package view;

import controller.ApplicationController;
import controller.GoogleController;
import model.CalendarEvent;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class VerifyConfiguration extends JPanel implements PropertyChangeListener {

    private JLabel pageDescription;
    private JList selectedEvents;
    private JTextArea eventDescription;
    private JLabel calendarName;
    private JButton submitButton;


    private ApplicationController applicationController;
    private GoogleController googleController;


    VerifyConfiguration(ApplicationController applicationController){
        this.applicationController = applicationController;
        this.googleController = applicationController.getGoogleController();

        this.applicationController.getCreationConfiguration().addPropertyChangeListener(this);


        SpringLayout layout = new SpringLayout();
        setLayout(layout);

        pageDescription = new JLabel("Please verify what will be created: ");
        add(pageDescription);

        /* Top Components */
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        JLabel calendarNameLabel = new JLabel("Calendar Name: ");
        top.add(calendarNameLabel);

        calendarName = new JLabel(this.applicationController.getCreationConfiguration().getSelectedCalendar());
        top.add(calendarName);

        add(top);

        /* Center Components */
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.X_AXIS));

        selectedEvents = new JList(applicationController.getCreationConfiguration().getSelectedEvents().toArray());
        JScrollPane selectedEventsScroller = new JScrollPane(selectedEvents);
        selectedEventsScroller.setPreferredSize(new Dimension(300, 200));
        center.add(selectedEventsScroller);

        selectedEvents.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(selectedEvents.getSelectedValue() != null){
                    eventDescription.setText(((CalendarEvent)selectedEvents.getSelectedValue()).toDetailedString());
                }
            }
        });

        eventDescription = new JTextArea();
        eventDescription.setEditable(false);
        center.add(eventDescription);

        add(center);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                submitButton.setText("Working");
                googleController.createCalendarEvents();

            }
        });
        add(submitButton);


        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, top, 0, SpringLayout.HORIZONTAL_CENTER, this);


        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, center, 0, SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, center, 0, SpringLayout.VERTICAL_CENTER, this);

        layout.putConstraint(SpringLayout.SOUTH, submitButton, 0, SpringLayout.SOUTH, this);
        layout.putConstraint(SpringLayout.SOUTH, submitButton, 0, SpringLayout.SOUTH, this);

    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if("selectedEvents".equals(evt.getPropertyName())){
            selectedEvents.clearSelection();
            selectedEvents.setListData(((List<CalendarEvent>)evt.getNewValue()).toArray());
        }
        if("selectedCalendar".equals(evt.getPropertyName())){
            calendarName.setText((String)evt.getNewValue());
        }
    }
}

package view;

import controller.ApplicationController;
import model.CalendarEvent;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;

public class ChooseEvents extends JPanel implements PropertyChangeListener {

    JLabel welcomeLabel;
    JList allEvents;
    JList selectedEvents;
    JButton addAll;
    JButton loadEmails;
    JButton moveToSelected;
    JButton removeFromSelected;
    JButton submitButton;

    JLabel messageStatus;

    JTextArea eventDescription;

    ApplicationController applicationController;

    ChooseEvents(ApplicationController applicationController) {
        this.applicationController = applicationController;
        this.applicationController.getCreationConfiguration().addPropertyChangeListener(this);

        SpringLayout layout = new SpringLayout();
        setLayout(layout);

        JPanel topControls = new JPanel();
        topControls.setLayout(new BoxLayout(topControls, BoxLayout.Y_AXIS));

        welcomeLabel = new JLabel("Confirm Events to Create");
        topControls.add(welcomeLabel);

        messageStatus = new JLabel("0 / 0");
        topControls.add(messageStatus);

        loadEmails = new JButton("Load Emails");
        loadEmails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    applicationController.getGoogleController().fetchGmailMessages();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        topControls.add(loadEmails);

        add(topControls);


        allEvents = new JList(applicationController.getCreationConfiguration().getAllEvents().toArray());
        JScrollPane allEventsScroller = new JScrollPane(allEvents);
        allEventsScroller.setPreferredSize(new Dimension(300, 200));
        add(allEventsScroller);

        allEvents.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (allEvents.getSelectedValue() != null) {
                    eventDescription.setText(((CalendarEvent) allEvents.getSelectedValue()).toDetailedString());
                    selectedEvents.clearSelection();
                }
            }
        });

        selectedEvents = new JList(applicationController.getCreationConfiguration().getSelectedEvents().toArray());
        JScrollPane selectedEventsScroller = new JScrollPane(selectedEvents);
        selectedEventsScroller.setPreferredSize(new Dimension(300, 200));
        add(selectedEventsScroller);

        selectedEvents.addListSelectionListener(e -> {
            if (selectedEvents.getSelectedValue() != null) {
                eventDescription.setText(((CalendarEvent) selectedEvents.getSelectedValue()).toDetailedString());
                allEvents.clearSelection();
            }
        });

        JPanel controlButtons = new JPanel();
        controlButtons.setLayout(new BoxLayout(controlButtons, BoxLayout.Y_AXIS));

        addAll = new JButton("Add All Events");
        addAll.setAlignmentX(Component.CENTER_ALIGNMENT);
        addAll.addActionListener(e -> {
            applicationController.getCreationConfiguration().selectAllEvents();

            allEvents.clearSelection();
        });
        controlButtons.add(addAll);

        moveToSelected = new JButton(">>");
        moveToSelected.setAlignmentX(Component.CENTER_ALIGNMENT);
        moveToSelected.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                List<CalendarEvent> events = (List<CalendarEvent>) allEvents.getSelectedValuesList();
                for (CalendarEvent evt : events) {
                    applicationController.getCreationConfiguration().selectEvent(evt);
                }


                allEvents.clearSelection();
            }
        });
        controlButtons.add(moveToSelected);

        removeFromSelected = new JButton("<<");
        removeFromSelected.setAlignmentX(Component.CENTER_ALIGNMENT);

        removeFromSelected.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                List<CalendarEvent> events = (List<CalendarEvent>) selectedEvents.getSelectedValuesList();
                for (CalendarEvent evt : events) {
                    applicationController.getCreationConfiguration().deselectEvent(evt);
                }

                allEvents.clearSelection();
            }
        });
        controlButtons.add(removeFromSelected);

        add(controlButtons);


        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));

        eventDescription = new JTextArea();
        eventDescription.setEditable(false);
        bottom.add(eventDescription);


        submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (applicationController.getCreationConfiguration().getSelectedEvents().size() > 0) {
                    applicationController.advanceWindows();
                } else {
                    allEvents.clearSelection();
                    selectedEvents.clearSelection();
                    eventDescription.setText("Please Select at least One Event");
                }
            }
        });
        bottom.add(submitButton);

        add(bottom);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, topControls, 0, SpringLayout.HORIZONTAL_CENTER, this);

        layout.putConstraint(SpringLayout.WEST, allEventsScroller, 0, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, allEventsScroller, 0, SpringLayout.VERTICAL_CENTER, this);

        layout.putConstraint(SpringLayout.EAST, selectedEventsScroller, 0, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, selectedEventsScroller, 0, SpringLayout.VERTICAL_CENTER, this);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, controlButtons, 0, SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, controlButtons, 0, SpringLayout.VERTICAL_CENTER, this);


        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, bottom, 0, SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.SOUTH, bottom, 0, SpringLayout.SOUTH, this);

    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("allEvents".equals(evt.getPropertyName())) {
            allEvents.setListData(((List) evt.getNewValue()).toArray());
            allEvents.updateUI();

            messageStatus.setText(((List) evt.getNewValue()).size() + "/" + applicationController.getCreationConfiguration().getTotalMessageFound());
            messageStatus.updateUI();

        }

        if ("selectedEvents".equals(evt.getPropertyName())) {
            selectedEvents.setListData(((List) evt.getNewValue()).toArray());
            selectedEvents.updateUI();
        }

        if("totalMessageFound".equals(evt.getPropertyName())){
            messageStatus.setText( 0 + "/" + evt.getNewValue());
            messageStatus.updateUI();
        }

    }

}

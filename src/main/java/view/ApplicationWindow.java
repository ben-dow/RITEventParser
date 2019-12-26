package view;

import controller.ApplicationController;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class ApplicationWindow extends JFrame {

    private JPanel cards;
    private ApplicationController applicationController;
    Logger logger;

    public ApplicationWindow(ApplicationController controller){
        logger = Logger.getLogger("EventLog");


        this.applicationController = controller;
        controller.setWindow(this);
        setSize(800,500);

        //Set Layout to Card Layout
        cards = new JPanel();
        cards.setLayout(new CardLayout());

        logger.info("Creating and adding Windows to Card Stack");


        cards.add(new WelcomeScreen(applicationController));
        cards.add(new GoogleAuthentication(applicationController));
        cards.add(new ChooseEvents(applicationController));
        cards.add(new SelectCalendar(applicationController));
        cards.add(new VerifyConfiguration(applicationController));
        cards.add(new CreatingEvents(applicationController));
        cards.add(new ProcessComplete(applicationController));



        add(cards);

        setVisible(true);

    }

    public void advanceToNext(){
        logger.info("Going to next Window");
        ((CardLayout) cards.getLayout()).next(cards);
    }

    public void backToPrevious(){
        logger.info("Going to Previous Window");
        ((CardLayout) cards.getLayout()).previous(cards);
    }





}

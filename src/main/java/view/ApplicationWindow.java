package view;

import controller.ApplicationController;

import javax.swing.*;
import java.awt.*;

public class ApplicationWindow extends JFrame {

    private JPanel cards;
    private ApplicationController applicationController;

    public ApplicationWindow(ApplicationController controller){
        this.applicationController = controller;
        controller.setWindow(this);
        setSize(800,500);

        //Set Layout to Card Layout
        cards = new JPanel();
        cards.setLayout(new CardLayout());

        cards.add(new WelcomeScreen(applicationController));
        cards.add(new GoogleAuthentication(applicationController));
        cards.add(new ChooseEvents(applicationController));
        cards.add(new SelectCalendar(applicationController));
        cards.add(new VerifyConfiguration(applicationController));
        cards.add(new CreationComplete());


        add(cards);

        setVisible(true);

    }

    public void advanceToNext(){
        ((CardLayout) cards.getLayout()).next(cards);
    }

    public void backToPrevious(){
        ((CardLayout) cards.getLayout()).previous(cards);
    }





}

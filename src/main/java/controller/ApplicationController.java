package controller;

import model.CreationConfiguration;
import view.ApplicationWindow;

public class ApplicationController {


    ApplicationWindow window;
    GoogleController googleController;
    CreationConfiguration creationConfiguration;


    public void setWindow(ApplicationWindow window) {
        this.window = window;
        creationConfiguration = new CreationConfiguration();
    }

    public void advanceWindows(){
        window.advanceToNext();
    }

    public CreationConfiguration getCreationConfiguration() {

        return creationConfiguration;
    }

    public GoogleController getGoogleController(){
        if(googleController != null){
            return googleController;
        } else {
            googleController = new GoogleController(this);
            return googleController;
        }

    }

    public void previousWindow(){
        window.backToPrevious();
    }

}

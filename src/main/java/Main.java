import controller.ApplicationController;
import view.ApplicationWindow;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {

    public static void main(String args[]){


        Logger logger = Logger.getLogger("EventLog");
        FileHandler fh;

        try {

            // This block configure the logger with handler and formatter
            fh = new FileHandler("RITEventsLog.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        logger.info("Creating Application Controller");

        ApplicationController applicationController = new ApplicationController();

        logger.info("Creating Application Window");
        new ApplicationWindow(applicationController);


    }
}

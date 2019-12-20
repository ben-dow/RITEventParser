import controller.ApplicationController;
import view.ApplicationWindow;

public class Main {

    public static void main(String args[]){

        ApplicationController applicationController = new ApplicationController();

        new ApplicationWindow(applicationController);
    }
}

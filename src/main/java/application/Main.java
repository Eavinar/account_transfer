package application;

import application.configuration.BasicModule;
import application.controller.Controller;
import application.helper.DBConnection;
import com.google.inject.Guice;

public class Main {
    public static void main(String[] args) {
        DBConnection.populateDB();
        Guice.createInjector(new BasicModule()).getInstance(Controller.class);
    }
}

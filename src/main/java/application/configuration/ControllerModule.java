package application.configuration;

import application.controller.Controller;
import application.controller.RouteController;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 * Class is responsible for loading required dependencies for Controller.
 */
public class ControllerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Controller.class).to(RouteController.class).in(Singleton.class);
    }
}
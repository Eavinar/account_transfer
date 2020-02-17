package application.configuration;

import com.google.inject.AbstractModule;

/**
 * Class is responsible for loading all modules.
 */
public class BasicModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new ServiceModule());
        install(new RepositoryModule());
        install(new ControllerModule());
        install(new ConsumerModule());
    }
}
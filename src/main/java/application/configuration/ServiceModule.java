package application.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import application.service.account.AccountService;
import application.service.account.AccountServiceImpl;
import application.service.transfer.TransferService;
import application.service.transfer.TransferServiceImpl;

/**
 * Class is responsible for loading required dependencies for Service.
 */
public class ServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AccountService.class).to(AccountServiceImpl.class).in(Singleton.class);
        bind(TransferService.class).to(TransferServiceImpl.class).in(Singleton.class);
    }
}
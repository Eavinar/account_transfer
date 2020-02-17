package application.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import application.dao.account.AccountDAO;
import application.dao.account.AccountDAOImpl;
import application.dao.transfer.TransferDAO;
import application.dao.transfer.TransferDAOImpl;

/**
 * Class is responsible for loading required dependencies for Repository.
 */
public class RepositoryModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AccountDAO.class).to(AccountDAOImpl.class).in(Singleton.class);
        bind(TransferDAO.class).to(TransferDAOImpl.class).in(Singleton.class);
    }
}
package application.service.account;

import application.dao.account.AccountDAO;
import application.entity.Account;
import application.exception.AccountNotFoundException;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Class is implementation of the service layer for handling Account related business logic
 */
@Singleton
public class AccountServiceImpl implements AccountService {
    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountDAO accountDAO;

    @Inject
    public AccountServiceImpl(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    /**
     * {@inheritDoc}
     */
    public Account findById(String id) {
        Optional<Account> account = accountDAO.findById(Long.valueOf(id));
        if (!account.isPresent()) {
            log.error("Account doesn't exist");
            throw new AccountNotFoundException(id);
        }
        return account.get();
    }
}

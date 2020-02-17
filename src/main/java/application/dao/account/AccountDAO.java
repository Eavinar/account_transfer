package application.dao.account;

import application.entity.Account;

import java.util.Optional;

/**
 * Class is responsible for handling DAO operations on {@code application.dao.account.Account}.
 */
public interface AccountDAO {
    /**
     * Method returns Account entity based on unique identifier
     *
     * @param id is Account unique identifier
     * @return Account entity by identifier
     */
    Optional<Account> findById(Long id);
}

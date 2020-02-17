package application.service.account;

import application.entity.Account;

/**
 * Class is service layer for handling Account related business logic
 */
public interface AccountService {
    /**
     * Method returns Account base on unique identifier of Account
     *
     * @param id is unique identifier for Account entity
     * @return Account entity.
     */
    Account findById(String id);
}

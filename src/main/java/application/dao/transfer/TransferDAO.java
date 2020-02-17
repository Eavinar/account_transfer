package application.dao.transfer;

import application.entity.Account;
import application.entity.Transfer;

import java.util.Optional;

/**
 * Class is responsible for handling DAO operations on {@code application.dao.transfer.Transfer}.
 */
public interface TransferDAO {

    /**
     * Method returns Transfer entity based on unique identifier
     *
     * @param id is Account unique identifier
     * @return Transfer entity by identifier
     */
    Optional<Transfer> findById(Long id);

    /**
     * Method creates Transfer entity and pushes transfer into the queue.
     *
     * @param debitAccount from which account balance should be deducted.
     * @param creditAccount to which account amount will be added.
     * @param amount is balance to transfer.
     * @return Transfer entity after creating it and pushing into the queue
     */
    Transfer createTransfer(Account debitAccount, Account creditAccount, String amount);

    /**
     * Method updates account based on transfer details.
     *
     * @param transfer is transaction based on what accounts is going to be updated.
     * @return
     */
    Transfer updateTransferAndAccounts(Transfer transfer);
}

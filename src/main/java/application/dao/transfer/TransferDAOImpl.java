package application.dao.transfer;

import application.entity.Account;
import application.entity.Transfer;
import application.helper.DBConnection;
import com.google.inject.Singleton;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * Implementation of DAO interface for Transfer
 */
@Singleton
public class TransferDAOImpl implements TransferDAO {
    private static final Logger log = LoggerFactory.getLogger(TransferDAOImpl.class);
    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Transfer> findById(Long id) {
        Optional<Transfer> transfer;
        Transaction transaction = null;
        try (Session session = DBConnection.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            TypedQuery<Transfer> query = session.createQuery("FROM Transfer t where t.id = :id ORDER BY t.createdAt");
            query.setParameter("id", id);
            transfer = query.getResultList().stream().findFirst();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            log.error("Exception occurred while obtaining Transfer Entity");
            throw e;
        }
        return transfer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transfer createTransfer(Account debitAccount, Account creditAccount, String amount) {
        Transfer transfer;
        Transaction transaction = null;
        try (Session session = DBConnection.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            transfer = new Transfer();
            transfer.setAmount(new BigDecimal(amount));
            transfer.setDebitAccount(debitAccount);
            transfer.setCreditAccount(creditAccount);
            transfer.setStatus("pending");

            BigDecimal currentAmount = new BigDecimal(amount);

            debitAccount.setCurrentAmount(debitAccount.getCurrentAmount().subtract(currentAmount));
            debitAccount.setBlockedAmount(debitAccount.getBlockedAmount().add(currentAmount));
            session.save(transfer);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            log.error("Exception occurred while creating Transfer Entity");
            throw e;
        }
        return transfer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transfer updateTransferAndAccounts(Transfer transfer) {
        Transaction transaction = null;
        try (Session session = DBConnection.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Account debitAccount = transfer.getDebitAccount();
            Account creditAccount = transfer.getCreditAccount();

            debitAccount.setBlockedAmount(debitAccount.getBlockedAmount().subtract(transfer.getAmount()));
            creditAccount.setCurrentAmount(creditAccount.getCurrentAmount().add(transfer.getAmount()));
            transfer.setStatus("done");

            session.update(transfer);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Exception occurred while updating Transfer Entity");
            handleFailedTransfer(transfer);
            e.printStackTrace();
        }
        return transfer;
    }

    private void handleFailedTransfer(Transfer transfer) {
        Transaction transaction = null;
        try (Session session = DBConnection.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            transfer.getDebitAccount().getBlockedAmount().subtract(transfer.getAmount());
            transfer.getDebitAccount().getCurrentAmount().add(transfer.getAmount());
            transfer.setStatus("failed");

            session.update(transfer);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Exception occurred while handling failed Transfer Entity");
            e.printStackTrace();
        }
    }
}

package application.dao.account;

import application.entity.Account;
import application.helper.DBConnection;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.persistence.TypedQuery;
import java.util.Optional;

/**
 * Implementation of DAO interface for Account
 */
@Singleton
public class AccountDAOImpl implements AccountDAO {
    private static final Logger log = LoggerFactory.getLogger(AccountDAOImpl.class);
    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Account> findById(Long id) {
        Optional<Account> account = null;
        Transaction transaction = null;
        try (Session session = DBConnection.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            TypedQuery<Account> query = session.createQuery("from Account where id = :id", Account.class);
            query.setParameter("id", id);
            account = query.getResultList().stream().findFirst();
            transaction.commit();
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            exception.printStackTrace();
            log.error("Exception occurred while obtaining Account Entity");
        }
        return account;
    }
}

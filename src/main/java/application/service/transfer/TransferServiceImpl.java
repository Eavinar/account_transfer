package application.service.transfer;

import application.consumer.TransferConsumer;
import application.dto.TransferDTO;
import application.entity.Account;
import application.entity.Transfer;
import application.exception.TransferNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import application.dao.account.AccountDAO;
import application.dao.transfer.TransferDAO;
import application.validate.TransferValidator;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of Bussiness logic of transfer service layer.
 */
@Singleton
public class TransferServiceImpl implements TransferService {
    private static final Logger log = LoggerFactory.getLogger(TransferServiceImpl.class);

    private static final String DEBIT_ACCOUNT = "debit";
    private static final String CREDIT_ACCOUNT = "credit";

    private final TransferDAO transferDAO;
    private final AccountDAO accountDAO;
    private final TransferConsumer transferConsumer;

    @Inject
    public TransferServiceImpl(TransferDAO transferDAO, AccountDAO accountDAO,
                               TransferConsumer transferConsumer) {
        this.transferDAO = transferDAO;
        this.accountDAO = accountDAO;
        this.transferConsumer = transferConsumer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transfer findById(String id) {
        Optional<Transfer> transfer = transferDAO.findById(Long.valueOf(id));
        if (!transfer.isPresent()) {
            log.error("Transfer doesn't exist");
            throw new TransferNotFoundException(id);
        }
        return transfer.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transfer createTransfer(TransferDTO transferDTO) {
        TransferValidator transferValidator = new TransferValidator(transferDAO, accountDAO);
        Map<String, Account> accountMap = transferValidator.validateAccounts(transferDTO.getDebit(), transferDTO.getCredit(), transferDTO.getAmount());
        Transfer transfer = transferDAO.createTransfer(accountMap.get(DEBIT_ACCOUNT), accountMap.get(CREDIT_ACCOUNT), transferDTO.getAmount());
        transferDTO.setId(transfer.getId());
        transferConsumer.produce(transferDTO);
        return transfer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TransferDTO dequeueTransfer() {
        return transferConsumer.consume();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAccountsByTransfer() {
        while (true) {
            TransferDTO transferDTO = dequeueTransfer();
            if(transferDTO != null) {
                Optional<Transfer> transfer = transferDAO.findById(transferDTO.getId());

                transfer.ifPresentOrElse(
                        transferDAO::updateTransferAndAccounts,
                        () -> {
                            log.error("Transfer doesn't exist");
                            throw new TransferNotFoundException(String.valueOf(transferDTO.getId()));
                        }
                );
            }
        }
    }
}

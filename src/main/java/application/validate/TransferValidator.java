package application.validate;

import application.entity.Account;
import application.exception.AccountNotFoundException;
import application.dao.account.AccountDAO;
import application.dao.transfer.TransferDAO;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TransferValidator {
    private static final String DEBIT_ACCOUNT = "debit";
    private static final String CREDIT_ACCOUNT = "credit";

    private final TransferDAO transferDAO;
    private final AccountDAO accountDAO;

    public TransferValidator(TransferDAO transferDAO, AccountDAO accountDAO) {
        this.transferDAO = transferDAO;
        this.accountDAO = accountDAO;
    }

    public Map<String, Account> validateAccounts(String debitAccount, String creditAccount, String amount) {
        Map<String, Account> accountMap = new HashMap<>();

        if (debitAccount.equals(creditAccount)) {
            throw new IllegalArgumentException("Account cannot be the same");
        }

        accountMap.put(DEBIT_ACCOUNT, validateAndGetAccount(debitAccount));
        accountMap.put(CREDIT_ACCOUNT, validateAndGetAccount(creditAccount));

        validateAccountBalance(accountMap.get(DEBIT_ACCOUNT), new BigDecimal(amount));
        return accountMap;
    }

    private Account validateAndGetAccount(String currentAccount) {
        Optional<Account> account = accountDAO.findById(Long.valueOf(currentAccount));
        if (!account.isPresent()) {
            throw new AccountNotFoundException(currentAccount);
        }
        return account.get();
    }

    public static void validateAccountBalance(Account account, BigDecimal amount) {
        if (account.getCurrentAmount().compareTo(account.getBlockedAmount().add(amount)) < 0) {
            throw new IllegalArgumentException("Balance is not enough to make a transfer");
        }
    }
}

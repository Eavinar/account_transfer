package application.integration;

import application.Main;
import application.entity.Account;
import application.exception.AccountNotFoundException;
import org.junit.jupiter.api.*;
import application.dao.account.AccountDAO;
import application.dao.account.AccountDAOImpl;
import application.service.account.AccountService;
import application.service.account.AccountServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static spark.Spark.awaitInitialization;
import static spark.Spark.stop;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class AccountServiceTest {
    private static AccountDAO accountDAO;
    private static AccountService accountService;

    @BeforeAll
    public static void setUp() {
        Main.main(null);
        awaitInitialization();
        accountDAO = new AccountDAOImpl();
        accountService = new AccountServiceImpl(accountDAO);
    }

    @AfterAll
    public static void tearDown() {
        stop();
    }

    @Test
    public void findByIdTest() {
        Account account = accountService.findById("1");
        assertNotNull(account);
        assertEquals(1, account.getId());
    }

    @Test
    public void testAccountNotFoundException() {
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.findById("100");
        });
    }
}

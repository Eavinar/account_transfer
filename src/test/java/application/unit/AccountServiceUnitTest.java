package application.unit;

import application.entity.Account;
import application.exception.AccountNotFoundException;
import application.dao.account.AccountDAO;
import application.service.account.AccountService;
import application.service.account.AccountServiceImpl;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class AccountServiceUnitTest {
    private static AccountDAO accountDAO;
    private static AccountService accountService;

    @BeforeAll
    public static void setUp() {
//        Main.main(null);
//        awaitInitialization();
        accountDAO = mock(AccountDAO.class);
        accountService = new AccountServiceImpl(accountDAO);
    }

    @Test
    public void findByIdTest() {
        when(accountDAO.findById(1L)).thenReturn(Optional.of(new Account()));
        Account account = accountService.findById("1");
        verify(accountDAO, times(1)).findById(1L);
        assertNotNull(account);
    }

    @Test
    public void testAccountNotFoundException() {
        when(accountDAO.findById(100L)).thenReturn(Optional.ofNullable(null));
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.findById("100");
        });
    }
}

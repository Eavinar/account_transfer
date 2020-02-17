package application.unit;

import application.consumer.TransferConsumer;
import application.dto.TransferDTO;
import application.entity.Account;
import application.entity.Transfer;
import application.exception.TransferNotFoundException;
import application.dao.account.AccountDAO;
import application.dao.transfer.TransferDAO;
import application.service.transfer.TransferService;
import application.service.transfer.TransferServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransferServiceUnitTest {
    private static TransferService transferService;
    private static TransferDAO transferDAO;
    private static TransferConsumer transferConsumer;
    private static AccountDAO accountDAO;

    @BeforeAll
    public static void setUp() {
        accountDAO = mock(AccountDAO.class);
        transferDAO = mock(TransferDAO.class);
        transferConsumer = mock(TransferConsumer.class);
        transferService = new TransferServiceImpl(transferDAO, accountDAO, transferConsumer);
    }

    @Test
    public void createTransferTest() {
        TransferDTO transferDTO = new TransferDTO(1L, "1", "2", "20");
        Transfer transfer = new Transfer();
        transfer.setId(100L);
        Account account1 = new Account(1L, null, null, new BigDecimal("100"), new BigDecimal("0"));
        Account account2 = new Account(2L, null, null, new BigDecimal("200"), new BigDecimal("0"));
        when(accountDAO.findById(1L)).thenReturn(Optional.of(account1));
        when(accountDAO.findById(2L)).thenReturn(Optional.of(account2));
        when(transferDAO.createTransfer(account1, account2, transferDTO.getAmount())).thenReturn(transfer);
        transferService.createTransfer(transferDTO);

        verify(transferConsumer, times(1)).produce(transferDTO);
    }

    @Test
    public void findByIdTest() {
        when(transferDAO.findById(1L)).thenReturn(Optional.of(new Transfer()));
        Transfer transfer = transferService.findById("1");
        assertNotNull(transfer);
        verify(transferDAO, times(1)).findById(1L);
    }

    @Test
    public void testTransferNotFoundException() {
        when(transferDAO.findById(1001L)).thenReturn(Optional.ofNullable(null));
        assertThrows(TransferNotFoundException.class, () -> {
            transferService.findById("1001");
        });
    }

    @Test
    public void testIllegalArgumentException() {
        when(transferDAO.findById(1001L)).thenReturn(Optional.ofNullable(null));
        assertThrows(TransferNotFoundException.class, () -> {
            transferService.findById("1001");
        });
    }
}


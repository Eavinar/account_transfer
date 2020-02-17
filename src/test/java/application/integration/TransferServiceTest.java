package application.integration;

import application.Main;
import application.consumer.TransferConsumer;
import application.dto.TransferDTO;
import application.entity.Transfer;
import application.exception.TransferNotFoundException;
import application.dao.account.AccountDAO;
import application.dao.account.AccountDAOImpl;
import application.dao.transfer.TransferDAO;
import application.dao.transfer.TransferDAOImpl;
import application.service.transfer.TransferService;
import application.service.transfer.TransferServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;
import static spark.Spark.awaitInitialization;
import static spark.Spark.stop;

public class TransferServiceTest {
    private static TransferService transferService;
    private static TransferDAO transferDAO;
    private static TransferConsumer transferConsumer;
    private static AccountDAO accountDAO;

    @BeforeAll
    public static void setUp() {
        Main.main(null);
        awaitInitialization();
        accountDAO = new AccountDAOImpl();
        transferDAO = new TransferDAOImpl();
        transferConsumer = new TransferConsumer(new LinkedBlockingQueue<>());
        transferService = new TransferServiceImpl(transferDAO, accountDAO, transferConsumer);
    }

    @AfterAll
    public static void tearDown() {
        stop();
    }

    @Test
    public void createTransferTest() {
        TransferDTO transferDTO = new TransferDTO(1L, "10", "11", "20");
        Transfer transfer = transferService.createTransfer(transferDTO);
        assertNotNull(transfer);
        assertEquals(1, transfer.getId());

        int initialMessageSize = transferConsumer.size();
        transferConsumer.consume();
        int messageSizeAfterConsuming = transferConsumer.size();

        assertTrue(initialMessageSize > 0);
        assertTrue(initialMessageSize > messageSizeAfterConsuming);
    }

    @Test
    public void findByIdTest() {
        Transfer transfer = transferService.findById("1");
        assertNotNull(transfer);
        assertEquals(1L, transfer.getId());
    }

    @Test
    public void testTransferNotFoundException() {
        assertThrows(TransferNotFoundException.class, () -> {
            transferService.findById("1001");
        });
    }
}


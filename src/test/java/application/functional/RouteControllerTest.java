package application.functional;

import application.Main;
import application.helper.TestResponse;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static application.helper.TestResponse.request;
import static org.junit.jupiter.api.Assertions.*;
import static spark.Spark.awaitInitialization;
import static spark.Spark.stop;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class RouteControllerTest {

    @BeforeAll
    public static void setUp() {
        Main.main(null);
        awaitInitialization();
    }

    @AfterAll
    public static void tearDown() {
        stop();
    }

    @Test
    public void getAccountTest() throws IOException, InterruptedException {
        TestResponse response = request("GET", "/api/account/1", Collections.emptyMap());
        Map json = response.json();
        assertEquals(200, response.status);
        assertEquals(1, (Double) json.get("id"));
        assertEquals(100, (Double) json.get("currentAmount"));
        assertEquals(0, (Double) json.get("blockedAmount"));
    }

    @Test
    public void addTransactionTest() throws IOException, InterruptedException {
        Map<String, Object> params = new HashMap<>();
        params.put("debit", "3");
        params.put("credit", "4");
        params.put("amount", "10");

        TestResponse response = request("POST", "/api/transfer", params);
        Map json = response.json();
        assertEquals(200, response.status);

        assertNotNull(json.get("id"));
        assertEquals("pending", json.get("status"));
        assertEquals(10, (Double) json.get("amount"));

        Map debitAccountBeforeTransferUpdate = (Map) json.get("debitAccount");
        Map creditAccountBeforeTransferUpdate = (Map) json.get("creditAccount");

        assertEquals(3, (Double) debitAccountBeforeTransferUpdate.get("id"));
        assertEquals(10, (Double) debitAccountBeforeTransferUpdate.get("blockedAmount"));
        assertEquals(290, (Double) debitAccountBeforeTransferUpdate.get("currentAmount"));

        assertEquals(4, (Double) creditAccountBeforeTransferUpdate.get("id"));
        assertEquals(0, (Double) creditAccountBeforeTransferUpdate.get("blockedAmount"));
        assertEquals(400, (Double) creditAccountBeforeTransferUpdate.get("currentAmount"));

        Thread.sleep(2000L);

        Map debitAccountTransferUpdate = getAccountDetails(3).json();
        Map creditAccountTransferUpdate = getAccountDetails(4).json();

        assertEquals(3, (Double) debitAccountTransferUpdate.get("id"));
        assertEquals(0, (Double) debitAccountTransferUpdate.get("blockedAmount"));
        assertEquals(290, (Double) debitAccountTransferUpdate.get("currentAmount"));

        assertEquals(4, (Double) creditAccountTransferUpdate.get("id"));
        assertEquals(0, (Double) creditAccountTransferUpdate.get("blockedAmount"));
        assertEquals(410, (Double) creditAccountTransferUpdate.get("currentAmount"));
    }

    @Test
    public void getTransferTest() throws IOException, InterruptedException {
        TestResponse response = request("GET", "/api/transfer/1", Collections.emptyMap());
        Map json = response.json();
        assertEquals(200, response.status);
        assertNotNull(json.get("id"));
    }

    @Test
    public void nonExistingAccountTest() throws IOException, InterruptedException {
        TestResponse response = request("GET", "/api/account/100", Collections.emptyMap());
        Map json = response.json();
        assertEquals(404, response.status);
        assertEquals("Account with id 100 not found.", json.get("message"));
    }

    @Test
    public void nonExistingTransferTest() throws IOException, InterruptedException {
        TestResponse response = request("GET", "/api/transfer/100", Collections.emptyMap());
        Map json = response.json();
        assertEquals(404, response.status);
        assertEquals("Transfer with id 100 not found.", json.get("message"));
    }

    private TestResponse getAccountDetails(long account) throws IOException, InterruptedException {
        TestResponse response = request("GET", "/api/account/" + account, Collections.emptyMap());
        return response;
    }
}

package application.controller;

import application.dto.TransferDTO;
import application.exception.AccountNotFoundException;
import application.exception.TransferNotFoundException;
import application.helper.ResponseError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import application.service.account.AccountService;
import application.service.transfer.TransferService;
import spark.Spark;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.PrintWriter;
import java.io.StringWriter;

import static spark.Spark.*;
import static application.helper.JsonUtil.*;

/**
 * Main controller which is manages routes.
 */
@Singleton
public class RouteController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(RouteController.class);

    private final static String APPLICATION_JSON = "application/json";
    private final static String NOT_FOUND_MESSAGE = "{\"message\":\"Custom 404\"}";
    private final static String ERROR_500_MESSAGE = "{\"message\":\"Custom 500 handling\"}";

    private final AccountService accountService;
    private final TransferService transferService;

    @Inject
    public RouteController(final AccountService accountService, final TransferService transferService) {
        this.accountService = accountService;
        this.transferService = transferService;
        startApplication();
    }

    private void startApplication() {
        path("/api", () -> {
            routes();
            serverErrorHandler();
            exceptionHandler();
        });
        after((request, response) -> response.type(APPLICATION_JSON));
        startConsumer();
    }

    private void routes() {
        get("/account/:id", (request, response) -> {
            log.info("GET Account request invoked");
            return accountService.findById(request.params("id"));
        }, json());

        get("/transfer/:id", (request, response) -> {
            log.info("GET Transfer request invoked");
            return transferService.findById(request.params("id"));
        }, json());

        post("/transfer", APPLICATION_JSON, (request, response) -> {
            log.info("POST create a new transfer request invoked");
            TransferDTO transferDTO = fromJson(request.body(), TransferDTO.class);
            return transferService.createTransfer(transferDTO);
        }, json());
    }

    private void serverErrorHandler() {
        internalServerError((request, response) -> {
            log.error("Internatl Server Error took place");
            response.type(APPLICATION_JSON);
            return ERROR_500_MESSAGE;
        });

        notFound((request, response) -> {
            log.error("Not Found request error took place");
            response.type(APPLICATION_JSON);
            return NOT_FOUND_MESSAGE;
        });
    }

    private void startConsumer() {
        Thread consumer = new Thread(transferService::updateAccountsByTransfer);
        log.error("Starting consumer... in thread: " + consumer.getName());
        consumer.start();
    }

    private void exceptionHandler() {
        // initialize exception handler
        Spark.exception(Exception.class, (e, request, response) -> {
            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw, true);
            e.printStackTrace(pw);
            System.out.println(sw.getBuffer().toString());
        });

        exception(AccountNotFoundException.class, (exception, request, response) -> {
            log.error("AccountNotFoundException error took place.");
            response.type(APPLICATION_JSON);
            response.status(404);
            response.body(toJson(new ResponseError(exception.getMessage())));
        });

        exception(TransferNotFoundException.class, (exception, request, response) -> {
            log.error("TransferNotFoundException error took place.");
            response.type(APPLICATION_JSON);
            response.status(404);
            response.body(toJson(new ResponseError(exception.getMessage())));
        });

        exception(IllegalArgumentException.class, (exception, request, response) -> {
            log.error("IllegalArgumentException error took place.");
            response.type(APPLICATION_JSON);
            response.status(500);
            response.body(toJson(new ResponseError(exception.getMessage())));
        });
    }
}
package application.service.transfer;

import application.dto.TransferDTO;
import application.entity.Transfer;

/**
 * Class is service layer for handling Transfer related business logic
 */
public interface TransferService {
    /**
     * Method returns Account base on unique identifier of Account
     *
     * @param id is unique identifier for Account entity
     * @return Account entity.
     */
    Transfer findById(String id);

    /**
     * Method creates transfers based on Transfer details.
     *
     * @param transferDTO is transfer details mapped to POJO file
     * @return transfer entity
     */
    Transfer createTransfer(TransferDTO transferDTO);

    /**
     * Takes Transfer DTO from the queue.
     * @return peeked Transfer details mapped POJO file
     */
    TransferDTO dequeueTransfer();

    /**
     * Method updates Accounts details based on Transfer details.
     */
    void updateAccountsByTransfer();
}

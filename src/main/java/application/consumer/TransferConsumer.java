package application.consumer;

import application.dto.TransferDTO;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.util.concurrent.BlockingQueue;

@Singleton
public class TransferConsumer {
    private static final Logger log = LoggerFactory.getLogger(TransferConsumer.class);
    private final BlockingQueue<TransferDTO> queue;

    @Inject
    public TransferConsumer(@Named("transfer-application.consumer") BlockingQueue<TransferDTO> queue) {
        this.queue = queue;
    }

    public TransferDTO consume() {
        TransferDTO transferDTO = null;
        try {
            transferDTO = queue.take();
        } catch (InterruptedException e) {
            log.error("Exception occured during dequeue process");
            e.printStackTrace();
        }
        return transferDTO;
    }

    public void produce(TransferDTO transferDTO) {
        try {
            queue.put(transferDTO);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("Exception occured during enqueue process");
        }
    }

    public int size() {
        return queue.size();
    }
}

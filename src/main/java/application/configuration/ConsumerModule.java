package application.configuration;

import application.consumer.TransferConsumer;
import application.dto.TransferDTO;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import javax.inject.Named;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Class is responsible for loading required dependencies for Consumer.
 */
public class ConsumerModule extends AbstractModule {
    @Provides
    @Named("transfer-application.consumer")
    public BlockingQueue<TransferDTO> queue() {
        return new LinkedBlockingQueue<>();
    }

    @Override
    protected void configure() {
        bind(TransferConsumer.class).in(Singleton.class);
    }
}
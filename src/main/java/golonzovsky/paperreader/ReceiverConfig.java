package golonzovsky.paperreader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.PollableChannel;

import java.io.File;


/* todo extract so separate app */
@Slf4j
@Configuration
@EnableBinding(Sink.class)
public class ReceiverConfig {

    @StreamListener(Sink.INPUT)
    public void receiveAsync(Paper p) {
        log.info("RECEIVE ASYNC '{}'", p.getTitle());
        //todo store to neo4j and mongo/elastic for text search
    }

}

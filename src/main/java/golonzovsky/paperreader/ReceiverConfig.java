package golonzovsky.paperreader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
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
@EnableBinding(Sink.class)
public class ReceiverConfig {

    @Autowired
    PaperRepository neoRepo;

    @StreamListener(Sink.INPUT)
    public void createNeo4jEntry(Paper p) {
        log.info("indexing createNeo4jEntry for '{}'", p.getTitle());
        //todo store to neo4j finding existing references
        neoRepo.save(p);
    }

    @StreamListener(Sink.INPUT)
    public void createTextIndexEntry(Paper p) {
        log.info("indexing createTextIndexEntry for'{}'", p.getTitle());
        //todo store to mongo/elastic for full text search

    }

}

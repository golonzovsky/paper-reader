package golonzovsky.paperreader;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.aws.inbound.S3InboundFileSynchronizer;
import org.springframework.integration.aws.inbound.S3InboundFileSynchronizingMessageSource;
import org.springframework.integration.aws.support.filters.S3RegexPatternFileListFilter;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.PollableChannel;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Function;

@Slf4j
@SpringBootApplication
@EnableBinding(Source.class)
public class PaperReaderApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaperReaderApplication.class, args);
    }

    @Autowired
    private Source source;

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata defaultPoller() {
        PollerMetadata pollerMetadata = new PollerMetadata();
        pollerMetadata.setTrigger(new PeriodicTrigger(10));
        return pollerMetadata;
    }

    @Bean
    AmazonS3 s3() {
        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.EU_CENTRAL_1)
                .withCredentials(new ProfileCredentialsProvider("default"))
                .withClientConfiguration(new ClientConfiguration().withRequestTimeout(5000))
                .build();
    }

    @Bean
    public S3InboundFileSynchronizer s3InboundFileSynchronizer() {
        S3InboundFileSynchronizer synchronizer = new S3InboundFileSynchronizer(s3());
        synchronizer.setDeleteRemoteFiles(true);
        synchronizer.setPreserveTimestamp(true);
        synchronizer.setRemoteDirectory("paper-storage");
        synchronizer.setFilter(new S3RegexPatternFileListFilter("^.*\\.pdf$"));
        return synchronizer;
    }

    @Bean
    @InboundChannelAdapter(value = "s3FilesChannel", poller = @Poller(fixedDelay = "100"))
    public S3InboundFileSynchronizingMessageSource s3InboundFileSynchronizingMessageSource() throws IOException {
        S3InboundFileSynchronizingMessageSource messageSource = new S3InboundFileSynchronizingMessageSource(s3InboundFileSynchronizer());
        messageSource.setAutoCreateLocalDirectory(true);
        messageSource.setLocalDirectory(createTempDir());
        messageSource.setLocalFilter(new AcceptOnceFileListFilter<>());
        return messageSource;
    }

    private static File createTempDir() throws IOException {
        File tempDir = Files.createTempDirectory("papers-local-folder").toFile();
        log.info("create temp dir for S3 sync: {}", tempDir.getAbsolutePath());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("removing S3 sync temp dir {}", tempDir.getAbsolutePath());
            FileSystemUtils.deleteRecursively(tempDir);
        }));
        return tempDir;
    }

    @Bean
    public PollableChannel s3FilesChannel() {
        return new QueueChannel();
    }

    @Autowired
    private PaperParser parser;

    @Transformer(inputChannel = "s3FilesChannel", outputChannel = "output")
    public Paper handler(File file) {
        Paper paper = parser.parse(file);
        log.info("parsing file {}, {}", file.getAbsolutePath(), paper);
        return paper;
    }


}

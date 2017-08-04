package golonzovsky.paperreader;

import org.junit.Test;
import java.io.File;
import static org.junit.Assert.*;

public class PaperParserTest {

    private PaperParser parser = new PaperParser();

    @Test
    public void testDynamo() throws Exception {
        File dynamo = new File("/home/alex/Downloads/amazon-dynamo-sosp2007.pdf");
        Paper dynamoPaper = parser.parse(dynamo);
        assertEquals("Dynamo: Amazon’s Highly Available Key-value Store", dynamoPaper.getTitle());
        assertEquals(24, dynamoPaper.getReferences().size());
    }

    @Test
    public void testZookeeper() throws Exception {
        File zookeeper = new File("/home/alex/Downloads/Hunt.pdf");
        Paper zookeeperPaper = parser.parse(zookeeper);
        assertEquals("ZooKeeper: Wait-free coordination for Internet-scale systems", zookeeperPaper.getTitle());
        assertEquals(31, zookeeperPaper.getReferences().size());
    }

    @Test
    public void testSpanner() throws Exception {
        File spanner = new File("/home/alex/Downloads/spanner.pdf");
        Paper spannerPaper = parser.parse(spanner);
        assertEquals("Spanner: Becoming a SQL System", spannerPaper.getTitle());
        assertEquals(10, spannerPaper.getReferences().size());
    }

}
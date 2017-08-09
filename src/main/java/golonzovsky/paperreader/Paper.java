package golonzovsky.paperreader;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.neo4j.ogm.annotation.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@NodeEntity
@Document(indexName = "paper", type = "paper", shards = 1, replicas = 0, refreshInterval = "-1")
@Getter @Setter @ToString @Builder
public class Paper {

    @GraphId
    private String id;

    @Id
    @Transient
    private String elasticIdentifier;

    @Index(unique = true, primary = true)
    private String title;

    private String authors;

    @Transient
    private String referencesFull;

    @Index
    private List<String> referencesText;

    @Relationship(type = "REFERENCES")
    private List<Paper> references;
}

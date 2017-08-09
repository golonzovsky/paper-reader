package golonzovsky.paperreader;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Transient;

import java.util.List;

@NodeEntity
@Getter @Setter @ToString @Builder
public class Paper {
    @GraphId private Long id;
    private String title;
    private String authors;

    @Transient
    private String referencesFull;

    @Transient
    private List<String> referencesText;

    @Relationship(type = "REFERENCES")
    private List<Paper> references;
}

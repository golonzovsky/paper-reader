package golonzovsky.paperreader;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.neo4j.ogm.annotation.*;

import java.util.List;

@NodeEntity
@Getter @Setter @ToString @Builder
public class Paper {
    @GraphId
    private Long id;

    @Index(unique = true, primary = true)
    private String title;

    private String authors;

    @Transient
    private String referencesFull;

    @Transient
    private List<String> referencesText;

    @Relationship(type = "REFERENCES")
    private List<Paper> references;
}

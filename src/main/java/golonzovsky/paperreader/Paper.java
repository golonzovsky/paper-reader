package golonzovsky.paperreader;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
class Paper {
    private String title;
    private String authors;
    private String referencesFull;
    private List<String> references;
}

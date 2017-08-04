package golonzovsky.paperreader;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class PaperReaderApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaperReaderApplication.class, args);
    }

}

@Component
class PaperParser {

    private PDFTextStripper pdfStripper;

    public PaperParser() {
        try {
            this.pdfStripper = new PDFTextStripper();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Paper parse(File file) throws IOException {
        PDDocument inputDoc = PDDocument.load(file);
        String text = pdfStripper.getText(inputDoc);

        Paper.PaperBuilder builder = Paper.builder()
                .title(text.replaceAll("(.*?)\\W?\n.*", "$1"))
                .authors(text.replaceAll("(?is)[^\n]*\n(.*?)abstract.*", "$1"));

        setReferences(text, builder);

        return builder.build();
    }

    private void setReferences(String text, Paper.PaperBuilder builder) {
        String references = text.replaceAll("(?is).*references?(.*)", "$1");
        builder.referencesFull(references);

        List<String> refs = Arrays.stream(references.split("\\["))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> "[" + s)
                .collect(Collectors.toList());
        builder.references(refs);
    }
}

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

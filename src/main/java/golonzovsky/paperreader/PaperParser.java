package golonzovsky.paperreader;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

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

    public Paper parse(File file) {
        String text = getFileAsText(file);

        Paper.PaperBuilder builder = Paper.builder()
                .title(text.replaceAll("(.*?)\\W?\n.*", "$1"))
                .authors(text.replaceAll("(?is)[^\n]*\n(.*?)abstract.*", "$1"));

        setReferences(text, builder);

        return builder.build();
    }

    private String getFileAsText(File file) {
        try (PDDocument inputDoc = PDDocument.load(file)) {
            return pdfStripper.getText(inputDoc);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setReferences(String text, Paper.PaperBuilder builder) {
        String references = text.replaceAll("(?is).*references?(.*)", "$1");
        builder.referencesFull(references);

        List<String> refs = Arrays.stream(references.split("\\[.+]"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> s.replaceAll("\n", ""))
                .collect(toList());
        builder.referencesText(refs);
    }
}

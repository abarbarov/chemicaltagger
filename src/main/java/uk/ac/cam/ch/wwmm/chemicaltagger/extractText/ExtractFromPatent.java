package uk.ac.cam.ch.wwmm.chemicaltagger.extractText;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.ParsingException;
import nu.xom.Text;
import org.apache.log4j.Logger;

public class ExtractFromPatent {

    private final Logger LOG = Logger.getLogger(ExtractFromPatent.class);

    public DocumentContainer getContent(String sourceFile) {
        Builder builder = new Builder();
        Document doc = null;
        DocumentContainer docContainer = new DocumentContainer();

        try {
            doc = builder.build(sourceFile);
            docContainer.setId(doc.getRootElement().getAttributeValue("id"));
            Nodes sections = doc.query("//p");
            String content = "";
            for (int i = 0; i < sections.size(); i++) {

                Node node = sections.get(i);
                for (int j = 0; j < node.getChildCount(); j++) {
                    if (node.getChild(j) instanceof Text) {
                        content = content + " " + node.getChild(j).getValue();
                    }


                }
            }

            String spectra = "";
            Nodes spectrum = doc.query("//spectrum");
            for (int i = 0; i < spectrum.size(); i++) {
                spectra = spectra + " " + spectrum.get(i).getValue();

            }

            docContainer.setContent(content.trim());
            docContainer.setNMR(spectra.trim());
        } catch (ParsingException ex) {
            LOG.fatal("ParsingException " + ex.getMessage(), new RuntimeException());
        } catch (IOException ex) {

            LOG.fatal(ex.getMessage(), new RuntimeException());
        }
        return docContainer;
    }

    public static void main(String[] args) {
        List docs = new ArrayList<DocumentContainer>();
        String path = "src/main/resources/patents/";
        File patentDirectory = new File(path);
        String[] patentDir = patentDirectory.list();
        for (String file : patentDir) {

            String resourcePath = path+file;
            ExtractFromPatent extract = new ExtractFromPatent();
            DocumentContainer docContainer = extract.getContent(resourcePath);
            System.err.println("+++++++++++"+docContainer.getContent());
            docs.add(docContainer);

        }

    }
}
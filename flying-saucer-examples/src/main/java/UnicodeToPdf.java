import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class UnicodeToPdf {
    private static final String doctypePrefix = "<!DOCTYPE html PUBLIC\n" +
                                                "\"-//OPENHTMLTOPDF//DOC XHTML Character Entities Only 1.0//EN\" \"\">\n";


    public static void main(String[] args) throws IOException, DocumentException {
        OutputStream os = new FileOutputStream("unicode.pdf");
        String html = "<html>\n" +
                      "<head>\n" +
                      "<style>\n" +
                      "  body {\n" +
                      "  font-family: \"JetBrains Sans\", \"Noto Emoji\", sans-serif;\n" +
                      "  }\n" +
                      "  </style>\n" +
                      "</head>\n" +
                      "<body>\n" +
                      "<p>🐡🦈my 🐡🦈text</p>\n" +
                      "<p>🐠🐟🐡🦈</p>\n" +
                      "<p>🐠🐟🐡🦈</p>\n" +
                      "<p>@user489041: I disagree: The right way to do this is to compile with java -encoding UTF-8. No\n" +
                      "                mess, no fuss. This is especially because 🐡🦈 20 years on, Java still has no standard way to talk about\n" +
                      "                code points by their official names. That means you are trying to insert evil and mysterious magic\n" +
                      "                numbers in your code. That is not a good thing! Sure, I might rather see \"\\N{GREEK SMALL LETTER ALPHA}\"\n" +
                      "                than \"α\", but I SURELY do not want to see \"\\u03B1\"! That’s just wicked. How are you going to maintain\n" +
                      "                that kind of crudola?</p>\n" +
                      "</body>\n" +
                      "</html>";
        Document document = Jsoup.parse(html, "UTF-8");
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        document.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
        ITextRenderer renderer = getRenderer();
        renderer.setDocumentFromString(doctypePrefix + document.html());
        renderer.layout();
        renderer.createPDF(os);
        os.close();
    }

    private static ITextRenderer getRenderer() throws DocumentException, IOException {
        ITextRenderer renderer = new ITextRenderer();
        renderer.getFontResolver().addFont(
                UnicodeToPdf.class.getClassLoader().getResource("demos/html/JetBrains Sans.ttf").toString(),
                BaseFont.IDENTITY_H,
                BaseFont.NOT_EMBEDDED
        );
        renderer.getFontResolver().addFont(
                UnicodeToPdf.class.getClassLoader().getResource("demos/html/NotoColorEmoji-Regular.ttf").toString(),
                BaseFont.IDENTITY_H,
                BaseFont.NOT_EMBEDDED
        );
        renderer.getFontResolver().addFont(
                UnicodeToPdf.class.getClassLoader().getResource("demos/html/NotoEmoji-VariableFont_wght.ttf").toString(),
                BaseFont.IDENTITY_H,
                BaseFont.NOT_EMBEDDED
        );
        return renderer;
    }
}

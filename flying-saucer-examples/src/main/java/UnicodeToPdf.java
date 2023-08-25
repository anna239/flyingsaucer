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
    private static final String doctypePrefix = """
            <!DOCTYPE html PUBLIC
            "-//OPENHTMLTOPDF//DOC XHTML Character Entities Only 1.0//EN" "">
            """;

    /*
    <p>
                    🐠🐟🐡🦈
                </p>
     */
    public static void main(String[] args) throws IOException, DocumentException {
        OutputStream os = new FileOutputStream("unicode.pdf");
        String html = """
                <html>
                <head>
                <style>
                  body {
                  font-family: "JetBrains Sans", "Noto Emoji", sans-serif;
                  }
                  </style>
                </head>
                <body>
                <p>my text</p>
//                
                </body>
                </html>""";
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

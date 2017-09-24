import com.sun.deploy.util.ArrayUtil;
import org.attoparser.*;
import org.attoparser.config.ParseConfiguration;
import org.attoparser.select.AttributeSelectionMarkingMarkupHandler;
import org.attoparser.simple.AbstractSimpleMarkupHandler;
import org.attoparser.simple.ISimpleMarkupHandler;
import org.attoparser.simple.ISimpleMarkupParser;
import org.attoparser.simple.SimpleMarkupParser;

import java.io.*;
import java.lang.reflect.Array;
import java.util.Map;

/**
 * Created by myuser on 9/19/17.
 */

    public class TagSoup {
    public static void main(String args[]) throws Exception {

            File input = new File("/home/myuser/Downloads/tuik_data/12-09-2017-00:08:35-16881753099321297021824160991.html");//disticaret2009-13_files.html");

            // print the 'src' attributes of <img> tags
            // from http://www.yahoo.com/
            // using the TagSoup parser

            final Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(input), "windows-1254"));


            // Create the handler instance. Extending the no-op AbstractMarkupHandler is a good start

        ISimpleMarkupHandler handler = new AbstractSimpleMarkupHandler() {
                @Override
                public void handleOpenElement(String elementName, Map<String, String> attributes, int line, int col) throws ParseException {
                    super.handleOpenElement(elementName, attributes, line, col);
                    System.out.print(elementName);
                    if(attributes != null)
                    {
                        System.out.print(" "+attributes.values().toString());
                    }
                    if(elementName.trim().equals("tr"))
                        System.out.println();
                }

            @Override
            public void handleText(char[] buffer, int offset, int len, int line, int col) throws ParseException {
                super.handleText(buffer, offset, len, line, col);
                System.out.print(new String(buffer,offset, len));
            }
        };

        // Create or obtain the parser instance (can be reused). Example uses the default configuration for HTML
        final ISimpleMarkupParser parser = new SimpleMarkupParser(ParseConfiguration.htmlConfiguration());

        // Parse it!
        parser.parse(reader, handler);

        }

}


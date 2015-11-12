package han.project.toeic;

import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Han on 08/11/2015.
 */
public class LessonsParser {
    public static List<Representative> parse(InputStream is){
        List<Representative> lessons = null;
        try{
            // create a XMLReader from SAXParser
            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            // create a SAXXMLHandler
            SAXXMLHandler saxHandler = new SAXXMLHandler();
            // store handler in XMLReader
            xmlReader.setContentHandler(saxHandler);
            // the process starts
            xmlReader.parse(new InputSource(is));
            // get the `Employee list`
            lessons = saxHandler.getLessons();
        }catch(Exception ex){
            Log.d("XML", "SAXXMLParser: parse() failed");
        }
        return lessons;
    }
}

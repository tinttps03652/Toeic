package han.project.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import han.project.mode.Representative;

/**
 * Created by Han on 08/11/2015.
 */
public class SAXXMLHandler extends DefaultHandler{
    private List<Representative> lessons;
    private String tempVal;
    private Representative tempLesson;
    public SAXXMLHandler() {
        lessons = new ArrayList<Representative>();
    }

    public List<Representative> getLessons() {
        return lessons;
    }


    // Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // reset
        tempVal = "";
        if (qName.equalsIgnoreCase("lesson")) {
            // create a new instance of lesson
            tempLesson = new Representative();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("lesson")) {
            // add it to the list
            lessons.add(tempLesson);
        } else if (qName.equalsIgnoreCase("title")) {
            tempLesson.setTitle(tempVal);
        } else if (qName.equalsIgnoreCase("meaning")) {
            tempLesson.setMeaning(tempVal);
        } else if (qName.equalsIgnoreCase("image")) {
            tempLesson.setImage(tempVal);
        }
    }

}

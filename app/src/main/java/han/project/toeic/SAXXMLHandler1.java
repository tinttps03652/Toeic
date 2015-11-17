package han.project.toeic;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Han on 08/11/2015.
 */
public class SAXXMLHandler1 extends DefaultHandler{
    private List<WordModel> words;
    private String tempVal;
    private WordModel tempWord;
    public SAXXMLHandler1() {
        words = new ArrayList<WordModel>();
    }

    public List<WordModel> getWords() {
        return words;
    }


    // Event Handlers
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // reset
        tempVal = "";
        if (qName.equalsIgnoreCase("words")) {
            // create a new instance of mobile
            tempWord = new WordModel();
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("words")) {
            // add it to the list
            words.add(tempWord);
        } else if (qName.equalsIgnoreCase("word")) {
            tempWord.setWord(tempVal);
        } else if (qName.equalsIgnoreCase("meaning")) {
            tempWord.setMeaning(tempVal);
        } else if (qName.equalsIgnoreCase("vietnamese")) {
            tempWord.setVietnamese(tempVal);
        } else if (qName.equalsIgnoreCase("image")) {
            tempWord.setImage(tempVal);
        }else if (qName.equalsIgnoreCase("audio")) {
            tempWord.setAudio(tempVal);
        }
    }

}

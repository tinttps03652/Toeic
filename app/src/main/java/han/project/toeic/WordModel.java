package han.project.toeic;

import java.io.Serializable;

/**
 * Created by Han on 11/11/2015.
 */
public class WordModel implements Serializable{
    String word;
    String meaning;
    String vietnamese;
    String image;
    public WordModel(){

    }
    public WordModel(String word, String meaning, String vietnamese, String image) {
        this.word = word;
        this.meaning = meaning;
        this.vietnamese = vietnamese;
        this.image = image;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getVietnamese() {
        return vietnamese;
    }

    public void setVietnamese(String vietnamese) {
        this.vietnamese = vietnamese;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return "WordModel{" +
                "word='" + word + '\'' +
                ", meaning='" + meaning + '\'' +
                ", vietnamese='" + vietnamese + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}

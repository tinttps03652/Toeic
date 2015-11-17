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
    String audio;
    public WordModel(){

    }
    public WordModel(String word, String meaning, String vietnamese, String image,String audio) {
        this.word = word;
        this.meaning = meaning;
        this.vietnamese = vietnamese;
        this.image = image;
        this.audio = audio;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
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

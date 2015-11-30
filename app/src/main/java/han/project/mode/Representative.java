package han.project.mode;

import java.io.Serializable;

public class Representative implements Serializable{
	String meaning;
	String title;
	String image;


	public String getMeaning() {
		return meaning;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Representative() {
		
	}

	public Representative( String title,String meaning, String image) {
		super();
		this.meaning = meaning;
		this.title = title;
		this.image = image;
	}

	@Override
	public String toString() {
		return " [lesson=" + meaning + ", title=" + title + ", image=" + image + "]";
	}

}

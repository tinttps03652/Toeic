package han.project.toeic;

import java.io.Serializable;

public class register implements Serializable{
	public int id;
	public String username;
	public String password;
	public register() {
		
	}
	public register(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public register(int id, String username, String password) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
	}

	@Override
	public String toString() {
		return "register [id=" + id + ", username=" + username + ", password=" + password + "]";
	}

}

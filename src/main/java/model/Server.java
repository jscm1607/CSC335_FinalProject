package model;

public final class Server {
	// INSTANCE VARIABLES
	private int id;
	private String username;
	private String password;

	// CONSTRUCTOR

	public Server() {
	}

	public Server(int id, String username, String password) {
		this.id = id;
		this.username = username;
		this.password = password;
	}

	// GETTERS
	public String getUsername() {
		return username;
	}

	// add a getPassword method
	public String getPassword() {
		return password;
	}

	public int getId() {
		return id;
	}

	// SETTERS
	public void setId(int id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Server{" +
				"id=" + id +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				'}';
	}
}

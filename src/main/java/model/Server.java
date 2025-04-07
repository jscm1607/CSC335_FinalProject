package model;

public final class Server {
	// INSTANCE VARIABLES
	private final String username;
	private final String password;

	// CONSTRUCTOR
	public Server(String username, String password) {
		this.username = username;
		this.password = password;		
	}
    
	// GETTERS
	public String getUsername() {
		return username;
	}

	//add a getPassword method
	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return "Server{" +
				"username='" + username + '\'' +
				", password='" + password + '\'' +
				'}';
	}
}

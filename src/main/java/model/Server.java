package model;

import dao.ServerDAO;

public final class Server {
	private static final ServerDAO dao = new ServerDAO();

	// INSTANCE VARIABLES
	private final int id;
	private final String username;
	private final String password;

	// CONSTRUCTOR

	public Server(int id, String username, String password) {
		this.id = id;
		this.username = username;
		this.password = password;
	}

	public Server(String username, String password) {
		this.username = username;
		this.password = password;
		this.id = dao.insert(this);
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


	public Server setUsername(String username) {
		Server out = new Server(id, username, password);
		dao.update(this);
		return out;
	}

	public Server setPassword(String password) {
		Server out = new Server(id, username, password);
		dao.update(this);
		return out;
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

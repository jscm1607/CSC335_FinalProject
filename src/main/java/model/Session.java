package model;

import java.time.LocalDateTime;

public class Session {
	// INSTANCE VARIABLES
	private LocalDateTime date;
	private Server server;
	private double totalTips;
	private boolean open;
	private int[] tables;
	
	// CONSTRUCTOR
	public Session(LocalDateTime date, Server server, double totalTips, boolean open, int[] tables) {
		super();
		this.date = date;
		this.server = server;
		this.totalTips = totalTips;
		this.open = open;
		this.tables = tables;
	}

	// SETTERS AND GETTERS
	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public double getTotalTips() {
		return totalTips;
	}

	public void setTotalTips(double totalTips) {
		this.totalTips = totalTips;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public int[] getTables() {
		return tables;
	}

	public void setTables(int[] tables) {
		this.tables = tables;
	}
	
}

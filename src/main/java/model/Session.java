package model;

import java.util.Date;

public class Session {
	// INSTANCE VARIABLES
	private Date date;
	private int serverId;
	private double totalTips;
	private boolean open;
	private int id;
	
	public Session(){}

	// CONSTRUCTOR
	public Session(int id, Date date, int serverId, double totalTips, boolean open) {
		super();
		this.id = id;
		this.date = date;
		this.serverId = serverId;
		this.totalTips = totalTips;
		this.open = open;
	}

	// SETTERS AND GETTERS
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getServer() {
		return serverId;
	}

	public void setServer(int serverId) {
		this.serverId = serverId;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Session{date=" + date +
			   ", server=" + serverId +
			   ", totalTips=" + totalTips +
			   ", open=" + open;
	}

	
}

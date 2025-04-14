package model;

import java.util.Date;

import dao.SessionDAO;


public class Session {
	private static final SessionDAO dao = new SessionDAO();

	// INSTANCE VARIABLES
	private final Date date;
	private final int serverId;
	private final double totalTips;
	private final boolean open;
	private final int id;

	// CONSTRUCTOR
	public Session(int id, Date date, int serverId, double totalTips, boolean open) {
		super();
		this.date = date;
		this.serverId = serverId;
		this.totalTips = totalTips;
		this.open = open;
		this.id = id;
	}

	// CONSTRUCTOR
	public Session(Date date, int serverId, double totalTips, boolean open) {
		super();
		this.date = date;
		this.serverId = serverId;
		this.totalTips = totalTips;
		this.open = open;
		this.id = dao.insert(this);
	}

	// SETTERS AND GETTERS
	public Date getDate() {
		return date;
	}

	public Session setDate(Date date) {
		Session out = new Session(id, date, serverId, totalTips, open);
		dao.update(out);
		return out;
	}

	public int getServer() {
		return serverId;
	}

	public Session setServer(int serverId) {
		Session out = new Session(id, date, serverId, totalTips, open);
		dao.update(out);
		return out;
	}

	public double getTotalTips() {
		return totalTips;
	}

	public Session setTotalTips(double totalTips) {
		Session out = new Session(id, date, serverId, totalTips, open);
		dao.update(out);
		return out;
	}

	public boolean isOpen() {
		return open;
	}

	public Session setOpen(boolean open) {
		Session out = new Session(id, date, serverId, totalTips, open);
		dao.update(out);
		return out;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Session{date=" + date +
			   ", server=" + serverId +
			   ", totalTips=" + totalTips +
			   ", open=" + open;
	}

	
}

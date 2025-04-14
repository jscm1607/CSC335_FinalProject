package model;

import dao.OrderDAO;

public class Order {
	private static final OrderDAO dao = new OrderDAO();

	// INSTANCE VARIABLES
	private final int id;
	private final boolean closed;
	private final int tableNumber;
	private final double tip;
	private final int sessionId;
	
	// CONSTRUCTOR
	public Order(int id, boolean closed, int tableNumber, double tip, int sessionId) {
		super();
		this.id = id;
		this.closed = closed;
		this.tableNumber = tableNumber;
		this.tip = tip;
		this.sessionId = sessionId;
	}

	public Order(boolean closed, int tableNumber, double tip, int sessionId) {
		super();
		this.closed = closed;
		this.tableNumber = tableNumber;
		this.tip = tip;
		this.sessionId = sessionId;
		this.id = dao.insert(this);
	}

	// SETTERS AND GETTERS
	public int getId() {
		return id;
	}

	public boolean isClosed() {
		return closed;
	}

	public Order setClosed(boolean closed) {
		Order out = new Order(id, closed, tableNumber, tip, sessionId);
		dao.update(out);
		return out;
	}

	public int getTableNumber() {
		return tableNumber;
	}

	public Order setTableNumber(int tableNumber) {
		Order out = new Order(id, closed, tableNumber, tip, sessionId);
		dao.update(out);
		return out;
	}

	public double getTip() {
		return tip;
	}

	public Order setTip(double tip) {
		Order out = new Order(id, closed, tableNumber, tip, sessionId);
		dao.update(out);
		return out;
	}

	public int getSessionId() {
		return sessionId;
	}

	public Order setSessionId(int sessionId) {
		Order out = new Order(id, closed, tableNumber, tip, sessionId);
		dao.update(out);
		return out;
	}
	
	@Override
	public String toString() {
		return "Order{id=" + id +
			   ", closed=" + closed +
			   ", tableNumber=" + tableNumber +
			   ", tip=" + tip +
			   ", sessionId=" + sessionId + '}';
	}

	
	
}

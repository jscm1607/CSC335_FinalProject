package model;

public class Order {
	// INSTANCE VARIABLES
	private int id;
	private boolean closed;
	private int tableNumber;
	private double tip;
	
	// CONSTRUCTOR
	public Order(int id, boolean closed, int tableNumber, double tip) {
		super();
		this.id = id;
		this.closed = closed;
		this.tableNumber = tableNumber;
		this.tip = tip;
	}

	// SETTERS AND GETTERS
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public int getTableNumber() {
		return tableNumber;
	}

	public void setTableNumber(int tableNumber) {
		this.tableNumber = tableNumber;
	}

	public double getTip() {
		return tip;
	}

	public void setTip(double tip) {
		this.tip = tip;
	}
	
	@Override
	public String toString() {
	    return "Order{id=" + id +
	           ", closed=" + closed +
	           ", tableNumber=" + tableNumber +
	           ", tip=" + tip + '}';
	}

	
	
}

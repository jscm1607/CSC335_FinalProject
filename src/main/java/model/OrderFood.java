package model;

public class OrderFood {
	// INSTANCE VARIABLES
	private int seat;
	private int quantity;
	private Food food;
	private Order order;
	private String[] modifications;
	
	// CONSTRUCTOR
	public OrderFood(int seat, int quantity, Food food, Order order, String[] modifications) {
		super();
		this.seat = seat;
		this.quantity = quantity;
		this.food = food;
		this.order = order;
		this.modifications = modifications;
	}

	// SETTERS AND GETTERS
	public int getSeat() {
		return seat;
	}

	public void setSeat(int seat) {
		this.seat = seat;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String[] getModifications() {
		return modifications;
	}

	public void setModifications(String[] modifications) {
		this.modifications = modifications;
	}
	
}

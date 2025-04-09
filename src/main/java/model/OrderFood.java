package model;

public class OrderFood {
	// INSTANCE VARIABLES
	private int id;
	private int seat;
	private int quantity;
	private int foodId;
	private int orderId;
	private String[] modifications;
	
	// CONSTRUCTOR
	public OrderFood() {}
	public OrderFood(int id, int seat, int quantity, int foodId, int orderId, String[] modifications) {
		super();
		this.id = id;
		this.seat = seat;
		this.quantity = quantity;
		this.foodId = foodId;
		this.orderId = orderId;
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

	public int getFoodId() {
		return foodId;
	}

	public void setFoodId(int foodId) {
		this.foodId = foodId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String[] getModifications() {
		return modifications;
	}

	public void setModifications(String[] modifications) {
		this.modifications = modifications;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
	    return "OrderFood{id=" + id +
	           ",seat=" + seat +
	           ", quantity=" + quantity +
	           ", food=" + foodId +
	           ", orderId=" + orderId +
	           ", modifications=" + (modifications != null ? String.join(", ", modifications) : "[]") + '}';
	}

}

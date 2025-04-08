package model;

public class Food {
	// Category enum
	public enum Category {
		BURGERS, FRIES, SHAKES, BEVERAGES;
	}

	// INSTANCE VARIABLES
	private int id;	// database id
	private String name;
	private Category category;
	private double cost;
	private boolean inStock;
	private int numOrders;
	
	// CONSTRUCTOR
	public Food() {}
	
	public Food(int id, String name, Category category, double cost, boolean inStock, int numOrders) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.cost = cost;
		this.inStock = inStock;
		this.numOrders = numOrders;
	}

	// SETTERS AND GETTERS
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public boolean isInStock() {
		return inStock;
	}

	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}

	public int getNumOrders() {
		return numOrders;
	}

	public void setNumOrders(int numOrders) {
		this.numOrders = numOrders;
	}
	
	@Override
	public String toString() {
	    return "Food{id=" + id +
	           ", name='" + name + '\'' +
	           ", category=" + category +
	           ", cost=" + cost +
	           ", inStock=" + inStock +
	           ", numOrders=" + numOrders + '}';
	}

	
}

package model;

import dao.FoodDAO;

public class Food {
	private static final FoodDAO dao = new FoodDAO();

	// Category enum
	public enum Category {
		BURGERS, FRIES, SHAKES, BEVERAGES;
	}

	// INSTANCE VARIABLES
	private final int id;	// database id
	private final String name;
	private final Category category;
	private final double cost;
	private final boolean inStock;
	
	// CONSTRUCTOR
	public Food(int id, String name, Category category, double cost, boolean inStock) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.cost = cost;
		this.inStock = inStock;
	}
	
	public Food(String name, Category category, double cost, boolean inStock) {
		this.name = name;
		this.category = category;
		this.cost = cost;
		this.inStock = inStock;
		this.id = dao.insert(this);
	}

	// SETTERS AND GETTERS
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Food setName(String name) {
		Food out = new Food(id, name, category, cost, inStock);
		dao.update(out);
		return out;
	}

	public Category getCategory() {
		return category;
	}

	public Food setCategory(Category category) {
		Food out = new Food(id, name, category, cost, inStock);
		dao.update(out);
		return out;
	}

	public double getCost() {
		return cost;
	}

	public Food setCost(double cost) {
		Food out = new Food(id, name, category, cost, inStock);
		dao.update(out);
		return out;
	}

	public boolean isInStock() {
		return inStock;
	}

	public Food setInStock(boolean inStock) {
		Food out = new Food(id, name, category, cost, inStock);
		dao.update(out);
		return out;
	}
	
	@Override
	public String toString() {
		return "Food{id=" + id +
			   ", name='" + name + '\'' +
			   ", category=" + category +
			   ", cost=" + cost +
			   ", inStock=" + inStock + '}';
	}
}

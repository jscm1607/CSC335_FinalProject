/* OrderFood.java
 * This class represents an OrderFood item in the system.
 * A order food object involves final variables, including the item id.
 * Other variables include seat, quantity, foodId, orderId and modifications.
 * */

package backend;

public class OrderFood {
	private static final OrderFoodDAO dao = new OrderFoodDAO();

	// INSTANCE VARIABLES
	private final int id;
	private final int seat;
	private final int quantity;
	private final int foodId;
	private final int orderId;
	private final String[] modifications;
	
	// CONSTRUCTOR
	public OrderFood(int id, int seat, int quantity, int foodId, int orderId, String[] modifications) {
		super();
		this.id = id;
		this.seat = seat;
		this.quantity = quantity;
		this.foodId = foodId;
		this.orderId = orderId;
		this.modifications = modifications;
	}

	public OrderFood(int seat, int quantity, int foodId, int orderId, String[] modifications) {
		super();
		this.seat = seat;
		this.quantity = quantity;
		this.foodId = foodId;
		this.orderId = orderId;
		this.modifications = modifications;
		this.id = dao.insert(this);
	}

	// SETTERS AND GETTERS
	public int getSeat() {
		return seat;
	}

	public OrderFood setSeat(int seat) {
		OrderFood out = new OrderFood(id, seat, quantity, foodId, modifications);
		dao.update(out);
		return out;
	}

	public int getQuantity() {
		return quantity;
	}

	public OrderFood setQuantity(int quantity) {
		OrderFood out = new OrderFood(id, seat, quantity, foodId, modifications);
		dao.update(out);
		return out;
	}

	public int getFoodId() {
		return foodId;
	}

	public OrderFood setFoodId(int foodId) {
		OrderFood out = new OrderFood(id, seat, quantity, foodId, modifications);
		dao.update(out);
		return out;
	}

	public int getOrderId() {
		return orderId;
	}

	public OrderFood setOrderId(int orderId) {
		OrderFood out = new OrderFood(id, seat, quantity, foodId, modifications);
		dao.update(out);
		return out;
	}

	public String[] getModifications() {
		return modifications;
	}

	public OrderFood setModifications(String[] modifications) {
		OrderFood out = new OrderFood(id, seat, quantity, foodId, modifications);
		dao.update(out);
		return out;
	}

	public int getId() {
		return id;
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

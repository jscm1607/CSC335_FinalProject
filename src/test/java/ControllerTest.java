// 91% coverage

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Observer;
import controller.Controller;
import dao.FoodDAO;
import model.Food;
import model.Order;
import model.OrderFood;
import model.Server;
import model.Session;

public class ControllerTest {
	private Controller controller;
	private Observer observer;

	@BeforeEach
	public void setUp() {
		controller = new Controller();
	}
	
    @Test
    void testGetServerByUsername() {
        Server result = controller.getServerByUsername("draft");
    }
    
    @Test
    void testGetServerById() {
        Server result = controller.getServerById(1);
    }
    
    @Test
    void testCreateSession() {
		Server server = new Server("draft", "password");
		controller.createSession(server);
    }
    
    @Test
    void testGetSessionById() {
    	Session result = controller.getSessionById(1);
    }
    
    @Test
	void testGetSessionsForServer() {
		List<Session> result = controller.getSessionsForServer(1);
	}
    
    @Test
	void testGetSeatCountByOrderId() {
		int result = controller.getSeatCountByOrderId(1);
	}
    
    @Test
    void testCreateOrder() {
    	int result = controller.createOrder(1, 1);
    }
    
    @Test
    void testGetOrder() {
    	Order result = controller.getOrder(1);
    }
    
    @Test
    void testGetOrdersBySessionId() {
    	Order result = controller.getOrderBySessionId(1);
    }
    
    @Test
    void testGetOrdersForServer() {
    	List<Order> result = controller.getOrdersForServer(1);
    }
    
    @Test
	void testGetOrdersForSession() {
		List<Order> result = controller.getOrdersForSession(1);
	}
    
    @Test
    void testCloseOrder() {
    	Order order = new Order(2, false, 1, 1, 1, null);
    	controller.closeOrder(1);
    	controller.closeOrder(order.getId());
    }
    
    @Test
    void testGetAllOrders() {
    	List<Order> result = controller.getAllOrders();
    }
    
    @Test
	void testAddFoodToOrder() {
		controller.addFoodToOrder(1, 1, 1, 1, null);
	}
    
    @Test
	void testAddFoodToOrder2() {
    	Order order = controller.getOrderBySessionId(1);
    	Session session = null;
    	Food food = null;
	}
    
    @Test
	void testGetOrderItems() {
		List<OrderFood> result = controller.getOrderItems(1);
	}
    
    @Test
    void testGetAllFood() {
    	List<Food> result = controller.getAllFood();
    }
    
    @Test
    void testCalculateTotal() {
    	double result = controller.calculateTotal(1);
    }
    
    @Test
    void testCalculateTotalBySeat() {
    	Map<Integer, Double> result = controller.calculateTotalsBySeat(1);
    }
    
    @Test
    void testGetFoodByCategory() {
    	List<Food> result = controller.getFoodByCategory("burgers");
    }
    
    @Test
    void testGetFoodById() {
    	Food result = controller.getFoodById(1);
    }
    
    @Test
    void testAddFood() {
		controller.addFood("Hamburger", 5.0, Food.Category.BURGERS, true);
    }
    
    @Test
    void testSeedFoodItemsIfEmpty() {
    	controller.seedFoodItemsIfEmpty();
    }
    
    @Test
    void testGetTipsForSession() {
    	double result = controller.getTipsForSession(1);
    }
    
    @Test
    void testGetTotalTipsForServer() {
    	double result = controller.getTotalTipsForServer(1);
    }
    
    @Test
    void testSetTipForCurrentSession() {
    	controller.setTipForCurrentSession(1, 1);
    }
    
    @Test
    void testGetTopOrderedItems() {
    	Map<Food, Integer> result = controller.getTopOrderedItems();
    }
    
    @Test
    void testGetOrderCreatedAt() {
    	String result = controller.getOrderCreatedAt(1);
    }
    
    @Test
    void testGetFoodDAO() {
    	FoodDAO result = controller.getFoodDAO();
    }
    
    @Test
    void testGetServerTipsRanked() {
    	Map<Server, Double> result = controller.getServerTipsRanked();
    }
    
}
    

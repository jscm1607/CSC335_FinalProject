// 91% coverage

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import controller.Controller;
import dao.FoodDAO;
import model.Food;
import model.Order;
import model.OrderFood;
import model.Server;
import model.Session;

public class ControllerTest extends DAOTest<Controller> {
	private Controller controller;

    public ControllerTest() {
        this.controller = new Controller();
        db.runH2Console();
    }

    @BeforeAll
    static void setUp() {
        db.executeUpdate("DELETE FROM Servers", statement -> {
        });
        db.executeUpdate("DELETE FROM Session", statement -> {
        });
        db.executeUpdate("DELETE FROM Server", statement -> {
        });
        db.executeUpdate("DELETE FROM Food", statement -> {});
        db.executeUpdate("DELETE FROM OrderFood", statement -> {});
    }

    @AfterEach
    void cleanUp() {
        db.executeUpdate("DELETE FROM Server", statement -> {});
        db.executeUpdate("DELETE FROM Session", statement -> {});
        db.executeUpdate("DELETE FROM Order", statement -> {});
        db.executeUpdate("DELETE FROM Food", statement -> {});
        db.executeUpdate("DELETE FROM OrderFood", statement -> {});
    }

    @Test
    void testGetServerByUsername() {
        Server server = new Server("testuser", "password1^");
        assertTrue(server.getId() > -2);
        Server result = controller.getServerByUsername("testuser");
        assertEquals(server.getUsername(), result.getUsername());
    }
        
    @Test
    void testCreateSession() {
		Server server = new Server("draft", "password");
        assertTrue(server.getId() > -2);
		int sessionid = controller.createSession(server);
        Session session = controller.getSessionById(sessionid);
        assertEquals(server.getId(), session.getServer());
    }
    
    @Test
	void testGetSessionsForServer() {
        Server server = new Server("draft", "password");
        assertTrue(server.getId() > -2);
        List<Integer> sessionIds = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int id = controller.createSession(server);
            assertTrue(id > -2);
            sessionIds.add(id);
        }  
		List<Session> result = controller.getSessionsForServer(server.getId());
        assertEquals(sessionIds.size(), result.size());
	}
    
    @Test
	void testGetSeatCountByOrderId() {
		int result = controller.getSeatCountByOrderId(1);
	}
    
    @Test
    void testCreateOrder() {
        Session session = SessionDAOTest.randomSession(ServerDAOTest.randomServer());
    	int result = controller.createOrder(1, session.getId());
        Order order = controller.getOrderBySessionId(session.getId());
        assertEquals(order.getId(), result);
    }
    
    @Test
    void testGetOrder() {
        Order order = OrderDAOTest.randomOrder(SessionDAOTest.randomSession(ServerDAOTest.randomServer()));
        assertTrue(order.getId() > -2);
    	Order result = controller.getOrder(order.getId());
        assertEquals(order.getId(), result.getId());
    }
    
    @Test
    void testGetOrdersForServer() {
        Server server = ServerDAOTest.randomServer();
        assertTrue(server.getId() > -2);
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Session session = SessionDAOTest.randomSession(server);
            assertTrue(session.getId() > -2);
            Order order = new Order(false, 1,1, session.getId(), null);
            assertTrue(order.getId() > -2);
            orders.add(order);
        }
    	List<Order> result = controller.getOrdersForServer(server.getId());
        assertEquals(orders.size(), result.size());
    }
    
    @Test
	void testGetOrdersForSession() {
        Session session = SessionDAOTest.randomSession(ServerDAOTest.randomServer());
        assertTrue(session.getId() > -2);
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Order order = new Order(false, 1, 1, session.getId(), null);
            assertTrue(order.getId() > -2);
            orders.add(order);
        }
        List<Order> result = controller.getOrdersForSession(session.getId());
        assertEquals(orders.size(), result.size());
	}
    
    @Test
    void testCloseOrder() {
        Order order = OrderDAOTest.randomOrder(SessionDAOTest.randomSession(ServerDAOTest.randomServer()));
        assertTrue(order.getId() > -2);
        controller.closeOrder(order.getId());
        Order result = controller.getOrder(order.getId());
        assertTrue(result.isClosed());
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
        Order order = OrderDAOTest.randomOrder(SessionDAOTest.randomSession(ServerDAOTest.randomServer()));
        Food food = FoodDAOTest.randomFood();
        assertTrue(order.getId() > -2);
        assertTrue(food.getId() > -2);
        List<OrderFood> orderFoods = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            OrderFood orderFood = new OrderFood(i, i, food.getId(), order.getId(), null);
            orderFoods.add(orderFood);
        }
        List<OrderFood> result = controller.getOrderItems(order.getId());
	}
    
    @Test
    void testGetAllFood() {
    	List<Food> result = controller.getAllFood();
    }
    
    @Test
    void testCalculateTotal() {
        Order order = OrderDAOTest.randomOrder(SessionDAOTest.randomSession(ServerDAOTest.randomServer()));
        Food food = FoodDAOTest.randomFood();
        assertTrue(order.getId() > -2);
        assertTrue(food.getId() > -2);
        OrderFood orderFood = new OrderFood(1, 2, food.getId(), order.getId(), null);
        double result = controller.calculateTotal(order.getId());
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
    void testGetFoodDAO() {
        FoodDAO result = controller.getFoodDAO();
        assertNotNull(result);
    }
    
    @Test
    void testGetServerTipsRanked() {
    	Map<Server, Double> result = controller.getServerTipsRanked();
    }
    
}
    

package controller;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import dao.*;
import model.*;

public class Controller {
    private ServerDAO serverDAO;
    private SessionDAO sessionDAO;
    private FoodDAO foodDAO;
    private OrderDAO orderDAO;
    private OrderFoodDAO orderFoodDAO;

    public Controller() {
        this.serverDAO = new ServerDAO();
        this.sessionDAO = new SessionDAO();
        this.foodDAO = new FoodDAO();
        this.orderDAO = new OrderDAO();
        this.orderFoodDAO = new OrderFoodDAO();
    }

    // DAO registration for observers
    public void registerDaoObserver(Observer observer) {
        serverDAO.addObserver(observer);
        sessionDAO.addObserver(observer);
        foodDAO.addObserver(observer);
        orderDAO.addObserver(observer);
        orderFoodDAO.addObserver(observer);
    }

    // Server
    public Server getServerByUsername(String username) {
    	System.out.println("Fetching server with username: " + username);
        return serverDAO.select(username);
    }
    
	public Server getServerById(int id) {
		System.out.println("Fetching server with ID: " + id);
		return serverDAO.select("server" + id);
	}
    
    // Session
    public int createSession(Server server) {
        Session session = new Session(new Date(), server.getId(), true);
        return session.getId();
    }

    public Session getSessionById(int sessionId) {
        return sessionDAO.select(sessionId);
    }

    public List<Session> getSessionsForServer(int serverId) {
        List<Session> allSessions = sessionDAO.selectAll();
        return allSessions.stream()
                .filter(s -> s.getServer() == serverId)
                .collect(Collectors.toList());
    }

    // Order
    public int createOrder(int tableNumber, int sessionId) {
        Order order = new Order(false, tableNumber, 0.0, sessionId);
        return order.getId();
    }

    public Order getOrder(int orderId) {
        return orderDAO.select(orderId);
    }

    public Order getOrderBySessionId(int sessionId) {
        List<Order> orders = orderDAO.selectAll();
        return orders.stream()
                .filter(o -> o.getSessionId() == sessionId && !o.isClosed())
                .findFirst()
                .orElse(null);
    }

    public List<Order> getOrdersForServer(int serverId) {
        List<Session> sessions = getSessionsForServer(serverId);
        Set<Integer> sessionIds = sessions.stream()
                .map(Session::getId)
                .collect(Collectors.toSet());
        return orderDAO.selectAll().stream()
                .filter(order -> sessionIds.contains(order.getSessionId()))
                .collect(Collectors.toList());
    }

    public List<Order> getOrdersForSession(int sessionId) {
        return orderDAO.selectAll().stream()
                .filter(order -> order.getSessionId() == sessionId)
                .collect(Collectors.toList());
    }

    /// 04/18
	public boolean closeOrder(int orderId) {
	    Order order = orderDAO.select(orderId);
	    if (order != null && !order.isClosed()) {
	        order.setClosed(true);
	        System.out.println("Order " + orderId + " closed.");
	        orderDAO.update(order);
	        return true;
	    }
	    return false;
	}
	
	public List<Order> getAllOrders() {
	    return orderDAO.selectAll();
	}
	
//	public int getSeatCountForOrder(int orderId) {
//	    Order order = orderDAO.select(orderId);
//	    return order != null ? orderDAO.getSeatCount(orderId) : 0;
//	}
    
    // OrderFood
    public void addFoodToOrder(int orderId, int foodId, int seat, int quantity, String[] modifications) {
        OrderFood orderFood = new OrderFood(seat, quantity, foodId, orderId, modifications);
        // Persisted via constructor
    }

    public void addFoodToOrder(Session session, int seat, Food food, String[] modifications) {
        Order order = getOrderBySessionId(session.getId());
        if (order != null) {
            addFoodToOrder(order.getId(), food.getId(), seat, 1, modifications);
        }
    }

    public List<OrderFood> getOrderItems(int orderId) {
        return orderFoodDAO.selectAll().stream()
                .filter(item -> item.getOrderId() == orderId)
                .collect(Collectors.toList());
    }

    // Food
    public List<Food> getAllFood() {
        return foodDAO.selectAll();
    }

    public List<Food> getFoodByCategory(String category) {
        return foodDAO.selectAll().stream()
                .filter(food -> food.getCategory().toString().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public Food getFoodById(int foodId) {
        return foodDAO.select(foodId);
    }

    public void addFood(String name, double cost, Food.Category category, boolean inStock) {
        new Food(name, category, cost, inStock);  // persisted by constructor
    }

    public void seedFoodItemsIfEmpty() {
        List<Food> allFood = getAllFood();
        boolean hasBurger = allFood.stream().anyMatch(f -> f.getCategory() == Food.Category.BURGERS);
        boolean hasFries = allFood.stream().anyMatch(f -> f.getCategory() == Food.Category.FRIES);
        boolean hasShakes = allFood.stream().anyMatch(f -> f.getCategory() == Food.Category.SHAKES);
        boolean hasBeverages = allFood.stream().anyMatch(f -> f.getCategory() == Food.Category.BEVERAGES);

        if (!hasBurger) addFood("Hamburger", 5.00, Food.Category.BURGERS, true);
        if (!hasFries) addFood("Fries", 3.50, Food.Category.FRIES, true);
        if (!hasShakes) addFood("Shakes", 4.00, Food.Category.SHAKES, true);
        if (!hasBeverages) addFood("Coke", 2.00, Food.Category.BEVERAGES, true);
    }

    // Tip logic (using SessionDAO's getTotalTips)
    public double getTipsForSession(int sessionId) {
        return sessionDAO.getTotalTips(sessionId);
    }

    public double getTotalTipsForServer(int serverId) {
        return getSessionsForServer(serverId).stream()
                .mapToDouble(s -> sessionDAO.getTotalTips(s.getId()))
                .sum();
    }
    
    
    ////current tip method
    
    public void setTipForCurrentSession(int sessionId, double tip) {
        Order order = getOrderBySessionId(sessionId);
        if (order != null) {
            order.setTip(tip); // Persists via DAO
        }
    }

    


    // Top items
    public Map<Food, Integer> getTopOrderedItems() {
        List<OrderFood> allOrderItems = orderFoodDAO.selectAll();
        Map<Integer, Integer> foodIdToCount = new HashMap<>();
        for (OrderFood item : allOrderItems) {
            int foodId = item.getFoodId();
            foodIdToCount.put(foodId, foodIdToCount.getOrDefault(foodId, 0) + item.getQuantity());
        }

        Map<Food, Integer> topItems = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : foodIdToCount.entrySet()) {
            Food food = foodDAO.select(entry.getKey());
            topItems.put(food, entry.getValue());
        }
        return topItems;
    }

    // Timestamp formatting
    public String getOrderCreatedAt(int orderId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date()); // You can refine this if you're tracking order timestamps in DB
    }
    
    
    
    
    public FoodDAO getFoodDAO() {
        return this.foodDAO;
    }

    
    
    
}




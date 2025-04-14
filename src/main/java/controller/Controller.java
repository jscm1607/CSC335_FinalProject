//package controller;

//import dao.DBM;

/*public class Controller {
    public static void main(String[] args) {
        (new DBM()).runH2Console();
        // TODO implement app
    }
}*/


package controller;

import java.util.Date;
import java.util.List;

import javax.swing.SwingUtilities;

import dao.*;
import model.*;

// coordinate ui-dao relationship

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
    
    // Server methods
    public Server getServerByUsername(String username) {
        return serverDAO.select(username);
    }
    
    // Session methods
    public int createSession(Server server) {
        Session session = new Session(new Date(), server.getId(), 0.0, true);
        return sessionDAO.insert(session);
    }
    
    // Food methods
    public List<Food> getAllFood() {
        return foodDAO.selectAll();
    }
    
    public List<Food> getFoodByCategory(String category) {
        List<Food> allFood = foodDAO.selectAll();
        
        System.out.println("Available food in DB:");
        for (Food food : allFood) {
            System.out.println("Name: " + food.getName() + " | Category: " + food.getCategory());
        }
        
        
        return allFood.stream()
                .filter(food -> food.getCategory().toString().equalsIgnoreCase(category))
                .toList();
    }
    
    // Order methods
    public int createOrder(int tableNumber, int sessionId) {
        Order order = new Order(0, false, tableNumber, 0.0, sessionId);
        return orderDAO.insert(order);
    }
    
    public Order getOrder(int orderId) {
        return orderDAO.select(orderId);
    }
    
    // OrderFood methods
    public void addFoodToOrder(int orderId, int foodId, int seat, int quantity, String[] modifications) {
        OrderFood orderFood = new OrderFood(0, seat, quantity, foodId, orderId, modifications);
        orderFoodDAO.insert(orderFood);
    }
    
    public List<OrderFood> getOrderItems(int orderId) {
        List<OrderFood> allItems = orderFoodDAO.selectAll();
        return allItems.stream()
                .filter(item -> item.getOrderId() == orderId)
                .toList();
    }
    
    // Utility methods
    public Food getFoodById(int foodId) {
        return foodDAO.select(foodId);
    }
    
    public static void main(String[] args) {
        (new DBM()).runH2Console();
       //SwingUtilities.invokeLater(LoginFrame::new);
    }
}
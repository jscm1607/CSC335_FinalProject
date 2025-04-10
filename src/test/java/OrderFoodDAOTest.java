import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import dao.FoodDAO;
import dao.OrderDAO;
import dao.OrderFoodDAO;
import model.Food;
import model.Order;
import model.OrderFood;

@Testable
public class OrderFoodDAOTest extends DAOTest<OrderFoodDAO> {
    private final OrderDAO orderDao;
    private final FoodDAO foodDao;
    private final OrderDAOTest orderDaoTest;
    private final FoodDAOTest foodDaoTest;

    public OrderFoodDAOTest() {
        this.dao = new OrderFoodDAO(db);
        this.orderDao = new OrderDAO(db);
        this.foodDao = new FoodDAO(db);
        this.orderDaoTest = new OrderDAOTest();
        this.foodDaoTest = new FoodDAOTest();
    }

    private OrderFood randomOrderFood(Food food, Order order) {
        return new OrderFood((int) (Math.random() * 10), (int) (Math.random() * 10),
                food.getId(),order.getId(),
                new String[] { "Extra cheese", "No onions" });
    }

    private OrderFood randomValidOrderFood() {
        Food food = foodDaoTest.randomFood();
        assertTrue(food.getId() >= 0, "Should be valid Food SQL insert");
        Order order = orderDaoTest.randomValidOrder();
        assertTrue(order.getId() >= 0, "Should be valid Order SQL insert");

        // Create OrderFood with valid foreign keys
        OrderFood of = randomOrderFood(food, order);
        return of;
    }

    @BeforeAll
    static void setUp() {
        db.executeUpdate("DELETE FROM OrderFood", statement -> {
        });
        db.executeUpdate("DELETE FROM Orders", statement -> {
        });
        db.executeUpdate("DELETE FROM Food", statement -> {
        });
        db.executeUpdate("DELETE FROM Server", statement -> {
        });
        db.executeUpdate("DELETE FROM Session", statement -> {
        });
    }

    @AfterEach
    void cleanUp() {
        db.executeUpdate("DELETE FROM OrderFood", statement -> {
        });
        db.executeUpdate("DELETE FROM Orders", statement -> {
        });
        db.executeUpdate("DELETE FROM Food", statement -> {
        });
        db.executeUpdate("DELETE FROM Server", statement -> {
        });
        db.executeUpdate("DELETE FROM Session", statement -> {
        });
    }

    @Test
    void testOrderFoodInsertBadForeignKey() {
        OrderFood of = randomOrderFood(foodDaoTest.randomFood(), orderDaoTest.randomValidOrder());
        of = of.setFoodId(99999); // Invalid foodId
        of = of.setOrderId(99999); // Invalid orderId
        assertFalse(of.getId() > 0, "SQL insert should fail with invalid foreign keys");
    }

    @Test
    void testOrderFoodInsert() {
        // Create OrderFood with valid foreign keys
        OrderFood of = randomValidOrderFood();
        assertTrue(of.getId() > 0, "Should be valid OrderFood SQL insert");
    }

    @Test
    void testOrderFoodSelect() {
        OrderFood of = randomValidOrderFood();
        assertTrue(of.getId() > 0, "Should be valid OrderFood SQL insert");
        OrderFood res = dao.select(of.getId());
        assertEquals(of.getId(), res.getId());
        assertEquals(of.toString(), res.toString());
    }

    @Test
    void testOrderFoodSelectEmpty() {
        List<OrderFood> res = dao.selectAll();
        assertEquals(0, res.size());
    }

    @Test
    void testOrderFoodSelectAll() {
        List<OrderFood> orderFoods = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            OrderFood of = randomValidOrderFood();
            orderFoods.add(of);
        }
        List<OrderFood> res = dao.selectAll();
        assertEquals(10, res.size());
        orderFoods.sort((o1, o2) -> Integer.compare(o1.getId(), o2.getId()));
        res.sort((o1, o2) -> Integer.compare(o1.getId(), o2.getId()));
        assertEquals(orderFoods.toString(), res.toString());
    }

    @Test
    void testOrderFoodUpdate() {
        OrderFood of = randomValidOrderFood();
        assertTrue(of.getId() >= 0);

        // Update OrderFood
        of.setSeat(of.getSeat() + 1);
        of.setQuantity(of.getQuantity() + 1);
        of.setModifications(new String[] { "No salt" });
        // Verify update
        OrderFood res = dao.select(of.getId());
        assertNotNull(res);
        assertEquals(of.toString(), res.toString());
    }

    @Test
    void testOrderFoodDelete() {
        OrderFood of = randomValidOrderFood();
        assertTrue(of.getId() >= 0, "Should be valid OrderFood SQL insert");
        assertEquals(1, dao.selectAll().size(), "Should be 1 OrderFood in DB");
        dao.delete(of.getId());
        assertEquals(0, dao.selectAll().size(), "Should be 0 OrderFood in DB");
        assertNull(dao.select(of.getId()), "Should return null after delete");
    }

    @Test
    void testOrderFoodSelectNonExistent() {
        assertNull(dao.select(99999), "Should return null for non-existent OrderFood");
    }
}

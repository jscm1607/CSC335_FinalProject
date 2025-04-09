import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
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
        this.dao = new OrderFoodDAO();
        this.orderDao = new OrderDAO();
        this.foodDao = new FoodDAO();
        this.orderDaoTest = new OrderDAOTest();
        this.foodDaoTest = new FoodDAOTest();
    }

    private OrderFood randomOrderFood() {
        OrderFood of = new OrderFood();
        of.setSeat((int) (Math.random() * 10));
        of.setQuantity((int) (Math.random() * 5) + 1);
        of.setModifications(new String[] { "Extra cheese", "No onions" });
        return of;
    }

    private OrderFood randomValidOrderFood() {
        Food food = foodDaoTest.randomFood();
        food.setId(foodDao.insert(food, db));
        assertTrue(food.getId() >= 0, "Should be valid Food SQL insert");
        Order order = orderDaoTest.randomValidOrder();
        order.setId(orderDao.insert(order, db));
        assertTrue(order.getId() >= 0, "Should be valid Order SQL insert");

        // Create OrderFood with valid foreign keys
        OrderFood of = randomOrderFood();
        of.setFoodId(food.getId());
        of.setOrderId(order.getId());
        return of;
    }

    @BeforeAll
    static void setUp() {
        db.executeUpdate("DELETE FROM OrderFood", statement -> {});
        db.executeUpdate("DELETE FROM Orders", statement -> {});
        db.executeUpdate("DELETE FROM Food", statement -> {});
        db.executeUpdate("DELETE FROM Server", statement -> {});
        db.executeUpdate("DELETE FROM Session", statement -> {});
    }

    @AfterEach
    void cleanUp() {
        db.executeUpdate("DELETE FROM OrderFood", statement -> {});
        db.executeUpdate("DELETE FROM Orders", statement -> {});
        db.executeUpdate("DELETE FROM Food", statement -> {});
        db.executeUpdate("DELETE FROM Server", statement -> {});
        db.executeUpdate("DELETE FROM Session", statement -> {});
    }

    @Test
    void testOrderFoodInsertBadForeignKey() {
        OrderFood of = randomOrderFood();
        of.setFoodId(99999); // Invalid foodId
        of.setOrderId(99999); // Invalid orderId
        of.setId(dao.insert(of, db));
        assertFalse(of.getId() > 0, "SQL insert should fail with invalid foreign keys");    
    }

    @Test
    void testOrderFoodInsert() {
        // Create OrderFood with valid foreign keys
        OrderFood of = randomValidOrderFood();
        of.setId(dao.insert(of, db));
        assertTrue(of.getId() > 0, "Should be valid OrderFood SQL insert");
    }

    @Test
    void testOrderFoodSelect() {
        OrderFood of = randomValidOrderFood();
        of.setId(dao.insert(of, db));
        assertTrue(of.getId() > 0, "Should be valid OrderFood SQL insert");
        OrderFood res = dao.select(of.getId(), db);
        assertEquals(of.getId(), res.getId());
        assertEquals(of.toString(), res.toString());
    }

    @Test
    void testOrderFoodSelectEmpty() {
        List<OrderFood> res = dao.selectAll(db);
        assertEquals(0, res.size());
    }

    @Test
    void testOrderFoodSelectAll() {
        List<OrderFood> orderFoods = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            OrderFood of = randomValidOrderFood();
            of.setId(dao.insert(of, db));
            orderFoods.add(of);
        }
        List<OrderFood> res = dao.selectAll(db);
        assertEquals(10, res.size());
        orderFoods.sort((o1, o2) -> Integer.compare(o1.getId(), o2.getId()));
        res.sort((o1, o2) -> Integer.compare(o1.getId(), o2.getId()));
        assertEquals(orderFoods.toString(), res.toString());
    }

    @Test
    void testOrderFoodUpdate() {
        OrderFood of = randomValidOrderFood();
        of.setId(dao.insert(of, db));
        assertTrue(of.getId() >= 0);

        // Update OrderFood
        of.setSeat(of.getSeat() + 1);
        of.setQuantity(of.getQuantity() + 1);
        of.setModifications(new String[] { "No salt" });
        dao.update(of, db);

        // Verify update
        OrderFood res = dao.select(of.getId(), db);
        assertNotNull(res);
        assertEquals(of.toString(), res.toString());
    }

    @Test
    void testOrderFoodDelete() {
        OrderFood of = randomValidOrderFood();
        of.setId(dao.insert(of, db));
        assertTrue(of.getId() >= 0, "Should be valid OrderFood SQL insert");
        assertEquals(1, dao.selectAll(db).size(), "Should be 1 OrderFood in DB");
        dao.delete(of.getId(), db);
        assertEquals(0, dao.selectAll(db).size(), "Should be 0 OrderFood in DB");
        assertNull(dao.select(of.getId(), db), "Should return null after delete");
    }

    @Test
    void testOrderFoodSelectNonExistent() {
        assertNull(dao.select(99999, db), "Should return null for non-existent OrderFood");
    }
}

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import dao.OrderDAO;
import dao.SessionDAO;
import model.Order;
import model.Session;

@Testable
public class OrderDAOTest extends DAOTest<OrderDAO> {
    private final SessionDAO sessionDao = new SessionDAO();
    private final SessionDAOTest sdaot = new SessionDAOTest();

    public OrderDAOTest() {
        this.dao = new OrderDAO();
    }

    Order randomOrder(int sessionId) {
        Order s = randomOrder();
        s.setSessionId(sessionId);
        return s;
    }

    Order randomOrder() {
        Order s = new Order();
        s.setClosed(Math.random() < 0.5);
        s.setTableNumber((int) (Math.random() * 100));
        s.setTip(Math.random() * 500);
        return s;
    }


    @BeforeAll
    static void setUp() {
        db.executeUpdate("DELETE FROM Orders", statement -> {});
        db.executeUpdate("DELETE FROM Session", statement -> {});
    }

    @AfterEach
    void cleanUp() {
        db.executeUpdate("DELETE FROM Orders", statement -> {});
        db.executeUpdate("DELETE FROM Session", statement -> {});
    }

        @Test
    void testOrderInsertAndSelect() {
        // Create a random session
        Session session = sdaot.randomSession();
        session.setId(sessionDao.insert(session, db));
        assertTrue(session.getId() > 0, "Insert should return a valid ID");

        // Create and insert a random order
        Order order = randomOrder(session.getId());
        order.setId(dao.insert(order, db));
        assertTrue(order.getId() > 0, "Insert should return a valid ID");

        // Verify the inserted order
        Order result = dao.select(order.getId(), db);
        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
        assertEquals(order.isClosed(), result.isClosed());
        assertEquals(order.getTableNumber(), result.getTableNumber());
        assertEquals(order.getTip(), result.getTip());
        assertEquals(order.getSessionId(), result.getSessionId());
    }

    @Test
    void testOrderSelectEmpty() {
        // Verify that the Orders table is empty
        List<Order> results = dao.selectAll(db);
        assertEquals(0, results.size());
    }

    @Test
    void testOrderSelectAll() {
        // Create and insert a session
        Session session = sdaot.randomSession();
        session.setId(sessionDao.insert(session, db));

        // Insert 10 random orders
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Order order = randomOrder(session.getId());
            order.setId(dao.insert(order, db));
            orders.add(order);
        }

        // Retrieve all orders from the database
        List<Order> results = dao.selectAll(db);
        assertEquals(10, results.size());

        // Sort both lists by ID for comparison
        orders.sort((o1, o2) -> Integer.compare(o1.getId(), o2.getId()));
        results.sort((o1, o2) -> Integer.compare(o1.getId(), o2.getId()));

        // Compare each order
        for (int i = 0; i < 10; i++) {
            Order expected = orders.get(i);
            Order actual = results.get(i);
            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.isClosed(), actual.isClosed());
            assertEquals(expected.getTableNumber(), actual.getTableNumber());
            assertEquals(expected.getTip(), actual.getTip());
            assertEquals(expected.getSessionId(), actual.getSessionId());
        }
    }

    @Test
    void testOrderUpdate() {
        // Create and insert a session
        Session session = sdaot.randomSession();
        session.setId(sessionDao.insert(session, db));

        // Insert an order
        Order original = randomOrder(session.getId());
        original.setId(dao.insert(original, db));

        // Create an updated version of the order
        Order updated = new Order(
                original.getId(),
                !original.isClosed(),
                original.getTableNumber() + 1,
                original.getTip() + 50.0,
                original.getSessionId()
        );

        // Perform the update
        dao.update(updated, db);

        // Verify the update
        Order result = dao.select(original.getId(), db);
        assertNotNull(result);
        assertEquals(updated.getId(), result.getId());
        assertEquals(updated.isClosed(), result.isClosed());
        assertEquals(updated.getTableNumber(), result.getTableNumber());
        assertEquals(updated.getTip(), result.getTip());
        assertEquals(updated.getSessionId(), result.getSessionId());

        // Verify only one record exists
        assertEquals(1, dao.selectAll(db).size());
    }

    @Test
    void testOrderDelete() {
        // Create and insert a session
        Session session = sdaot.randomSession();
        session.setId(sessionDao.insert(session, db));

        // Insert an order
        Order order = randomOrder(session.getId());
        order.setId(dao.insert(order, db));

        // Verify the order exists
        assertFalse(dao.selectAll(db).isEmpty());
        assertEquals(1, dao.selectAll(db).size());

        // Delete the order
        dao.delete(order.getId(), db);

        // Verify the order is deleted
        assertTrue(dao.selectAll(db).isEmpty(), "Should be empty after deletion");
        assertNull(dao.select(order.getId(), db), "Should return null for deleted order");
    }

    @Test
    void testOrderSelectNonExistent() {
        // Attempt to select a non-existent order
        assertNull(dao.select(99999, db), "Should return null for non-existent order");
    }
}
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import dao.OrderDAO;
import dao.ServerDAO;
import dao.SessionDAO;
import model.Order;
import model.Server;
import model.Session;

@Testable
public class OrderDAOTest extends DAOTest<OrderDAO> {
    private final SessionDAO sessionDao;
    private final SessionDAOTest sndaot;
    private final ServerDAO serverDao;
    private final ServerDAOTest svdaot;

    public OrderDAOTest() {
        sessionDao = new SessionDAO(db);
        serverDao = new ServerDAO(db);
        this.dao = new OrderDAO(db);
        sndaot = new SessionDAOTest();
        svdaot = new ServerDAOTest();
    }

    Order randomOrder(Session session) {
        return new Order(Math.random() < 0.5,(int) (Math.random() * 100),Math.random() * 500, session.getId());
    }

    Order randomValidOrder() {
        // Create & insert random server
        Server sv = svdaot.randomServer();
        assertTrue(sv.getId() >= 0, "Should be valid Server SQL insert");
        // Create & insert random session
        Session sn = sndaot.randomSession(sv);
        sn.setServer(sv.getId());
        assertTrue(sn.getId() >= 0, "Should be valid Session SQL insert");
        // Create order with valid database foreign keys (sessionId)
        Order od = randomOrder(sn);
        od.setSessionId(sn.getId());
        return od;
    }

    @BeforeAll
    static void setUp() {
        db.executeUpdate("DELETE FROM Orders", statement -> {
        });
        db.executeUpdate("DELETE FROM Session", statement -> {
        });
        db.executeUpdate("DELETE FROM Server", statement -> {
        });
    }

    @AfterEach
    void cleanUp() {
        db.executeUpdate("DELETE FROM Orders", statement -> {
        });
        db.executeUpdate("DELETE FROM Session", statement -> {
        });
        db.executeUpdate("DELETE FROM Server", statement -> {
        });
    }

    @Test
    void testOrderInsert() {
        Order od = randomValidOrder();
        assertTrue(od.getId() >= 0);
    }

    @Test
    void testOrderSelect() {
        Order od = randomValidOrder();
        Order res = dao.select(od.getId());
        assertNotNull(res);
        assertEquals(od.toString(), res.toString());
    }

    @Test
    void testOrderSelectEmpty() {
        List<Order> res = dao.selectAll();
        assertEquals(0, res.size());
    }

    @Test
    void testOrderSelectAll() {
        // Establish a valid session (shared to all orders)
        Server sv = svdaot.randomServer();
        assertTrue(sv.getId() >= 0);
        Session sn = sndaot.randomSession(sv);
        assertTrue(sn.getId() >= 0);

        // Do order insertions
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Order od = randomOrder(sn);
            orders.add(od);
        }
        List<Order> results = dao.selectAll();
        // assertEquals(10, results.size());
        // orders.sort((s1, s2) -> Integer.compare(s1.getId(), s2.getId()));
        // results.sort((s1, s2) -> Integer.compare(s1.getId(), s2.getId()));
        assertEquals(orders.toString(), results.toString());    
    
    }

    @Test
    void testOrderUpdate() {
        Order od = randomValidOrder();
        assertTrue(od.getId() >= 0);
        // Update order
        od.setClosed(!od.isClosed());
        dao.update(od);
        // Check if updated
        Order res = dao.select(od.getId());
        assertNotNull(res);
        assertEquals(od.getId(), res.getId());
        assertEquals(od.isClosed(), res.isClosed());
    }

    @Test
    void testOrderDelete() {
        Order od = randomValidOrder();
        assertTrue(od.getId() >= 0, "Should be valid Order SQL insert");
        assertEquals(1, dao.selectAll().size(), "Should be 1 order in DB");
        dao.delete(od.getId());
        assertEquals(0, dao.selectAll().size(), "Should be 0 order in DB");
        Order res = dao.select(od.getId());
        assertNull(res, "Should be null after delete");
    }

    @Test
    void testOrderSelectNonExistent() {
        Order od = randomValidOrder();
        assertTrue(od.getId() >= 0);
        // Select non-existent order
        Order res = dao.select(99999);
        assertNull(res);
    }

}
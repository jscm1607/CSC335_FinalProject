import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
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
        sessionDao = new SessionDAO();
        serverDao = new ServerDAO();
        this.dao = new OrderDAO();
        sndaot = new SessionDAOTest();
        svdaot = new ServerDAOTest();
    }

    Order randomOrder() {
        Order s = new Order();
        s.setClosed(Math.random() < 0.5);
        s.setTableNumber((int) (Math.random() * 100));
        s.setTip(Math.random() * 500);
        return s;
    }

    Order randomValidOrder() {
        // Create & insert random server
        Server sv = svdaot.randomServer();
        sv.setId(serverDao.insert(sv, db));
        assertTrue(sv.getId() >= 0, "Should be valid Server SQL insert");
        // Create & insert random session
        Session sn = sndaot.randomSession();
        sn.setServer(sv.getId());
        sn.setId(sessionDao.insert(sn, db));
        assertTrue(sn.getId() >= 0, "Should be valid Session SQL insert");
        // Create order with valid database foreign keys (sessionId)
        Order od = randomOrder();
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
    void testOrderInsertBadSession() {
        Session session = sndaot.randomSession();
        session.setId(12345); // Not a real session ID
        session.setId(sessionDao.insert(session, db));
        assertFalse(session.getId() >= 0, "Invalid FK sessionId should fail SQL");
    }

    @Test
    void testOrderInsert() {
        Order od = randomValidOrder();
        od.setId(dao.insert(od, db));
        assertTrue(od.getId() >= 0);
    }

    @Test
    void testOrderSelect() {
        Order od = randomValidOrder();
        od.setId(dao.insert(od, db));
        Order res = dao.select(od.getId(), db);
        assertNotNull(res);
        assertEquals(od.toString(), res.toString());
    }

    @Test
    void testOrderSelectEmpty() {
        List<Order> res = dao.selectAll(db);
        assertEquals(0, res.size());
    }

    @Test
    void testOrderSelectAll() {
        // Establish a valid session (shared to all orders)
        Server sv = svdaot.randomServer();
        sv.setId(serverDao.insert(sv, db));
        assertTrue(sv.getId() >= 0);
        Session sn = sndaot.randomSession();
        sn.setServer(sv.getId());
        sn.setId(sessionDao.insert(sn, db));
        assertTrue(sn.getId() >= 0);

        // Do order insertions
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Order od = randomOrder();
            od.setSessionId(sn.getId());
            od.setId(dao.insert(od, db));
            orders.add(od);
        }
        List<Order> results = dao.selectAll(db);
        // assertEquals(10, results.size());
        // orders.sort((s1, s2) -> Integer.compare(s1.getId(), s2.getId()));
        // results.sort((s1, s2) -> Integer.compare(s1.getId(), s2.getId()));
        assertEquals(orders.toString(), results.toString());    
    
    }

    @Test
    void testOrderUpdate() {
        Order od = randomValidOrder();
        od.setId(dao.insert(od, db));
        assertTrue(od.getId() >= 0);
        // Update order
        od.setClosed(!od.isClosed());
        dao.update(od, db);
        // Check if updated
        Order res = dao.select(od.getId(), db);
        assertNotNull(res);
        assertEquals(od.getId(), res.getId());
        assertEquals(od.isClosed(), res.isClosed());
    }

    @Test
    void testOrderDelete() {
        Order od = randomValidOrder();
        od.setId(dao.insert(od, db));
        assertTrue(od.getId() >= 0, "Should be valid Order SQL insert");
        assertEquals(1, dao.selectAll(db).size(), "Should be 1 order in DB");
        dao.delete(od.getId(), db);
        assertEquals(0, dao.selectAll(db).size(), "Should be 0 order in DB");
        Order res = dao.select(od.getId(), db);
        assertNull(res, "Should be null after delete");
    }

    @Test
    void testOrderSelectNonExistent() {
        Order od = randomValidOrder();
        od.setId(dao.insert(od, db));
        assertTrue(od.getId() >= 0);
        // Select non-existent order
        Order res = dao.select(99999, db);
        assertNull(res);
    }

}
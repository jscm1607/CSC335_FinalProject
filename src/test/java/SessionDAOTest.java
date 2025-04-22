import java.util.ArrayList;
import java.util.Date;
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

import dao.ServerDAO;
import dao.SessionDAO;
import model.Order;
import model.Server;
import model.Session;

@Testable
public class SessionDAOTest extends DAOTest<SessionDAO> {
    private final ServerDAO serverDao;

    public SessionDAOTest() {
        this.dao = new SessionDAO(db);
        this.serverDao = new ServerDAO(db);
        db.runH2Console();
    }

    public static Session randomSession(Server server) {
        return new Session(new Date(), server.getId(), Math.random() < 0.5);
    }

    @BeforeAll
    static void setUp() {
        db.executeUpdate("DELETE FROM Session", statement -> {
        });
        db.executeUpdate("DELETE FROM Server", statement -> {
        });
        db.executeUpdate("DELETE FROM Orders", statement -> {
        });
    }

    @AfterEach
    void cleanUp() {
        db.executeUpdate("DELETE FROM Session", statement -> {
        });
        db.executeUpdate("DELETE FROM Server", statement -> {
        });
        db.executeUpdate("DELETE FROM Orders", statement -> {
        });
    }

    @Test
    void testZSessionTipAggregation() {
        Session session = randomSession(ServerDAOTest.randomServer());
        assertTrue(session.getId() > -2);

        double aggregatedTips = 0;
        for (int i = 0; i < 10; i++) {
            double tip = (Math.random() * 100);
            aggregatedTips += tip;
            Order temp_order = new Order(false, i, tip, session.getId());
            assertTrue(temp_order.getId() > -2);
        }

        assertEquals(aggregatedTips, session.getTotalTips(), 0.01);
    }

    @Test
    void testSessionGetOrders() {
        Session session = randomSession(ServerDAOTest.randomServer());
        assertTrue(session.getId() > -2);
        Session seperateSession = randomSession(ServerDAOTest.randomServer());
        assertTrue(seperateSession.getId() > -2);

        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Order temp_order = OrderDAOTest.randomOrder(session);
            orders.add(temp_order);
            assertTrue(temp_order.getId() > -2);
        }
        List<Order> seperateOrders = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Order temp_order = OrderDAOTest.randomOrder(seperateSession);
            seperateOrders.add(temp_order);
            assertTrue(temp_order.getId() > -2);
        }

        List<Order> resultOrders = session.getOrders();
        assertEquals(orders.size(), resultOrders.size());
        List<Order> resultSeperateOrders = seperateSession.getOrders();
        assertEquals(seperateOrders.size(), resultSeperateOrders.size());
    }

    @Test
    void testSessionInsertAndSelect() {
        Server server = ServerDAOTest.randomServer();
        Session session = randomSession(server);
        assertTrue(session.getId() > -2);
        Session result = dao.select(session.getId());
        assertNotNull(result);
        assertEquals(session.getId(), result.getId());
        assertEquals(session.getServer(), result.getServer());
        assertEquals(session.isOpen(), result.isOpen());
        // Date comparison within 1 second tolerance due to timestamp precision
        assertTrue(Math.abs(session.getDate().getTime() - result.getDate().getTime()) < 1000);
    }

    @Test
    void testSessionSelectEmpty() {
        List<Session> results = dao.selectAll();
        assertEquals(0, results.size());
    }

    @Test
    void testSessionUpdate() {
        // Insert original session
        Session original = randomSession(ServerDAOTest.randomServer());

        assertTrue(original.getId() > -2);

        // Create updated version
        Session updated = new Session(
                original.getId(),
                new Date(),
                original.getServer(),
                !original.isOpen());

        // Perform update
        dao.update(updated);

        // Verify update
        Session result = dao.select(original.getId());
        assertNotNull(result);
        assertEquals(updated.getId(), result.getId());
        assertEquals(updated.getServer(), result.getServer());
        assertEquals(updated.isOpen(), result.isOpen());
        assertTrue(Math.abs(updated.getDate().getTime() - result.getDate().getTime()) < 1000);

        // Verify only one record exists
        assertEquals(1, dao.selectAll().size());
    }

    @Test
    void testSessionDelete() {
        // Start with empty table
        assertTrue(dao.selectAll().isEmpty());

        // Insert and verify
        Session session = randomSession(ServerDAOTest.randomServer());
        assertFalse(dao.selectAll().isEmpty());
        assertEquals(1, dao.selectAll().size());

        // Delete and verify
        dao.delete(session.getId());
        assertTrue(dao.selectAll().isEmpty(), "Should be empty after deletion");
        assertNull(dao.select(session.getId()), "Should return null for deleted session");
    }

    @Test
    void testSessionSelectNonExistent() {
        assertNull(dao.select(99999), "Should return null for non-existent session");
    }

    @Test
    void testSessionSelectAll() {
        // Insert 10 random sessions
        List<Server> servers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Server server = ServerDAOTest.randomServer();
            servers.add(server);
        }
        servers = serverDao.selectAll();
        List<Session> sessions = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Session session = randomSession(servers.get(i));
            sessions.add(session);
        }

        // Get all from db
        List<Session> results = dao.selectAll();
        assertEquals(10, results.size());

        // Sort both lists by ID for comparison
        sessions.sort((s1, s2) -> Integer.compare(s1.getId(), s2.getId()));
        results.sort((s1, s2) -> Integer.compare(s1.getId(), s2.getId()));

        // Compare each session
        for (int i = 0; i < 10; i++) {
            Session expected = sessions.get(i);
            Session actual = results.get(i);
            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getServer(), actual.getServer());
            assertEquals(expected.isOpen(), actual.isOpen());
            assertTrue(Math.abs(expected.getDate().getTime() - actual.getDate().getTime()) < 1000);
        }
    }


    @Test
    void testSessionSelectAllOpen() {
        // Insert 10 random sessions
        List<Server> servers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Server server = ServerDAOTest.randomServer();
            servers.add(server);
        }
        servers = serverDao.selectAll();
        List<Session> openSessions = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Session session = randomSession(servers.get(i));
            if (session.isOpen()) {
                openSessions.add(session);
            }
        }

        // Get all from db
        List<Session> results = dao.selectAllOpen();
        assertEquals(openSessions.size(), results.size());

        // Sort both lists by ID for comparison
        openSessions.sort((s1, s2) -> Integer.compare(s1.getId(), s2.getId()));
        results.sort((s1, s2) -> Integer.compare(s1.getId(), s2.getId()));

        // Compare each session
        for (int i = 0; i < openSessions.size(); i++) {
            Session expected = openSessions.get(i);
            Session actual = results.get(i);
            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getServer(), actual.getServer());
            assertEquals(expected.isOpen(), actual.isOpen());
            assertTrue(Math.abs(expected.getDate().getTime() - actual.getDate().getTime()) < 1000);
        }
    }

    @Test
    void testSessionSetDate(){
        Session session = randomSession(ServerDAOTest.randomServer());
        assertTrue(session.getId() > -2);
        Date newDate = new Date(System.currentTimeMillis() + 1000);
        session = session.setDate(newDate);
        assertEquals(newDate.getTime(), session.getDate().getTime(), 10);
    }

    @Test
    void testSessionToString() {
        Session session = randomSession(ServerDAOTest.randomServer());
        String expectedString = "Session{date=" + session.getDate() +
                ", server=" + session.getServer() +
                ", open=" + session.isOpen() + "}";
        assertEquals(expectedString, session.toString());
    }
}
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
import model.Server;
import model.Session;

@Testable
public class SessionDAOTest extends DAOTest<SessionDAO> {
    private final ServerDAO serverDao;
    private final ServerDAOTest sdaot = new ServerDAOTest();

    public SessionDAOTest() {
        this.dao = new SessionDAO();
        this.serverDao = new ServerDAO();
    }

    Session randomSession() {
        Session s = new Session();
        s.setDate(new Date());
        s.setTotalTips(Math.random() * 500);
        s.setOpen(Math.random() < 0.5);
        return s;
    }

    @BeforeAll
    static void setUp() {
        db.executeUpdate("DELETE FROM Session", statement -> {
        });
    }

    @AfterEach
    void cleanUp() {
        db.executeUpdate("DELETE FROM Session", statement -> {
        });
    }

    @Test
    void testSessionInsertAndSelect() {
        Session session = randomSession();
        Server server = sdaot.randomServer();
        session.setServer(serverDao.insert(server, db));
        session.setId(dao.insert(session, db));
        assertTrue(session.getId() > -2);
        Session result = dao.select(session.getId(), db);
        assertNotNull(result);
        assertEquals(session.getId(), result.getId());
        assertEquals(session.getServer(), result.getServer());
        assertEquals(session.getTotalTips(), result.getTotalTips());
        assertEquals(session.isOpen(), result.isOpen());
        // Date comparison within 1 second tolerance due to timestamp precision
        assertTrue(Math.abs(session.getDate().getTime() - result.getDate().getTime()) < 1000);
    }

    @Test
    void testSessionSelectEmpty() {
        List<Session> results = dao.selectAll(db);
        assertEquals(0, results.size());
    }

    @Test
    void testSessionSelectAll() {
        // Insert 10 random sessions
        List<Server> servers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Server server = sdaot.randomServer();
            server.setId(serverDao.insert(server, db));
            servers.add(server);
        }
        servers = serverDao.selectAll(db);
        List<Session> sessions = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Session session = randomSession();
            session.setServer(servers.get(i).getId());
            session.setId(dao.insert(session, db));
            sessions.add(session);
        }

        // Get all from db
        List<Session> results = dao.selectAll(db);
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
            assertEquals(expected.getTotalTips(), actual.getTotalTips());
            assertEquals(expected.isOpen(), actual.isOpen());
            assertTrue(Math.abs(expected.getDate().getTime() - actual.getDate().getTime()) < 1000);
        }
    }

    @Test
    void testSessionUpdate() {
        // Insert original session
        Session original = randomSession();
        Server server = sdaot.randomServer();
        original.setServer(serverDao.insert(server, db));
        original.setId(dao.insert(original, db));

        assertTrue(original.getId() > -2);

        // Create updated version
        Session updated = new Session(
                original.getId(),
                new Date(),
                original.getServer(),
                original.getTotalTips() + 50.0,
                !original.isOpen());

        // Perform update
        dao.update(updated, db);

        // Verify update
        Session result = dao.select(original.getId(), db);
        assertNotNull(result);
        assertEquals(updated.getId(), result.getId());
        assertEquals(updated.getServer(), result.getServer());
        assertEquals(updated.getTotalTips(), result.getTotalTips());
        assertEquals(updated.isOpen(), result.isOpen());
        assertTrue(Math.abs(updated.getDate().getTime() - result.getDate().getTime()) < 1000);

        // Verify only one record exists
        assertEquals(1, dao.selectAll(db).size());
    }

    @Test
    void testSessionDelete() {
        // Start with empty table
        assertTrue(dao.selectAll(db).isEmpty());

        // Insert and verify
        Session session = randomSession();
        session.setServer(serverDao.insert(sdaot.randomServer(), db));
        session.setId(dao.insert(session, db));
        assertFalse(dao.selectAll(db).isEmpty());
        assertEquals(1, dao.selectAll(db).size());

        // Delete and verify
        dao.delete(session.getId(), db);
        assertTrue(dao.selectAll(db).isEmpty(), "Should be empty after deletion");
        assertNull(dao.select(session.getId(), db), "Should return null for deleted session");
    }

    @Test
    void testSessionSelectNonExistent() {
        assertNull(dao.select(99999, db), "Should return null for non-existent session");
    }
}
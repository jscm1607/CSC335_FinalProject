import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.platform.commons.annotation.Testable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import model.Server;
import dao.ServerDAO;

@Testable
public class ServerDAOTest extends DAOTest<ServerDAO> {
    public ServerDAOTest() {
        this.dao = new ServerDAO();
    }

    private Server randomServer() {
        String randomUsername = "user" + System.currentTimeMillis();
        String randomPassword = "pass" + System.nanoTime();
        return new Server(randomUsername, randomPassword);
    }

    @BeforeAll
    static void setUp() {
        db.executeUpdate("DELETE FROM Server", statement -> {});
    }

    @AfterEach
    void cleanUp() {
        db.executeUpdate("DELETE FROM Server", statement -> {});
    }

    @Test
    void testServerInsertAndSelect() {
        Server s = randomServer();
        dao.insert(s, db);
        Server res = dao.select(s.getUsername(), db);
        assertEquals(s.toString(), res.toString());
    }

    @Test
    void testServerSelectEmpty() {
        List<Server> res = dao.selectAll(db);
        assertEquals(0, res.size());
    }

    @Test
    void testServerSelectAll() {
        // insert 10 random
        List<Server> svs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Server s = randomServer();
            dao.insert(s, db);
            svs.add(s);
        }
        // get all from db
        List<Server> res = dao.selectAll(db);
        assertEquals(10, res.size());
        // sort each list by username, should be equal
        res.sort((s1, s2) -> s1.getUsername().compareTo(s2.getUsername()));
        svs.sort((s1, s2) -> s1.getUsername().compareTo(s2.getUsername()));
        assertEquals(svs.toString(), res.toString());
    }

    @Test
    void testServerUpdate() {
        String newPassword = "newPass";
        Server s = randomServer();
        Server s_up = new Server(s.getUsername(), newPassword);
        dao.insert(s, db);
        dao.update(s_up, db);
        // check if updated
        Server res1 = dao.select(s.getUsername(), db);
        assertEquals(s_up.toString(), res1.toString());
        // check no duplicates
        Server res2 = dao.selectAll(db).get(0);
        assertEquals(s_up.toString(), res2.toString());
    }

    @Test
    void testServerDelete() {
        assertTrue(dao.selectAll(db).isEmpty());
        Server s = randomServer();
        dao.insert(s, db);
        List<Server> res = dao.selectAll(db);
        assertFalse(res.isEmpty());
        assertEquals(1, res.size());
        assertEquals(s.toString(), res.get(0).toString());
        assertEquals(s.toString(), dao.select(s.getUsername(), db).toString());
        dao.delete(s.getUsername(), db);
        assertTrue(dao.selectAll(db).isEmpty(), "Should be empty.");
    }



}

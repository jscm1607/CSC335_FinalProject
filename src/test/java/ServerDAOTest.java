import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import dao.ServerDAO;
import model.Server;

@Testable
public class ServerDAOTest extends DAOTest<ServerDAO> {
    public ServerDAOTest() {
        this.dao = new ServerDAO();
    }

    public Server randomServer() {
        String randomUsername = "user" + System.currentTimeMillis();
        String randomPassword = "pass" + System.nanoTime();
        Server ret = new Server();
        ret.setUsername(randomUsername);
        ret.setPassword(randomPassword);
        return ret;
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
        List<String> svs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Server s = randomServer();
            dao.insert(s, db);
            svs.add(s.getUsername());
        }
        // get all from db
        List<Server> res = dao.selectAll(db);
        List<String> res_usernames = new ArrayList<>();
        for (Server s : res) {
            res_usernames.add(s.getUsername());
        }
        assertEquals(10, res.size());
        svs.sort(String::compareTo);
        res_usernames.sort(String::compareTo);
        assertEquals(svs.toString(), res_usernames.toString());
    }

    @Test
    void testServerUpdate() {
        String newPassword = "newPass";
        Server s = randomServer();
        dao.insert(s, db);
        Server s_up = dao.select(s.getUsername(), db);
        s_up.setPassword(newPassword);
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

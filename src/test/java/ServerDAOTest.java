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
import model.Order;
import model.Server;
import model.Session;

@Testable
public class ServerDAOTest extends DAOTest<ServerDAO> {
    public ServerDAOTest() {
        this.dao = new ServerDAO(db);
    }

    public static Server randomServer() {
        return new Server("user" + System.currentTimeMillis(),"pass" + System.nanoTime());
    }

    @BeforeAll
    static void setUp() {
        db.executeUpdate("DELETE FROM Server", statement -> {});
        db.executeUpdate("DELETE FROM Session", statement -> {});
        db.executeUpdate("DELETE FROM Order", statement -> {});
    }

    @AfterEach
    void cleanUp() {
        db.executeUpdate("DELETE FROM Server", statement -> {});
        db.executeUpdate("DELETE FROM Session", statement -> {});
        db.executeUpdate("DELETE FROM Order", statement -> {});
    }

    @Test
    void testServerInsertAndSelect() {
        Server s = randomServer();
        Server res = dao.select(s.getUsername());
        assertEquals(s.getUsername(), res.getUsername());
        assertEquals(s.getPassword(), res.getPassword());
    }

    @Test
    void testServerSelectEmpty() {
        List<Server> res = dao.selectAll();
        assertEquals(0, res.size());
    }

    @Test
    void testZServerGetOpenOrders(){
        Server server = randomServer();
        assertTrue(server.getId() > -2);

        Session openSession = SessionDAOTest.randomSession(server);
        assertTrue(openSession.getId() > -2);
        openSession = openSession.setOpen(true);
        assertTrue(openSession.isOpen());

        Session closedSession = SessionDAOTest.randomSession(server);
        assertTrue(closedSession.getId() > -2);
        closedSession = closedSession.setOpen(false);
        assertFalse(closedSession.isOpen());

        ArrayList<Order> openOrders = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Order temp_order = OrderDAOTest.randomOrder(Math.random() < 0.5 ? openSession : closedSession);
            if (!temp_order.isClosed() && temp_order.getSessionId() == openSession.getId()) openOrders.add(temp_order);
        }

        assertEquals(openOrders.size(), server.getOpenOrders().size());
    }

    @Test
    void testServerSelectAll() {
        // insert 10 random
        List<String> svs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Server s = randomServer();
            svs.add(s.getUsername());
        }
        // get all from db
        List<Server> res = dao.selectAll();
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
        Server s_up = dao.select(s.getUsername());
        s_up.setPassword(newPassword);
        dao.update(s_up);
        // check if updated
        Server res1 = dao.select(s.getUsername());
        assertEquals(s_up.toString(), res1.toString());
        // check no duplicates
        Server res2 = dao.selectAll().get(0);
        assertEquals(s_up.toString(), res2.toString());
    }

    @Test
    void testServerDelete() {
        assertTrue(dao.selectAll().isEmpty());
        Server s = randomServer();
        List<Server> res = dao.selectAll();
        assertEquals(1, res.size());
        assertEquals(s.getPassword(), dao.select(s.getUsername()).getPassword());
        dao.delete(s.getUsername());
        assertTrue(dao.selectAll().isEmpty(), "Should be empty.");
    }



}

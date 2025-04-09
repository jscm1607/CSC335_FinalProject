import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import dao.FoodDAO;
import model.Food;

@Testable
public class FoodDAOTest extends DAOTest<FoodDAO> {
    public FoodDAOTest() {
        this.dao = new FoodDAO();
    }

    private Food randomFood() {
        Food fd = new Food();
        fd.setName("Food" + System.currentTimeMillis());
        fd.setCategory(Food.Category.values()[(int) (Math.random() * Food.Category.values().length)]);
        fd.setCost(Math.random() * 100);
        fd.setInStock(Math.random() < 0.5);
        fd.setNumOrders((int) (Math.random() * 100));
        return fd;
    }

    @BeforeAll
    static void setUp() {
        db.executeUpdate("DELETE FROM Food", statement -> {});
    }

    @AfterEach
    void cleanUp() {
        db.executeUpdate("DELETE FROM Food", statement -> {});
    }

    @Test
    void testFoodInsertAndSelect() {
        Food fd = randomFood();
        fd.setId(dao.insert(fd, db));
        assertTrue(fd.getId() > -2, "Should be valid Food SQL insert");
        Food res = dao.select(fd.getId(), db);
        assertEquals(fd.toString(), res.toString());
    }

    public static void main(String[] args) {
        FoodDAOTest test = new FoodDAOTest();
        FoodDAO dao = new FoodDAO();

        Food fd = test.randomFood();
        fd.setId(dao.insert(fd, db));
        assertTrue(fd.getId() > -2, "Should be valid Food SQL insert");
        Food res = dao.select(fd.getId(), db);
        assertEquals(fd.toString(), res.toString());
    }
    

    @Test
    void testFoodSelectEmpty() {
        List<Food> res = dao.selectAll(db);
        assertEquals(0, res.size());
    }

    @Test
    void testFoodSelectAll() {
        // insert 10 random
        List<Food> foods = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Food fd = randomFood();
            fd.setId(dao.insert(fd, db));
            foods.add(fd);
        }
        // get all from db
        List<Food> res = dao.selectAll(db);
        assertEquals(10, res.size());
        // sort each list by username, should be equal
        res.sort((s1, s2) -> Integer.compare(s1.getId(), s2.getId()));
        foods.sort((s1, s2) -> Integer.compare(s1.getId(), s2.getId()));
        assertEquals(foods.toString(), res.toString());
    }

    @Test
    void testFoodUpdate() {
        Food fd = randomFood();
        Food fd_up = randomFood();
        fd.setId(dao.insert(fd, db));
        fd_up.setId(fd.getId()); // set new food with same id
        dao.update(fd_up, db);
        // check if updated
        Food res1 = dao.select(fd.getId(), db);
        assertEquals(fd_up.toString(), res1.toString());
        // check no duplicates
        Food res2 = dao.selectAll(db).get(0);
        assertEquals(fd_up.toString(), res2.toString());
    }

    @Test
    void testFoodDelete() {
        assertTrue(dao.selectAll(db).isEmpty());
        Food fd = randomFood();
        fd.setId(dao.insert(fd, db));
        List<Food> res = dao.selectAll(db);
        assertFalse(res.isEmpty());
        assertEquals(1, res.size());
        assertEquals(fd.toString(), res.get(0).toString());
        assertEquals(fd.toString(), dao.select(fd.getId(), db).toString());
        dao.delete(fd.getId(), db);
        assertTrue(dao.selectAll(db).isEmpty(), "Should be empty.");
    }
}

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import dao.FoodDAO;
import model.Food;

@Testable
public class FoodDAOTest extends DAOTest<FoodDAO> {
    public FoodDAOTest() {
        this.dao = new FoodDAO(db);
    }

    public Food randomFood() {
        return new Food(
            "Food" + System.currentTimeMillis(),
            Food.Category.values()[(int) (Math.random() * Food.Category.values().length)],
            Math.random() * 100,
            Math.random() < 0.5
        );
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
        assertTrue(fd.getId() > -2, "Should be valid Food SQL insert");
        Food res = dao.select(fd.getId());
        assertEquals(fd.toString(), res.toString());
    }

    public static void main(String[] args) {
        FoodDAOTest test = new FoodDAOTest();
        FoodDAO dao = new FoodDAO();

        Food fd = test.randomFood();
        assertTrue(fd.getId() > -2, "Should be valid Food SQL insert");
        Food res = dao.select(fd.getId());
        assertEquals(fd.toString(), res.toString());
    }
    

    @Test
    void testFoodSelectEmpty() {
        List<Food> res = dao.selectAll();
        assertEquals(0, res.size());
    }

    @Test
    void testFoodSelectAll() {
        // insert 10 random
        List<Food> foods = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Food fd = randomFood();
            foods.add(fd);
        }
        // get all from db
        List<Food> res = dao.selectAll();
        assertEquals(10, res.size());
        // sort each list by username, should be equal
        res.sort((s1, s2) -> Integer.compare(s1.getId(), s2.getId()));
        foods.sort((s1, s2) -> Integer.compare(s1.getId(), s2.getId()));
        assertEquals(foods.toString(), res.toString());
    }

    @Test
    void testFoodUpdate() {
        Food fd = randomFood();
        Food fd_up = fd.setCost(fd.getCost() + 1);
        // check if updated
        Food res1 = dao.select(fd.getId());
        assertEquals(fd_up.toString(), res1.toString());
        // check no duplicates
        Food res2 = dao.selectAll().get(0);
        assertEquals(fd_up.toString(), res2.toString());
    }

    @Test
    void testFoodDelete() {
        assertTrue(dao.selectAll().isEmpty());
        Food fd = randomFood();
        List<Food> res = dao.selectAll();
        assertFalse(res.isEmpty());
        assertEquals(1, res.size());
        assertEquals(fd.toString(), res.get(0).toString());
        assertEquals(fd.toString(), dao.select(fd.getId()).toString());
        dao.delete(fd.getId());
        assertTrue(dao.selectAll().isEmpty(), "Should be empty.");
    }
}

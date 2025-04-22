import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import dao.FoodDAO;
import model.Food;
import model.OrderFood;

@Testable
public class FoodDAOTest extends DAOTest<FoodDAO> {
    public FoodDAOTest() {
        this.dao = new FoodDAO(db);
        db.runH2Console();
    }

    public static Food randomFood() {
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
        FoodDAO dao = new FoodDAO();

        Food fd = randomFood();
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

    @Test
    void testFoodGetNumOrdersById() {
        Food food1 = randomFood();
        Food food2 = randomFood();
        int food1Count = (int)Math.random() * 10;
        int food2Count = (int)Math.random() * 10;
        // insert 0-10 random OrderFood with food1
        for (int i = 0; i < food1Count; i++) {
            OrderFoodDAOTest.randomOrderFood(food1, OrderDAOTest.randomValidOrder());
        }
        // insert 0-10 random OrderFood with food2
        for (int i = 0; i < food2Count; i++) {
            OrderFoodDAOTest.randomOrderFood(food2, OrderDAOTest.randomValidOrder());
        }
        assertEquals(food1Count, dao.getNumFoodOrdersByFoodId(food1.getId()));
        assertEquals(food2Count, dao.getNumFoodOrdersByFoodId(food2.getId()));
    }

    @Test
    void testFoodVariousGettersSetters(){
        Food food = randomFood();
        assertTrue(food.getId() > -2);
        String name = food.getName();
        food = food.setName(name + " updated");
        Food.Category category = food.getCategory();
        Food.Category newCategory = Food.Category.values()[category.ordinal()+1 % Food.Category.values().length-1];
        food = food.setCategory(newCategory);
        boolean instock = food.isInStock();
        food = food.setInStock(!instock);
        assertEquals(food.getName(), name + " updated");
        assertEquals(food.getCategory(), newCategory);
        assertEquals(food.isInStock(), !instock);
    }

    @Test
    void testFoodGetTotalProfitByFoodName() {
        Food food1 = randomFood();
        Food food2 = randomFood();
        double food1Profit = 0;
        double food2Profit = 0;
        int food1Count = (int)Math.random() * 10;
        int food2Count = (int)Math.random() * 10;
        // insert 0-10 random OrderFood with food1
        for (int i = 0; i < food1Count; i++) {
            OrderFood of = OrderFoodDAOTest.randomOrderFood(food1, OrderDAOTest.randomValidOrder());
            food1Profit += food1.getCost() * of.getQuantity();
        }
        // insert 0-10 random OrderFood with food2
        for (int i = 0; i < food2Count; i++) {
            OrderFood of = OrderFoodDAOTest.randomOrderFood(food2, OrderDAOTest.randomValidOrder());
            food2Profit += food2.getCost() * of.getQuantity();
        }
        Map<String, Double> profits = dao.getTotalProfitByFoodName();
        assertEquals(food1Profit, profits.get(food1.getName()));
        assertEquals(food2Profit, profits.get(food2.getName()));
    }
}

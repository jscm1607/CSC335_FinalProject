package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Food;

public class FoodDAO extends DAO<Food, Integer> {

    public FoodDAO(){}
    public FoodDAO(DBM db){
        super(db);
    }

    @Override
    public int insert(Food entity) {
        return db.executeInsert("INSERT INTO food (name, category, cost, inStock) VALUES (?, ?, ?, ?)",
                ps -> {
                    ps.setString(1, entity.getName());
                    ps.setString(2, entity.getCategory().toString());
                    ps.setDouble(3, entity.getCost());
                    ps.setBoolean(4, entity.isInStock());
                });
    }

    @Override
    public void update(Food entity) {
        db.executeUpdate("UPDATE food SET name = ?, category = ?, cost = ?, inStock = ? WHERE id = ?",
                ps -> {
                    ps.setString(1, entity.getName());
                    ps.setString(2, entity.getCategory().toString());
                    ps.setDouble(3, entity.getCost());
                    ps.setBoolean(4, entity.isInStock());
                    ps.setInt(5, entity.getId());
                });
    }

    @Override
    public Food select(Integer id) {
        return db.executeQuery("SELECT * FROM food WHERE id = ?", ps -> ps.setInt(1, id), rs -> {
            if (rs.next()) {
                return new Food(
                    rs.getInt("id"),
                    rs.getString("name"),
                    Food.Category.valueOf(rs.getString("category")),
                    rs.getDouble("cost"),
                    rs.getBoolean("inStock")
                );
            }
            return null;
        });
    }

    @Override
    public List<Food> selectAll() {
        return db.executeQuery("SELECT * FROM food", ps -> {}, rs -> {
            List<Food> foods = new ArrayList<>();
            while (rs.next()) {
                foods.add(new Food(
                    rs.getInt("id"),
                    rs.getString("name"),
                    Food.Category.valueOf(rs.getString("category")),
                    rs.getDouble("cost"),
                    rs.getBoolean("inStock")
                ));
            }
            return foods;
        });
    }

    @Override
    public void delete(Integer id) {
        db.executeUpdate("DELETE FROM food WHERE id = ?", ps -> ps.setInt(1, id));
    }

    public Integer getNumFoodOrdersByFoodId(int id) {
        return db.executeQuery("SELECT SUM(quantity) AS total FROM OrderFood WHERE foodId = ?", 
        ps -> ps.setInt(1, id), 
        rs -> {
            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;
        });
    }

    public Map<String, Double> getTotalProfitByFoodName() {
        List<Food> allFoods = selectAll();
        Map<String, Double> foodProfits = new HashMap<>();
        for (Food f : allFoods) {
            foodProfits.put(f.getName(), getNumFoodOrdersByFoodId(f.getId()) * f.getCost());
        }
        return foodProfits;
    }
}

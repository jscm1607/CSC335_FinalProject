package dao;

import java.util.ArrayList;
import java.util.List;

import model.Food;

public class FoodDAO implements DAO<Food, Integer> {

    @Override
    public int insert(Food entity, DBM db) {
        return db.executeInsert("INSERT INTO food (name, category, cost, inStock, numOrders) VALUES (?, ?, ?, ?, ?)",
                ps -> {
                    ps.setString(1, entity.getName());
                    ps.setString(2, entity.getCategory().toString());
                    ps.setDouble(3, entity.getCost());
                    ps.setBoolean(4, entity.isInStock());
                    ps.setInt(5, entity.getNumOrders());
                });
    }

    @Override
    public void update(Food entity, DBM db) {
        db.executeUpdate("UPDATE food SET name = ?, category = ?, cost = ?, inStock = ?, numOrders = ? WHERE id = ?",
                ps -> {
                    ps.setString(1, entity.getName());
                    ps.setString(2, entity.getCategory().toString());
                    ps.setDouble(3, entity.getCost());
                    ps.setBoolean(4, entity.isInStock());
                    ps.setInt(5, entity.getNumOrders());
                    ps.setInt(6, entity.getId());
                });
    }

    @Override
    public Food select(Integer id, DBM db) {
        return db.executeQuery("SELECT * FROM food WHERE id = ?", ps -> ps.setInt(1, id), rs -> {
            if (rs.next()) {
                Food food = new Food();
                food.setId(rs.getInt("id"));
                food.setName(rs.getString("name"));
                food.setCategory(Food.Category.valueOf(rs.getString("category")));
                food.setCost(rs.getDouble("cost"));
                food.setInStock(rs.getBoolean("inStock"));
                food.setNumOrders(rs.getInt("numOrders"));
                return food;
            }
            return null;
        });
    }

    @Override
    public List<Food> selectAll(DBM db) {
        return db.executeQuery("SELECT * FROM food", ps -> {}, rs -> {
            List<Food> foods = new ArrayList<>();
            while (rs.next()) {
                Food food = new Food();
                food.setId(rs.getInt("id"));
                food.setName(rs.getString("name"));
                food.setCategory(Food.Category.valueOf(rs.getString("category")));
                food.setCost(rs.getDouble("cost"));
                food.setInStock(rs.getBoolean("inStock"));
                food.setNumOrders(rs.getInt("numOrders"));
                foods.add(food);
            }
            return foods;
        });
    }

    @Override
    public void delete(Integer id, DBM db) {
        db.executeUpdate("DELETE FROM food WHERE id = ?", ps -> ps.setInt(1, id));
    }

}

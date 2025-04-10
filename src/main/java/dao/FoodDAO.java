package dao;

import java.util.ArrayList;
import java.util.List;

import model.Food;

public class FoodDAO extends DAO<Food, Integer> {

    public FoodDAO(){}
    public FoodDAO(DBM db){
        super(db);
    }

    @Override
    public int insert(Food entity) {
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
    public void update(Food entity) {
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
    public Food select(Integer id) {
        return db.executeQuery("SELECT * FROM food WHERE id = ?", ps -> ps.setInt(1, id), rs -> {
            if (rs.next()) {
                return new Food(
                    rs.getInt("id"),
                    rs.getString("name"),
                    Food.Category.valueOf(rs.getString("category")),
                    rs.getDouble("cost"),
                    rs.getBoolean("inStock"),
                    rs.getInt("numOrders")
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
                    rs.getBoolean("inStock"),
                    rs.getInt("numOrders")
                ));
            }
            return foods;
        });
    }

    @Override
    public void delete(Integer id) {
        db.executeUpdate("DELETE FROM food WHERE id = ?", ps -> ps.setInt(1, id));
    }

}

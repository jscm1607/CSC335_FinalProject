package dao;

import java.util.ArrayList;
import java.util.List;

import model.OrderFood;

public class OrderFoodDAO implements DAO<OrderFood, Integer> {
    @Override
    public int insert(OrderFood entity, DBM db) {
        return db.executeInsert("INSERT INTO OrderFood (seat, quantity, foodId, orderId, modifications) VALUES (?, ?, ?, ?, ?)",
                ps -> {
                    ps.setInt(1, entity.getSeat());
                    ps.setInt(2, entity.getQuantity());
                    ps.setInt(3, entity.getFoodId());
                    ps.setInt(4, entity.getOrderId());
                    ps.setString(5, String.join(",", entity.getModifications()));
                });
    }

    @Override
    public void update(OrderFood entity, DBM db) {
        db.executeUpdate("UPDATE OrderFood SET seat = ?, quantity = ?, foodId = ?, orderId = ?, modifications = ? WHERE id = ?",
                ps -> {
                    ps.setInt(1, entity.getSeat());
                    ps.setInt(2, entity.getQuantity());
                    ps.setInt(3, entity.getFoodId());
                    ps.setInt(4, entity.getOrderId());
                    ps.setString(5, String.join(",", entity.getModifications()));
                    ps.setInt(6, entity.getId());
                });
    }

    @Override
    public OrderFood select(Integer id, DBM db) {
        return db.executeQuery("SELECT * FROM OrderFood WHERE id = ?", ps -> ps.setInt(1, id), rs -> {
            if (rs.next()) {
                OrderFood orderFood = new OrderFood();
                orderFood.setSeat(rs.getInt("seat"));
                orderFood.setQuantity(rs.getInt("quantity"));
                orderFood.setFoodId(rs.getInt("foodId"));
                orderFood.setOrderId(rs.getInt("orderId"));
                orderFood.setModifications(rs.getString("modifications").split(","));
                orderFood.setId(id);
                return orderFood;
            }
            return null;
        });
    }

    @Override
    public List<OrderFood> selectAll(DBM db) {
        return db.executeQuery("SELECT * FROM OrderFood", ps -> {}, rs -> {
            List<OrderFood> orderFoods = new ArrayList<>();
            while (rs.next()) {
                OrderFood orderFood = new OrderFood();
                orderFood.setSeat(rs.getInt("seat"));
                orderFood.setQuantity(rs.getInt("quantity"));
                orderFood.setFoodId(rs.getInt("foodId"));
                orderFood.setOrderId(rs.getInt("orderId"));
                orderFood.setModifications(rs.getString("modifications").split(","));
                orderFood.setId(rs.getInt("id"));
                orderFoods.add(orderFood);
            }
            return orderFoods;
        });
    }

    @Override
    public void delete(Integer id, DBM db) {
        db.executeUpdate("DELETE FROM OrderFood WHERE id = ?", ps -> ps.setInt(1, id));
    }
}

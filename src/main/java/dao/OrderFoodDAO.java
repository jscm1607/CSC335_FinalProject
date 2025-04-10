package dao;

import java.util.ArrayList;
import java.util.List;

import model.OrderFood;

public class OrderFoodDAO extends DAO<OrderFood, Integer> {
    @Override
    public int insert(OrderFood entity) {
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
    public void update(OrderFood entity) {
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
    public OrderFood select(Integer id) {
        return db.executeQuery("SELECT * FROM OrderFood WHERE id = ?", ps -> ps.setInt(1, id), rs -> {
            if (rs.next()) {
                return new OrderFood(
                    id,
                    rs.getInt("seat"),
                    rs.getInt("quantity"),
                    rs.getInt("foodId"),
                    rs.getInt("orderId"),
                    rs.getString("modifications").split(",")
                );
            }
            return null;
        });
    }

    @Override
    public List<OrderFood> selectAll() {
        return db.executeQuery("SELECT * FROM OrderFood", ps -> {}, rs -> {
            List<OrderFood> orderFoods = new ArrayList<>();
            while (rs.next()) {
                OrderFood orderFood = new OrderFood(
                    rs.getInt("id"),
                    rs.getInt("seat"),
                    rs.getInt("quantity"),
                    rs.getInt("foodId"),
                    rs.getInt("orderId"),
                    rs.getString("modifications").split(",")
                );
                orderFoods.add(orderFood);
            }
            return orderFoods;
        });
    }

    @Override
    public void delete(Integer id) {
        db.executeUpdate("DELETE FROM OrderFood WHERE id = ?", ps -> ps.setInt(1, id));
    }
}

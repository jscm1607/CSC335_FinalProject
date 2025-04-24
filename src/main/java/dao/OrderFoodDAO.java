// OrderFoodDAO

package dao;

import java.util.ArrayList;
import java.util.List;

import model.OrderFood;

public class OrderFoodDAO extends DAO<OrderFood, Integer> {

    public OrderFoodDAO(){}
    public OrderFoodDAO(DBM db){
        super(db);
    }

    @Override
    public int insert(OrderFood entity) {
        int result = db.executeInsert("INSERT INTO OrderFood (seat, quantity, foodId, orderId, modifications) VALUES (?, ?, ?, ?, ?)",
                ps -> {
                    ps.setInt(1, entity.getSeat());
                    ps.setInt(2, entity.getQuantity());
                    ps.setInt(3, entity.getFoodId());
                    ps.setInt(4, entity.getOrderId());
                    ps.setString(5, String.join(",", entity.getModifications()));
                });
        notifyDBChanged();
        return result;
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
        notifyDBChanged();
    }

    @Override
    public OrderFood select(Integer id) {
        OrderFood result = db.executeQuery("SELECT * FROM OrderFood WHERE id = ?", ps -> ps.setInt(1, id), rs -> {
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
        notifyDBChanged();
        return result;
    }

    @Override
    public List<OrderFood> selectAll() {
        List<OrderFood> result = db.executeQuery("SELECT * FROM OrderFood", ps -> {}, rs -> {
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
        notifyDBChanged();
        return result;
    }

    @Override
    public void delete(Integer id) {
        db.executeUpdate("DELETE FROM OrderFood WHERE id = ?", ps -> ps.setInt(1, id));
        notifyDBChanged();
    }
}

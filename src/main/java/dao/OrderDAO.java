package dao;

import java.util.ArrayList;
import java.util.List;

import model.Order;

public class OrderDAO implements DAO<Order, Integer> {

    @Override
    public int insert(Order entity, DBM db) {
        return db.executeInsert("INSERT INTO Orders (id, closed, tableNumber, tip, sessionId) VALUES (?, ?, ?, ?, ?)",
                ps -> {
                    ps.setInt(1, entity.getId());
                    ps.setBoolean(2, entity.isClosed());
                    ps.setInt(3, entity.getTableNumber());
                    ps.setDouble(4, entity.getTip());
                    ps.setInt(5, entity.getSessionId());
                });
    }

    @Override
    public void update(Order entity, DBM db) {
        db.executeUpdate("UPDATE Orders SET closed = ?, tableNumber = ?, tip = ?, sessionId = ? WHERE id = ?",
                ps -> {
                    ps.setBoolean(1, entity.isClosed());
                    ps.setInt(2, entity.getTableNumber());
                    ps.setDouble(3, entity.getTip());
                    ps.setInt(4, entity.getSessionId());
                    ps.setInt(5, entity.getId());
                });
    }

    @Override
    public Order select(Integer id, DBM db) {
        return db.executeQuery("SELECT * FROM Orders WHERE id = ?", ps -> ps.setInt(1, id), rs -> {
            if (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setClosed(rs.getBoolean("closed"));
                order.setTableNumber(rs.getInt("tableNumber"));
                order.setTip(rs.getDouble("tip"));
                order.setSessionId(rs.getInt("sessionId"));
                return order;
            }
            return null;
        });
    }

    @Override
    public List<Order> selectAll(DBM db) {
        return db.executeQuery("SELECT * FROM Orders", ps -> {}, rs -> {
            List<Order> orders = new ArrayList<>();
            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setClosed(rs.getBoolean("closed"));
                order.setTableNumber(rs.getInt("tableNumber"));
                order.setTip(rs.getDouble("tip"));
                order.setSessionId(rs.getInt("sessionId"));
                orders.add(order);
            }
            return orders;
        });
    }

    @Override
    public void delete(Integer id, DBM db) {
        db.executeUpdate("DELETE FROM Orders WHERE id = ?", ps -> ps.setInt(1, id));
    }
}
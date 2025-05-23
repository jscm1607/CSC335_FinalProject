package dao;

import java.util.ArrayList;
import java.util.List;

import model.Order;

public class OrderDAO extends DAO<Order, Integer> {

    public OrderDAO(){}
    public OrderDAO(DBM db){
        super(db);
    }

    @Override
    public int insert(Order entity) {
        return db.executeInsert("INSERT INTO Orders (closed, tableNumber, tip, sessionId) VALUES (?, ?, ?, ?)",
                ps -> {
                    ps.setBoolean(1, entity.isClosed());
                    ps.setInt(2, entity.getTableNumber());
                    ps.setDouble(3, entity.getTip());
                    ps.setInt(4, entity.getSessionId());
                });
    }

    @Override
    public void update(Order entity) {
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
    public Order select(Integer id) {
        return db.executeQuery("SELECT * FROM Orders WHERE id = ?", ps -> ps.setInt(1, id), rs -> {
            if (rs.next()) {
                return new Order(
                    rs.getInt("id"),
                    rs.getBoolean("closed"),
                    rs.getInt("tableNumber"),
                    rs.getDouble("tip"),
                    rs.getInt("sessionId")
                );
            }
            return null;
        });
    }

    @Override
    public List<Order> selectAll() {
        return db.executeQuery("SELECT * FROM Orders", ps -> {}, rs -> {
            List<Order> orders = new ArrayList<>();
            while (rs.next()) {
                Order order = new Order(
                    rs.getInt("id"),
                    rs.getBoolean("closed"),
                    rs.getInt("tableNumber"),
                    rs.getDouble("tip"),
                    rs.getInt("sessionId")
                );
                orders.add(order);
            }
            return orders;
        });
    }

    @Override
    public void delete(Integer id) {
        db.executeUpdate("DELETE FROM Orders WHERE id = ?", ps -> ps.setInt(1, id));
    }
}
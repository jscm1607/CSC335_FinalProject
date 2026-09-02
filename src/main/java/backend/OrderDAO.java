// OrderDAO

package backend;

import java.util.ArrayList;
import java.util.List;

public class OrderDAO extends DAO<Order, Integer> {

    public OrderDAO(){}
    public OrderDAO(DBM db){
        super(db);
    }

    @Override
    public int insert(Order entity) {
        int result = db.executeInsert("INSERT INTO Orders (closed, tableNumber, tip, sessionId) VALUES (?, ?, ?, ?)",
                ps -> {
                    ps.setBoolean(1, entity.isClosed());
                    ps.setInt(2, entity.getTableNumber());
                    ps.setDouble(3, entity.getTip());
                    ps.setInt(4, entity.getSessionId());
                });
        notifyDBChanged();
        return result;
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
        notifyDBChanged();
    }

    @Override
    public Order select(Integer id) {
        Order result = db.executeQuery("SELECT * FROM Orders WHERE id = ?", ps -> ps.setInt(1, id), rs -> {
            if (rs.next()) {
                return new Order(
                    rs.getInt("id"),
                    rs.getBoolean("closed"),
                    rs.getInt("tableNumber"),
                    rs.getDouble("tip"),
                    rs.getInt("sessionId"),
                    rs.getDate("createdAt")
                );
            }
            return null;
        });
        notifyDBChanged();
        return result;
    }

    @Override
    public List<Order> selectAll() {
        List<Order> result = db.executeQuery("SELECT * FROM Orders", ps -> {}, rs -> {
            List<Order> orders = new ArrayList<>();
            while (rs.next()) {
                Order order = new Order(
                    rs.getInt("id"),
                    rs.getBoolean("closed"),
                    rs.getInt("tableNumber"),
                    rs.getDouble("tip"),
                    rs.getInt("sessionId"),
                    rs.getDate("createdAt")
                );
                orders.add(order);
            }
            return orders;
        });
        notifyDBChanged();
        return result;
    }

    @Override
    public void delete(Integer id) {
        db.executeUpdate("DELETE FROM Orders WHERE id = ?", ps -> ps.setInt(1, id));
        notifyDBChanged();
    }
}
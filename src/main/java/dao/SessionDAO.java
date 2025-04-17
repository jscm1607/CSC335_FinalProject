package dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.Order;
import model.Session;

public class SessionDAO extends DAO<Session, Integer> {

    public SessionDAO(){}
    public SessionDAO(DBM db){
        super(db);
    }

    @Override
    public int insert(Session entity) {
        return db.executeInsert("INSERT INTO Session (date, serverId, open) VALUES (?, ?, ?)",
                statement -> {
                    statement.setTimestamp(1, new Timestamp(entity.getDate().getTime()));
                    statement.setInt(2, entity.getServer());
                    statement.setBoolean(3, entity.isOpen());
                });
    }

    @Override
    public void update(Session entity) {
        db.executeUpdate("UPDATE Session SET date = ?, serverId = ?, open = ? WHERE id = ?",
                statement -> {
                    statement.setTimestamp(1, new Timestamp(entity.getDate().getTime()));
                    statement.setInt(2, entity.getServer());
                    statement.setBoolean(3, entity.isOpen());
                    statement.setInt(4, entity.getId());
                });
    }

    @Override
    public Session select(Integer id) {
        return db.executeQuery("SELECT * FROM Session WHERE id = ?",
                statement -> statement.setInt(1, id),
                resultSet -> resultSet.next() 
                    ? new Session(
                        resultSet.getInt("id"),
                        resultSet.getTimestamp("date"),
                        resultSet.getInt("serverId"),
                        resultSet.getBoolean("open"))
                    : null);
    }

    @Override
    public List<Session> selectAll() {
        return db.executeQuery("SELECT * FROM Session",
                statement -> {},
                rs -> {
                    List<Session> sessions = new ArrayList<>();
                    while (rs.next()) {
                        sessions.add(new Session(
                            rs.getInt("id"),
                            rs.getTimestamp("date"),
                            rs.getInt("serverId"),
                            rs.getBoolean("open")
                        ));
                    }
                    return sessions;
                });
    }

    @Override
    public void delete(Integer id) {
        db.executeUpdate("DELETE FROM Session WHERE id = ?",
                statement -> statement.setInt(1, id));
    }

    public double getTotalTips(Integer id){
        return db.executeQuery(
            "SELECT SUM(tip) AS totalTips FROM Orders where sessionId = ?"
        ,statement ->statement.setInt(1, id),
        rs -> {
            double total = 0;
            while (rs.next()) {
                total += rs.getDouble("totalTips");
            }
            return total;
        });
    }

    public List<Order> getOrders(Integer sessionId){
        return db.executeQuery(
            "SELECT * FROM Orders where sessionId = ?"
        ,statement ->statement.setInt(1, sessionId),
        rs -> {
            List<Order> orders = new ArrayList<>();
            while (rs.next()) {
                orders.add(new Order(
                    rs.getInt("id"),
                    rs.getBoolean("closed"),
                    rs.getInt("tableNumber"),
                    rs.getDouble("tip"),
                    rs.getInt("sessionId")
                ));
            }
            return orders;
        });
    }
}
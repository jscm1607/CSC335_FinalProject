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
        int result = db.executeInsert("INSERT INTO Session (date, serverId, open) VALUES (?, ?, ?)",
                statement -> {
                    statement.setTimestamp(1, new Timestamp(entity.getDate().getTime()));
                    statement.setInt(2, entity.getServer());
                    statement.setBoolean(3, entity.isOpen());
                });
        notifyDBChanged();
        return result;
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
        notifyDBChanged();
    }

    @Override
    public Session select(Integer id) {
        Session session = db.executeQuery("SELECT * FROM Session WHERE id = ?",
                statement -> statement.setInt(1, id),
                resultSet -> resultSet.next()
                        ? new Session(
                                resultSet.getInt("id"),
                                resultSet.getTimestamp("date"),
                                resultSet.getInt("serverId"),
                                resultSet.getBoolean("open"))
                        : null);
        notifyDBChanged();
        return session;
    }

    @Override
    public List<Session> selectAll() {
        List<Session> sessions = db.executeQuery("SELECT * FROM Session",
                statement -> {},
                rs -> {
                    List<Session> result = new ArrayList<>();
                    while (rs.next()) {
                        result.add(new Session(
                                rs.getInt("id"),
                                rs.getTimestamp("date"),
                                rs.getInt("serverId"),
                                rs.getBoolean("open")
                        ));
                    }
                    return result;
                });
        notifyDBChanged();
        return sessions;
    }

    @Override
    public void delete(Integer id) {
        db.executeUpdate("DELETE FROM Session WHERE id = ?",
                statement -> statement.setInt(1, id));
        notifyDBChanged();
    }

    public double getTotalTips(Integer id){
        double total = db.executeQuery(
            "SELECT SUM(tip) AS totalTips FROM Orders where sessionId = ?"
        ,statement ->statement.setInt(1, id),
                rs -> {
                    double result = 0;
                    while (rs.next()) {
                        result += rs.getDouble("totalTips");
                    }
                    return result;
                });
        notifyDBChanged();
        return total;
    }

    public List<Session> selectAllOpen() {
        List<Session> sessions = db.executeQuery("SELECT * FROM Session WHERE open = true",
                statement -> {},
                rs -> {
                    List<Session> result = new ArrayList<>();
                    while (rs.next()) {
                        result.add(new Session(
                                rs.getInt("id"),
                                rs.getTimestamp("date"),
                                rs.getInt("serverId"),
                                rs.getBoolean("open")));
                    }
                    return result;
                });
        notifyDBChanged();
        return sessions;
    }

    public List<Order> getOrders(Integer sessionId){
        List<Order> orders = db.executeQuery(
            "SELECT * FROM Orders where sessionId = ?"
        ,statement ->statement.setInt(1, sessionId),
        rs -> {
            List<Order> result = new ArrayList<>();
            while (rs.next()) {
                result.add(new Order(
                    rs.getInt("id"),
                    rs.getBoolean("closed"),
                    rs.getInt("tableNumber"),
                    rs.getDouble("tip"),
                    rs.getInt("sessionId"),
                    rs.getDate("createdAt")
                ));
            }
            return result;
        });
        notifyDBChanged();
        return orders;
    }
}
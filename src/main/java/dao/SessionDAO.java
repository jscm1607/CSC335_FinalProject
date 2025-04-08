package dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.Session;

public class SessionDAO implements DAO<Session, Integer> {

    @Override
    public void insert(Session entity, DBM db) {
        db.executeUpdate("INSERT INTO Session (id, date, serverId, totalTips, open) VALUES (?, ?, ?, ?, ?)",
                statement -> {
                    statement.setInt(1, entity.getId());
                    statement.setTimestamp(2, new Timestamp(entity.getDate().getTime()));
                    statement.setInt(3, entity.getServer());
                    statement.setDouble(4, entity.getTotalTips());
                    statement.setBoolean(5, entity.isOpen());
                });
    }

    @Override
    public void update(Session entity, DBM db) {
        db.executeUpdate("UPDATE Session SET date = ?, serverId = ?, totalTips = ?, open = ? WHERE id = ?",
                statement -> {
                    statement.setTimestamp(1, new Timestamp(entity.getDate().getTime()));
                    statement.setInt(2, entity.getServer());
                    statement.setDouble(3, entity.getTotalTips());
                    statement.setBoolean(4, entity.isOpen());
                    statement.setInt(5, entity.getId());
                });
    }

    @Override
    public Session select(Integer id, DBM db) {
        return db.executeQuery("SELECT * FROM Session WHERE id = ?",
                statement -> statement.setInt(1, id),
                resultSet -> resultSet.next() 
                    ? new Session(
                        resultSet.getInt("id"),
                        resultSet.getTimestamp("date"),
                        resultSet.getInt("serverId"),
                        resultSet.getDouble("totalTips"),
                        resultSet.getBoolean("open"))
                    : null);
    }

    @Override
    public List<Session> selectAll(DBM db) {
        return db.executeQuery("SELECT * FROM Session",
                statement -> {},
                rs -> {
                    List<Session> sessions = new ArrayList<>();
                    while (rs.next()) {
                        sessions.add(new Session(
                            rs.getInt("id"),
                            rs.getTimestamp("date"),
                            rs.getInt("serverId"),
                            rs.getDouble("totalTips"),
                            rs.getBoolean("open")
                        ));
                    }
                    return sessions;
                });
    }

    @Override
    public void delete(Integer id, DBM db) {
        db.executeUpdate("DELETE FROM Session WHERE id = ?",
                statement -> statement.setInt(1, id));
    }
}
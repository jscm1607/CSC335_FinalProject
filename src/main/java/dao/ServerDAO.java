package dao;
import java.util.ArrayList;
import java.util.List;

import model.Server;

public class ServerDAO extends  DAO<Server, String> {

    @Override
    public int insert(Server entity) {
        return db.executeInsert("INSERT INTO Server (username, password) VALUES (?, ?)", 
            statement -> {
                statement.setString(1, entity.getUsername());
                statement.setString(2, entity.getPassword());
            });
    }

    @Override
    public void update(Server entity) {
        db.executeUpdate("UPDATE Server SET password = ? WHERE username = ?", 
            statement -> {
                statement.setString(1, entity.getPassword());
                statement.setString(2, entity.getUsername());
            });
    }

    @Override
    public Server select(String id) {
        return db.executeQuery("SELECT * FROM Server WHERE username = ?", 
            statement -> statement.setString(1, id), 
            resultSet -> resultSet.next() ? new Server(resultSet.getInt("id"),resultSet.getString("username"), resultSet.getString("password")) : null);
    }

    @Override
    public List<Server> selectAll() {
        return db.executeQuery("SELECT * FROM Server", 
            statement -> {}, 
            resultSet -> {
                List<Server> servers = new ArrayList<>();
                while (resultSet.next()) {
                    servers.add(new Server(resultSet.getInt("id"),resultSet.getString("username"), resultSet.getString("password")));
                }
                return servers;
            });
    }

    @Override
    public void delete(String id) {
        db.executeUpdate("DELETE FROM Server WHERE username = ?", 
            statement -> statement.setString(1, id));
    }

    
}
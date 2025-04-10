package dao;

import java.util.List;

/**
 * DAO is a Data Access Object interface, allowing CRUD
 * operations between database entities and Java objects.
 * T is the type of the entity, ID is the id value type.
 */
public abstract class DAO<T, ID> {

    static DBM db = new DBM("sa", "", "jdbc:h2:./data/db");

    void setDB(DBM db) {
        DAO.db = db;
    }

    abstract int insert(T entity);

    abstract void update(T entity);

    abstract T select(ID id);

    abstract List<T> selectAll();

    abstract void delete(ID id);
}

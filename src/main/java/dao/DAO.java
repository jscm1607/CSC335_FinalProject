package dao;

import java.util.List;

/**
 * DAO is a Data Access Object interface, allowing CRUD
 * operations between database entities and Java objects.
 * T is the type of the entity, ID is the id value type.
 */
public interface DAO<T, ID> {

    int insert(T entity, DBM db);

    void update(T entity, DBM db);

    T select(ID id, DBM db);

    List<T> selectAll(DBM db);

    void delete(ID id, DBM db);
}

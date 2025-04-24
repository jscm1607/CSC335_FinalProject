// Data Access Object

package dao;

import java.util.List;
import java.util.Observable;


/**
 * DAO is a Data Access Object interface, allowing CRUD
 * operations between database entities and Java objects.
 * T is the type of the entity, ID is the id value type.
 */
@SuppressWarnings("deprecation")
public abstract class DAO<T, ID> extends Observable {
    
    protected void notifyDBChanged() {
        setChanged();
        notifyObservers();
    }

    static DBM db = new DBM();

    public DAO(){};
    public DAO(DBM db){DAO.db=db;}

    abstract int insert(T entity);

    abstract void update(T entity);

    abstract T select(ID id);

    abstract List<T> selectAll();

    abstract void delete(ID id);
}

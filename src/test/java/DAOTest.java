// 100% coverage

import backend.DBM;

public abstract class DAOTest<T> {
    protected static DBM db = new DBM(
            "sa",
            "",
            "jdbc:h2:./data/db_testing");
    protected T dao;
}

package dao;

import java.sql.SQLException;

public class DBManager {
    private final UserDAO userDAO;
    private final CollectionDAO collectionDAO;
    private final HistoryDAO historyDAO;
    private final PermissionsDAO permissionsDAO;
    private final DAO dao;

    public DBManager(DAO coreDao) {
        this.userDAO = new UserDAO(coreDao);
        this.collectionDAO = new CollectionDAO(coreDao, this.userDAO);
        this.historyDAO = new HistoryDAO(coreDao, this.userDAO);
        this.permissionsDAO = new PermissionsDAO(coreDao);
        this.dao = coreDao;
    }

    public UserDAO users() { return userDAO; }
    public CollectionDAO collection() { return collectionDAO; }
    public HistoryDAO history() { return historyDAO; }
    public PermissionsDAO permissions() { return permissionsDAO; }

    public void ddlUpdate(String sql) throws SQLException {
        dao.executeUpdate(sql);
    }
}

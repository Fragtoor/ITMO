package managers;

import common.net.User;
import dao.DBManager;

import javax.naming.AuthenticationException;

public class UserManager {
    public static String login(DBManager db, User user) throws AuthenticationException {
        return db.users().selectUser(user);
    }

    public static String register(DBManager db, User user) throws AuthenticationException {
        return db.users().addUser(user);
    }
}


package managers;

import common.general.User;

import javax.naming.AuthenticationException;

public class UserManager {
    public String login(User user) throws AuthenticationException {
        // Доделай!!!
        // если получилось войти
        if (true) {
            return "User вошёл";
        }
        throw new AuthenticationException("Не получилось войти");
    }
    public String register(User user) throws AuthenticationException {
        // Доделай!!!
        // если получилось зарегистрироваться
        if (true) {
            return "User зарегался";
        }
        throw new AuthenticationException("Не получилось зарегаться");
    }

}


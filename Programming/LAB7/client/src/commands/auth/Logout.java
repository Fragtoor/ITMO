package commands.auth;

import commands.CommandClient;
import common.general.Request;
import common.general.User;
import tools.AuthService;

public class Logout extends CommandClient {
    public Logout(Object parameter) {
        super(parameter);
    }
    public Request<?, User> toRequest() {
        User user = AuthService.login();
        return new Request<>(user, "logout", null, null, false);
    }
}

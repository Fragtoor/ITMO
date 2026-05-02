package commands.auth;

import commands.CommandClient;
import common.general.AuthRequest;
import common.general.Request;
import common.general.User;
import tools.AuthService;

public class Logout extends CommandClient {
    public Logout(Object parameter) {
        super(parameter);
    }
    public Request toRequest() {
        User user = AuthService.login();
        return new AuthRequest(user, "logout");
    }
}

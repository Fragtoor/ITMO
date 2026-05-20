package commands.auth;

import commands.CommandClient;
import common.net.request.AuthRequest;
import common.net.request.Request;
import common.net.User;
import tools.AuthService;

public class Logout extends CommandClient {
    public Logout(String... params) {
        super(params);
    }
    public Request toRequest() {
        User user = AuthService.login();
        return new AuthRequest(user, "logout");
    }
}

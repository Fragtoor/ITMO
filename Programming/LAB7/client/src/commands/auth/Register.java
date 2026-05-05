package commands.auth;

import commands.CommandClient;
import common.net.request.AuthRequest;
import common.net.request.Request;
import common.net.User;
import tools.AuthService;


public class Register extends CommandClient {
    public Register(String... params) {
        super(params);
    }

    public Request toRequest() {
        User user = AuthService.register();
        return new AuthRequest(user, "register");
    }
}

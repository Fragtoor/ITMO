package commands.auth;

import commands.CommandClient;
import common.general.AuthRequest;
import common.general.Request;
import common.general.User;
import tools.AuthService;


public class Register extends CommandClient {
    public Register(Object parameter) {
        super(parameter);
    }

    public Request toRequest() {
        User user = AuthService.register();
        return new AuthRequest(user, "register");
    }
}

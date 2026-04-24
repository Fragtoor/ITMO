package commands.auth;

import commands.CommandClient;
import common.general.Request;

import common.general.User;
import tools.AuthService;



public class Login extends CommandClient {
    public Login(Object parameter) {
        super(parameter);
    }
    public Request<?, User> toRequest() {
        User user = AuthService.login();
        return new Request<>("login", null, user, false);
    }
}

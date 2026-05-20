package commands.auth;

import commands.CommandClient;
import common.net.request.AuthRequest;
import common.net.request.Request;

import common.net.User;
import tools.AuthService;



public class Login extends CommandClient {
    public Login(String... params) {
        super(params);
    }
    public Request toRequest() {
        User user = AuthService.login();
        return new AuthRequest(user, "login");
    }
}

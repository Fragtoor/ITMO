package commands.auth;

import commands.CommandClient;
import common.net.request.AuthRequest;
import common.net.request.Request;


public class Login extends CommandClient {
    public Login(String... params) {
        super(params);
    }
    public Request toRequest() {
        return new AuthRequest(getUser(), "login");
    }
}

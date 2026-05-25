package commands.auth;

import commands.CommandClient;
import common.net.request.AuthRequest;
import common.net.request.Request;


public class Register extends CommandClient {
    public Register(String... params) {
        super(params);
    }

    public Request toRequest() {
        return new AuthRequest(getUser(), "register");
    }
}

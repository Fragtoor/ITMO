package commands.auth;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import managers.UserManager;

import javax.naming.AuthenticationException;

public class Login extends Command {
    public Login(User user) {
        super(user);
    }

    public Response execute(UserManager um, Object... params) {
        try {
            String message = um.login(getUser());
            return new Response(ResponseType.AUTH_SUCCESS, message);
        } catch (AuthenticationException e) {
            return new Response(ResponseType.AUTH_ERROR, e.getMessage());
        }
    }
    public String getCommandName() {
        return "login";
    }
}

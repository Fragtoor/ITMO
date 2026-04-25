package commands.auth;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import common.tools.Validator;
import managers.UserManager;

import javax.naming.AuthenticationException;

public class Login extends Command {
    private final UserManager um;
    public Login(UserManager um) {
        this.um = um;
    }

    public boolean validateParams(Object... params) {
        if ((params.length != 0) && (params[0] instanceof User user)) {
            if (!Validator.validatePassword(user.getPassword()).equals("ОК")) return false;
            return user.getLogin() != null && !user.getLogin().isBlank();
        }
        return false;
    }

    public Response execute(Object... params) {
        try {
            String message = um.login((User)params[0]);
            return new Response(ResponseType.AUTH_SUCCESS, message);
        } catch (AuthenticationException e) {
            return new Response(ResponseType.AUTH_SUCCESS, e.getMessage());
        }
    }
    public String getCommandName() {
        return "login";
    }
}

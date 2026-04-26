package commands.auth;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import dao.DAO;
import managers.UserManager;

import javax.naming.AuthenticationException;

public class Register extends Command {
    private final UserManager um;
    private final User user;
    public Register(UserManager um, User user, DAO dao) {
        super(user, dao);
        this.um = um;
        this.user = user;
    }

    public Response execute(Object... params) {
        try {
            String message = um.register(user);
            return new Response(ResponseType.AUTH_SUCCESS, message);
        } catch (AuthenticationException e) {
            return new Response(ResponseType.AUTH_SUCCESS, e.getMessage());
        }
    }
    public String getCommandName() {
        return "register";
    }
}

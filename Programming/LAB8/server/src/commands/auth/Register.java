package commands.auth;

import commands.Command;
import common.net.*;
import dao.DBManager;
import managers.UserManager;

import javax.naming.AuthenticationException;

public class Register extends Command {
    public Register(User user) {
        super(user);
    }

    public Response execute(DBManager db, Object... params) {
        try {
            String message = UserManager.register(db, getUser());
            return new Response(ResponseType.AUTH_SUCCESS, message);
        } catch (AuthenticationException e) {
            return new Response(ResponseType.AUTH_ERROR, e.getMessage());
        }
    }
    public String getCommandName() {
        return "register";
    }
}

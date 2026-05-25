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
            return new Response.Builder(ResponseType.AUTH_SUCCESS).message(message).build();
        } catch (AuthenticationException e) {
            return new Response.Builder(ResponseType.AUTH_ERROR).message(e.getMessage()).build();
        }
    }
    public String getCommandName() {
        return "register";
    }
}

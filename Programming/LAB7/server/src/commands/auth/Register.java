package commands.auth;

import commands.Command;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import dao.DBManager;
import managers.UserManager;

import javax.naming.AuthenticationException;

public class Register extends Command {
    private final DBManager db;
    public Register(User user, DBManager db) {
        super(user);
        this.db = db;
    }

    public Response execute(Object... params) {
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

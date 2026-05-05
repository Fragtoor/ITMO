package common.net.request;

import common.net.User;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String commandName;
    private final User user;
    private final Object[] params;

    public Request(User user, String commandName, Object... params) {
        this.commandName = commandName;
        this.user = user;
        this.params = params;
    }

    public String getCommandName() {
        return commandName;
    }

    public Object[] getParams() {return params;}

    public User getUser() {
        return user;
    }
}
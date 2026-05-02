package common.general;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String commandName;
    private final User user;

    public Request(User user, String commandName) {
        this.commandName = commandName;
        this.user = user;
    }

    public String getCommandName() {
        return commandName;
    }

    public User getUser() {
        return user;
    }
}
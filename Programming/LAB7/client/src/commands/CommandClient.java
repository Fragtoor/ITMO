package commands;
import common.general.Request;
import common.general.User;

import java.io.Serializable;

public class CommandClient implements Serializable {
    private Object parameter;
    private boolean fromTheFile;
    private User user;

    public CommandClient(Object parameter) {
        this.parameter = parameter;
    }

    public void validate() {
    }

    public Request<?, ?> toRequest() {
        return new Request<>(getUser(), "command", null, null, fromTheFile);
    }

    public Object getParameter() {
        return parameter;
    }

    public boolean getFromTheFile() {
        return fromTheFile;
    }

    public void setFromTheFile(boolean fromTheFile) {
        this.fromTheFile = fromTheFile;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}

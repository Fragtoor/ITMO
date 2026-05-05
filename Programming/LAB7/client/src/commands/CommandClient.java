package commands;

import common.net.request.CollectionRequest;
import common.net.request.Request;
import common.net.User;

import java.io.Serializable;

public class CommandClient implements Serializable {
    private final String[] params;
    private User user;
    private boolean fromTheFile;

    public CommandClient(String[] params) {
        this.params = params;
    }

    public void validate() {}

    public Request toRequest() {
        return new CollectionRequest(getUser(), "command", false);
    }

    public String[] getParams() {return params;}

    public boolean getFromTheFile() {return fromTheFile;}

    public void setFromTheFile(boolean fromTheFile) {this.fromTheFile = fromTheFile;}

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}

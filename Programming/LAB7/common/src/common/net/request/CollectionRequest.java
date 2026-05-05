package common.net.request;

import common.net.User;

public class CollectionRequest extends Request {
    private final boolean fromTheFile;
    public CollectionRequest(User user, String commandName, boolean fromTheFile, Object... params) {
        super(user, commandName, params);
        this.fromTheFile = fromTheFile;
    }

    public boolean getFromTheFile() {return fromTheFile;}
}

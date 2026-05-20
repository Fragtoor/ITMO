package common.net.request;

import common.net.User;

public class CollectionRequest extends Request {
    public CollectionRequest(User user, String commandName, Object... params) {
        super(user, commandName, params);
    }
}

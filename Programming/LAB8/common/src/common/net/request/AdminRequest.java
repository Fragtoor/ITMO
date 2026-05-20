package common.net.request;

import common.net.User;

public class AdminRequest extends Request{
    public AdminRequest(User user, String commandName, Object... params) {
        super(user, commandName, params);
    }
}

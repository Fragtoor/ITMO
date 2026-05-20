package common.net.request;

import common.net.User;

public class AuthRequest extends Request{
    public AuthRequest(User user, String commandName) {
        super(user, commandName);
    }
}

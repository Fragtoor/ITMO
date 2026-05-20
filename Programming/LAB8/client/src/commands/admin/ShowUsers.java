package commands.admin;

import commands.CommandClient;
import common.net.request.AdminRequest;
import common.net.request.Request;

public class ShowUsers extends CommandClient {
    public ShowUsers(String... params) {
        super(params);
    }
    public Request toRequest() {
        return new AdminRequest(getUser(), "show_users");
    }
}


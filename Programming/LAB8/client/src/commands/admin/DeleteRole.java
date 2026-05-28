package commands.admin;

import commands.CommandClient;
import common.net.request.AdminRequest;
import common.net.request.Request;

public class DeleteRole extends CommandClient {
    public DeleteRole(String... params) {
        super(params);
    }

    public Request toRequest() {
        return new AdminRequest(getUser(), "delete_role", (Object[]) getParams());
    }
}
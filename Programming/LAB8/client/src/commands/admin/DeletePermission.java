package commands.admin;

import commands.CommandClient;
import common.net.request.AdminRequest;
import common.net.request.Request;

public class DeletePermission extends CommandClient {
    public DeletePermission(String... params) {
        super(params);
    }

    public Request toRequest() {
        return new AdminRequest(getUser(), "delete_permission", (Object[]) getParams());
    }
}
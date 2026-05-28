package commands.admin;

import commands.CommandClient;
import common.net.request.AdminRequest;
import common.net.request.Request;

public class CreateRole extends CommandClient {
    public CreateRole(String... params) {
        super(params);
    }

    public Request toRequest() {
        return new AdminRequest(getUser(), "create_role", (Object[]) getParams());
    }
}
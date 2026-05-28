package commands.admin;
import commands.CommandClient;
import common.net.request.AdminRequest;
import common.net.request.Request;

public class CreatePermission extends CommandClient {
    public CreatePermission(String... params) { super(params); }
    public Request toRequest() { return new AdminRequest(getUser(), "create_permission", (Object[]) getParams()); }
}
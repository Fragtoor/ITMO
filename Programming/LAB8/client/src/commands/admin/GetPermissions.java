package commands.admin;
import commands.CommandClient;
import common.net.request.AdminRequest;
import common.net.request.Request;

public class GetPermissions extends CommandClient {
    public GetPermissions(String... params) { super(params); }
    public Request toRequest() { return new AdminRequest(getUser(), "get_permissions", (Object[]) getParams()); }
}
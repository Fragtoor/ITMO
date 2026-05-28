package commands.admin;
import commands.CommandClient;
import common.net.request.AdminRequest;
import common.net.request.Request;

public class GetRoles extends CommandClient {
    public GetRoles(String... params) { super(params); }
    public Request toRequest() { return new AdminRequest(getUser(), "get_roles", (Object[]) getParams()); }
}
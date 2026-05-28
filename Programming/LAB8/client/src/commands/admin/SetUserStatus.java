package commands.admin;
import commands.CommandClient;
import common.net.request.AdminRequest;
import common.net.request.Request;

public class SetUserStatus extends CommandClient {
    public SetUserStatus(String... params) { super(params); }
    public Request toRequest() { return new AdminRequest(getUser(), "set_user_status", (Object[]) getParams()); }
}
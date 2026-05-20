package commands.admin;

import commands.CommandClient;
import common.exceptions.InvalidInputException;
import common.net.request.AdminRequest;
import common.net.request.Request;

public class DeleteFunctions extends CommandClient {
    public DeleteFunctions(String... params) {
        super(params);
    }

    public void validate() {
        try {
            if (getParams() == null || getParams().length <= 1) {
                throw new InvalidInputException("");
            }

        } catch (InvalidInputException e) {
            throw new InvalidInputException("""
                    У delete_functions должны быть аргументы nameFunction и functions!
                    nameFunction - название роли
                    functions - последовательность из функциональностей
                    """);
        }
    }

    public Request toRequest() {
        return new AdminRequest(getUser(), "delete_functions", (Object[]) getParams());
    }
}

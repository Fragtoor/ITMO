package commands.admin;

import commands.CommandClient;
import common.exceptions.InvalidInputException;
import common.net.request.AdminRequest;
import common.net.request.Request;


public class AddFunctions extends CommandClient {
    public AddFunctions(String... params) {
        super(params);
    }

    public void validate() {
        try {
            if (getParams() == null || getParams().length <= 1) {
                throw new InvalidInputException("");
            }

        } catch (InvalidInputException e) {
            throw new InvalidInputException("""
                    У add_functions должны быть аргументы nameFunction и functions!
                    nameFunction - название роли
                    functions - последовательность из функциональностей
                    """);
        }
    }
    public Request toRequest() {
        return new AdminRequest(getUser(), "add_functions", (Object[]) getParams());
    }
}

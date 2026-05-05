package commands.admin;

import commands.CommandClient;
import common.exceptions.InvalidInputException;
import common.net.request.AdminRequest;
import common.net.request.Request;
import common.tools.Validator;

public class AddFunctions extends CommandClient {
    public AddFunctions(String... params) {
        super(params);
    }

    public void validate() {
        try {
            if (getParams() == null || getParams().length <= 1) {
                throw new InvalidInputException("");
            }
            else if (!Validator.isInt(getParams()[0])) {
                throw new InvalidInputException("");
            } else if (Integer.parseInt(getParams()[0]) <= 0) {
                throw new InvalidInputException("");
            }
        } catch (InvalidInputException e) {
            throw new InvalidInputException("У add_functions должны быть аргументы id и function!" +
                    "\nid - целое положительное число" +
                    "\nfunction - последовательность из строк\n");
        }
    }
    public Request toRequest() {
        return new AdminRequest(getUser(), "add_functions", (Object[]) getParams());
    }
}

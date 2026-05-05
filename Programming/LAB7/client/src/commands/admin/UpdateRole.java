package commands.admin;

import commands.CommandClient;
import common.exceptions.InvalidInputException;
import common.net.request.AdminRequest;
import common.net.request.Request;
import common.tools.Validator;


public class UpdateRole extends CommandClient {
    public UpdateRole(String... params) {
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
            throw new InvalidInputException("""
                    У update_role должны быть аргументы id и role!
                    id - целое положительное число
                    role - строка
                    """);
        }
    }

    public Request toRequest() {
        return new AdminRequest(getUser(), "update_role", (Object[]) getParams());
    }
}

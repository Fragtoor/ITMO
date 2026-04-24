package commands.auth;

import commands.Command;
import common.general.User;
import common.tools.Validator;
import managers.UserManager;

public class Register extends Command {
    private final UserManager um;
    public Register(UserManager um) {
        this.um = um;
    }

    public boolean validateParams(Object... params) {
        if ((params.length != 0) && (params[0] instanceof User user)) {
            if (!Validator.validatePassword(user.getPassword()).equals("ОК")) return false;
            if (user.getLogin() == null || user.getLogin().isBlank()) return false;
            return user.getUserName() != null && !user.getUserName().isBlank();
        }
        return false;
    }

    public String execute(Object... params) {
        return um.register((User)params[0]);
    }
    public String getCommandName() {
        return "register";
    }
}

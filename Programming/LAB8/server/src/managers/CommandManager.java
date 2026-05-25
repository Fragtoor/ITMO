package managers;


import commands.Command;
import commands.admin.AddFunctions;
import commands.admin.DeleteFunctions;
import commands.admin.ShowUsers;
import commands.admin.UpdateRole;
import commands.auth.Login;
import commands.auth.Register;
import commands.collection.*;
import common.exceptions.InvalidInputException;
import common.net.Response;
import common.net.ResponseType;
import common.net.User;
import dao.DBManager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CommandManager {
    private CollectionManager cm;
    private final User user;
    private final DBManager db;
    private final Map<String, Command> commandsMap = new HashMap<>();
    private Set<String> userPermissions;

    public CommandManager(DBManager db, User user) {
        this.db = db;
        this.user = user;
        initCommandsMap();
        initUserPermissions();
    }

    public CommandManager(CollectionManager cm, DBManager db, User user) {
        this.cm = cm;
        this.db = db;
        this.user = user;
        initCommandsMap();
        initUserPermissions();
    }

    private void initUserPermissions() {
        try {
            userPermissions = db.getUserPermissions(user);
        } catch (SQLException e) {
            userPermissions = new HashSet<>();
        }
    }

    private void initCommandsMap() {
        commandsMap.put("help", new Help(user));
        commandsMap.put("clear", new Clear(user));
        commandsMap.put("info", new Info(user));
        commandsMap.put("show", new Show(user));
        commandsMap.put("add", new Add(user));
        commandsMap.put("update", new Update(user));
        commandsMap.put("remove_by_id", new RemoveById(user));
        commandsMap.put("history", new History(user));
        commandsMap.put("add_if_min", new AddIfMin(user));
        commandsMap.put("remove_greater", new RemoveGreater(user));
        commandsMap.put("sum_of_number_of_participants", new SumOfNumberOfParticipants(user));
        commandsMap.put("average_of_number_of_participants", new AverageOfNumberOfParticipants(user));
        commandsMap.put("filter_contains_name", new FilterContainsName(user));
        commandsMap.put("login", new Login(user));
        commandsMap.put("register", new Register(user));
        commandsMap.put("show_users", new ShowUsers(user));
        commandsMap.put("update_role", new UpdateRole(user));
        commandsMap.put("add_functions", new AddFunctions(user));
        commandsMap.put("delete_functions", new DeleteFunctions(user));
    }

    public Response executeCollectionCommand(String nameCommand, Object... params) {
        if (!commandsMap.containsKey(nameCommand)) {
            return new Response.Builder(ResponseType.COMMAND_ERROR).message("Такой команды нет! Используйте команду help, чтобы посмотреть список команд").build();
        }

        Command command = commandsMap.get(nameCommand);
        try {
            command.validateParams(params);
        } catch (InvalidInputException e) {
            return new Response.Builder(ResponseType.COMMAND_ERROR).message(e.getMessage()).build();
        }

        String requiredPermission = command.getRequiredPermission();

        if (requiredPermission != null && !userPermissions.contains(requiredPermission)) {
            return new Response.Builder(ResponseType.COMMAND_ERROR).message("У вас недостаточно прав для выполнения этой команды.").build();
        }
        return command.execute(cm, db, params);
    }

    public Response executeAuthCommand(String nameCommand) {
        if (!commandsMap.containsKey(nameCommand)) {
            return new Response.Builder(ResponseType.COMMAND_ERROR).message("Такой команды нет! Используйте команду help, чтобы посмотреть список команд").build();
        }
        Command command = commandsMap.get(nameCommand);

        return command.execute(db);
    }

    public Response executeAdminCommand(String nameCommand, Object... params) {
        if (!commandsMap.containsKey(nameCommand)) {
            return new Response.Builder(ResponseType.COMMAND_ERROR).message("Такой команды нет! Используйте команду help, чтобы посмотреть список команд").build();
        }
        Command command = commandsMap.get(nameCommand);

        try {
            command.validateParams(params);
        } catch (InvalidInputException e) {
            return new Response.Builder(ResponseType.COMMAND_ERROR).message(e.getMessage()).build();
        }

        String requiredPermission = command.getRequiredPermission();
        if (requiredPermission != null && !userPermissions.contains(requiredPermission)) {
            return new Response.Builder(ResponseType.COMMAND_ERROR).message("У вас недостаточно прав для выполнения этой команды.").build();
        }
        return command.execute(db, params);
    }
}
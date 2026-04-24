package reader_manager;


import commands.*;
import commands.auth.Login;
import commands.auth.Register;
import commands.collection.*;
import commands.other.Exit;
import commands.other.Help;

import java.util.HashMap;
/**
 * Менеджер команд - управляет созданием и выполнением команд.
 */
public class CommandManager {
    /**
     * HashMap всех команд, которые используются в программе
     */
    public HashMap<String, CommandClient> commandsUser = new HashMap<>();
    /**
     * Добавление всех команд в {@code commands}
     *
     * @param parameter параметр, который передаётся команде в командной строке
     */
    public CommandManager(Object parameter) {
        commandsUser.put("help", new Help(parameter));
        commandsUser.put("clear", new Clear(parameter));
        commandsUser.put("info", new Info(parameter));
        commandsUser.put("show", new Show(parameter));
        commandsUser.put("add", new Add(parameter));
        commandsUser.put("back", new Back(parameter));
        commandsUser.put("update", new Update(parameter));
        commandsUser.put("remove_by_id", new RemoveById(parameter));
        commandsUser.put("execute_script", new ExecuteScript(parameter));
        commandsUser.put("history", new History(parameter));
        commandsUser.put("exit", new Exit(parameter));
        commandsUser.put("add_if_min", new AddIfMin(parameter));
        commandsUser.put("remove_greater", new RemoveGreater(parameter));
        commandsUser.put("sum_of_number_of_participants", new SumOfNumberOfParticipants(parameter));
        commandsUser.put("average_of_number_of_participants", new AverageOfNumberOfParticipants(parameter));
        commandsUser.put("login", new Login(parameter));
        commandsUser.put("register", new Register(parameter));
    }
    /**
     * Запуск команды
     *
     * @param cm название команды
     */
    public CommandClient getCommand(String cm) {
        if (commandsUser.containsKey(cm)) {
            return commandsUser.get(cm);
        } else {
            return new CommandClient(null);
        }
    }
}
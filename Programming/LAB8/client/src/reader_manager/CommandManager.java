package reader_manager;


import commands.*;
import commands.admin.*;
import commands.auth.*;
import commands.collection.*;
import commands.other.*;

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
     * @param params параметр, который передаётся команде в командной строке
     */
    public CommandManager(String[] params) {
        commandsUser.put("help", new Help(params));
        commandsUser.put("clear", new Clear(params));
        commandsUser.put("info", new Info(params));
        commandsUser.put("show", new Show(params));
//        commandsUser.put("add", new Add(params));
//        commandsUser.put("update", new Update(params));
        commandsUser.put("remove_by_id", new RemoveById(params));
        commandsUser.put("execute_script", new ExecuteScript(params));
        commandsUser.put("history", new History(params));
        commandsUser.put("exit", new Exit(params));
//        commandsUser.put("add_if_min", new AddIfMin(params));
//        commandsUser.put("remove_greater", new RemoveGreater(params));
        commandsUser.put("sum_of_number_of_participants", new SumOfNumberOfParticipants(params));
        commandsUser.put("average_of_number_of_participants", new AverageOfNumberOfParticipants(params));
        commandsUser.put("filter_contains_name", new FilterContainsName(params));
        commandsUser.put("login", new Login(params));
        commandsUser.put("register", new Register(params));
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
package reader_manager;


import commands.*;
import java.util.HashMap;
/**
 * Менеджер команд - управляет созданием и выполнением команд.
 */
public class CommandManager {
    /**
     * HashMap всех команд, которые используются в программе
     */
    public static HashMap<String, Command> commands = new HashMap<>();
    /**
     * Добавление всех команд в {@code commands}
     *
     * @param parameter параметр, который передаётся команде в командной строке
     */
    public CommandManager(Object parameter) {
        commands.put("help", new Help(parameter));
        commands.put("clear", new Clear(parameter));
        commands.put("info", new Info(parameter));
        commands.put("show", new Show(parameter));
        commands.put("add", new Add(parameter));
        commands.put("back", new Back(parameter));
        commands.put("update", new Update(parameter));
        commands.put("remove_by_id", new RemoveById(parameter));
        commands.put("save", new Save(parameter));
        commands.put("execute_script", new ExecuteScript(parameter));
        commands.put("history", new History(parameter));
        commands.put("exit", new Exit(parameter));
        commands.put("add_if_min", new AddIfMin(parameter));
        commands.put("remove_greater", new RemoveGreater(parameter));
        commands.put("sum_of_number_of_participants", new SumOfNumberOfParticipants(parameter));
        commands.put("average_of_number_of_participants", new AverageOfNumberOfParticipants(parameter));
        commands.put("filter_contains_name", new FilterContainsName(parameter));
    }
    /**
     * Запуск команды
     *
     * @param cm название команды
     */
    public void runCommand(String cm) {
        Command command;
        if (commands.containsKey(cm)) {
            command = commands.get(cm);
            command.validate();
        } else {
            command = new Command();
        }
        command.execute();
    }
}
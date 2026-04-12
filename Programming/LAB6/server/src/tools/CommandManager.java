package tools;


import commands.*;

public class CommandManager {
    private CollectionManager collectionManager;

    public CommandManager(CollectionManager cm) {
        this.collectionManager = cm;
    }

    public String execute(String cm, Object param, Object obj) {

        return switch (cm) {
            case "help" -> {
                Help help = new Help(collectionManager);
                yield help.execute();
            }
            case "clear" -> {
                Clear clear = new Clear(collectionManager);
                yield clear.execute();
            }
            case "info" -> {
                Info info = new Info(collectionManager);
                yield info.execute();
            }
            case "show" -> {
                Show show = new Show(collectionManager);
                yield show.execute();
            }
            case "add" -> {
                Add add = new Add(collectionManager);
                if (add.validateParams(obj)) yield add.execute(obj);
                throw new RuntimeException("Ошибка сервера: Получены некорректные или поврежденные данные объекта MusicBand.");
            }
            case "back" -> {
                Back back = new Back(collectionManager);
                if (back.validateParams(param)) yield back.execute(param);
                throw new RuntimeException("Ошибка сервера: n - не положительное целое число.");
            }
            case "update" -> {
                Update update = new Update(collectionManager);
                if (update.validateParams(param, obj)) yield update.execute(param, obj);
                throw new RuntimeException("Ошибка сервера: Получены некорректные или поврежденные данные");
            }
            case "remove_by_id" -> {
                RemoveById removeById = new RemoveById(collectionManager);
                if (removeById.validateParams(param)) yield removeById.execute(param);
                throw new RuntimeException("Ошибка сервера: id - не положительное целое число.");
            }
            case "history" -> {
                History history = new History(collectionManager);
                yield history.execute();
            }
            case "add_if_min" -> {
                AddIfMin addIfMin = new AddIfMin(collectionManager);
                if (addIfMin.validateParams(obj)) yield addIfMin.execute(obj);
                throw new RuntimeException("Ошибка сервера: Получены некорректные или поврежденные данные объекта MusicBand.");
            }
            case "remove_greater" -> {
                RemoveGreater removeGreater = new RemoveGreater(collectionManager);
                if (removeGreater.validateParams(obj)) yield removeGreater.execute(obj);
                throw new RuntimeException("Ошибка сервера: Получены некорректные или поврежденные данные объекта MusicBand.");
            }
            case "sum_of_number_of_participants" -> {
                SumOfNumberOfParticipants sumOfNumberOfParticipants = new SumOfNumberOfParticipants(collectionManager);
                yield sumOfNumberOfParticipants.execute();
            }
            case "average_of_number_of_participants" -> {
                AverageOfNumberOfParticipants averageOfNumberOfParticipants = new AverageOfNumberOfParticipants(collectionManager);
                yield averageOfNumberOfParticipants.execute();
            }
            case "filter_contains_name" -> {
                FilterContainsName filterContainsName = new FilterContainsName(collectionManager);
                if (filterContainsName.validateParams(param)) yield filterContainsName.execute(param);
                throw new RuntimeException("Ошибка сервера: Получены некорректные или поврежденные данные подстроки.");
            }
            default -> "Такой команды нет! Используйте команду help, чтобы посмотреть список команд\n";
        };
    }
}
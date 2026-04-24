package managers;


import commands.auth.Login;
import commands.auth.Register;
import commands.collection.*;

public class CommandManager {
    private ServerManagers sm;

    public CommandManager(ServerManagers sm) {
        this.sm = sm;
    }

    public String execute(String cm, Object param, Object obj) {

        return switch (cm) {
            case "help" -> {
                Help help = new Help(sm.collectionManager);
                yield help.execute();
            }
            case "clear" -> {
                Clear clear = new Clear(sm.collectionManager);
                yield clear.execute();
            }
            case "info" -> {
                Info info = new Info(sm.collectionManager);
                yield info.execute();
            }
            case "show" -> {
                Show show = new Show(sm.collectionManager);
                yield show.execute();
            }
            case "add" -> {
                Add add = new Add(sm.collectionManager);
                if (add.validateParams(obj)) yield add.execute(obj);
                throw new RuntimeException("Ошибка сервера: Получены некорректные или поврежденные данные объекта MusicBand.");
            }
            case "back" -> {
                Back back = new Back(sm.collectionManager);
                if (back.validateParams(param)) yield back.execute(param);
                throw new RuntimeException("Ошибка сервера: n - не положительное целое число.");
            }
            case "update" -> {
                Update update = new Update(sm.collectionManager);
                if (update.validateParams(param, obj)) yield update.execute(param, obj);
                throw new RuntimeException("Ошибка сервера: Получены некорректные или поврежденные данные");
            }
            case "remove_by_id" -> {
                RemoveById removeById = new RemoveById(sm.collectionManager);
                if (removeById.validateParams(param)) yield removeById.execute(param);
                throw new RuntimeException("Ошибка сервера: id - не положительное целое число.");
            }
            case "history" -> {
                History history = new History(sm.collectionManager);
                yield history.execute();
            }
            case "add_if_min" -> {
                AddIfMin addIfMin = new AddIfMin(sm.collectionManager);
                if (addIfMin.validateParams(obj)) yield addIfMin.execute(obj);
                throw new RuntimeException("Ошибка сервера: Получены некорректные или поврежденные данные объекта MusicBand.");
            }
            case "remove_greater" -> {
                RemoveGreater removeGreater = new RemoveGreater(sm.collectionManager);
                if (removeGreater.validateParams(obj)) yield removeGreater.execute(obj);
                throw new RuntimeException("Ошибка сервера: Получены некорректные или поврежденные данные объекта MusicBand.");
            }
            case "sum_of_number_of_participants" -> {
                SumOfNumberOfParticipants sumOfNumberOfParticipants = new SumOfNumberOfParticipants(sm.collectionManager);
                yield sumOfNumberOfParticipants.execute();
            }
            case "average_of_number_of_participants" -> {
                AverageOfNumberOfParticipants averageOfNumberOfParticipants = new AverageOfNumberOfParticipants(sm.collectionManager);
                yield averageOfNumberOfParticipants.execute();
            }
            case "filter_contains_name" -> {
                FilterContainsName filterContainsName = new FilterContainsName(sm.collectionManager);
                if (filterContainsName.validateParams(param)) yield filterContainsName.execute(param);
                throw new RuntimeException("Ошибка сервера: Получены некорректные или поврежденные данные подстроки.");
            }
            case "login" -> {
                Login login = new Login(sm.userManager);
                if (login.validateParams(obj)) yield login.execute(obj);
                throw new RuntimeException("Ошибка сервера: Получены некорректные или поврежденные данные объекта User.");
            }
            case "register" -> {
                Register register = new Register(sm.userManager);
                if (register.validateParams(obj)) yield register.execute(obj);
                throw new RuntimeException("Ошибка сервера: Получены некорректные или поврежденные данные объекта User.");
            }
            default -> "Такой команды нет! Используйте команду help, чтобы посмотреть список команд\n";
        };
    }
}
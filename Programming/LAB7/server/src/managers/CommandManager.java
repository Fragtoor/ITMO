package managers;


import commands.auth.Login;
import commands.auth.Register;
import commands.collection.*;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import dao.DBManager;

public class CommandManager {
    private CollectionManager cm;
    private final User user;
    private final DBManager db;

    public CommandManager(DBManager db, User user) {
        this.db = db;
        this.user = user;
    }

    public CommandManager(CollectionManager cm, DBManager db, User user) {
        this.cm = cm;
        this.db = db;
        this.user = user;
    }

    public Response executeCollectionCommand(String nameCommand, Object param, Object obj) {
        return switch (nameCommand) {
            case "help" -> {
                Help help = new Help(user);
                yield help.execute(cm, db);
            }
            case "clear" -> {
                Clear clear = new Clear(user);
                yield clear.execute(cm, db);
            }
            case "info" -> {
                Info info = new Info(user);
                yield info.execute(cm, db);
            }
            case "show" -> {
                Show show = new Show(user);
                yield show.execute(cm, db);
            }
            case "add" -> {
                Add add = new Add(user);
                if (add.validateParams(obj)) yield add.execute(cm, db, obj);
                yield new Response(ResponseType.COMMAND_ERROR, "Получены некорректные или поврежденные данные объекта MusicBand.");
            }
            case "back" -> {
                Back back = new Back(user);
                if (back.validateParams(param)) yield back.execute(cm, db, param);
                yield new Response(ResponseType.COMMAND_ERROR, "n - не положительное целое число.");
            }
            case "update" -> {
                Update update = new Update(user);
                if (update.validateParams(param, obj)) yield update.execute(cm, db, param, obj);
                yield new Response(ResponseType.COMMAND_ERROR, "Получены некорректные или поврежденные данные");
            }
            case "remove_by_id" -> {
                RemoveById removeById = new RemoveById(user);
                if (removeById.validateParams(param)) yield removeById.execute(cm, db, param);
                yield new Response(ResponseType.COMMAND_ERROR, "id - не положительное целое число.");
            }
            case "history" -> {
                History history = new History(user);
                yield history.execute(cm, db);
            }
            case "add_if_min" -> {
                AddIfMin addIfMin = new AddIfMin(user);
                if (addIfMin.validateParams(obj)) yield addIfMin.execute(cm, db, obj);
                yield new Response(ResponseType.COMMAND_ERROR, "Получены некорректные или поврежденные данные объекта MusicBand.");
            }
            case "remove_greater" -> {
                RemoveGreater removeGreater = new RemoveGreater(user);
                if (removeGreater.validateParams(obj)) yield removeGreater.execute(cm, db, obj);
                yield new Response(ResponseType.COMMAND_ERROR, "Получены некорректные или поврежденные данные объекта MusicBand.");
            }
            case "sum_of_number_of_participants" -> {
                SumOfNumberOfParticipants sumOfNumberOfParticipants = new SumOfNumberOfParticipants(user);
                yield sumOfNumberOfParticipants.execute(cm, db);
            }
            case "average_of_number_of_participants" -> {
                AverageOfNumberOfParticipants averageOfNumberOfParticipants = new AverageOfNumberOfParticipants(user);
                yield averageOfNumberOfParticipants.execute(cm, db);
            }
            case "filter_contains_name" -> {
                FilterContainsName filterContainsName = new FilterContainsName(user);
                if (filterContainsName.validateParams(param)) yield filterContainsName.execute(cm, db, param);
                yield new Response(ResponseType.COMMAND_ERROR, "Получены некорректные или поврежденные данные подстроки.");
            }
            case "login" -> {
                Login login = new Login(user, db);
                yield login.execute(cm);
            }
            case "register" -> {
                Register register = new Register(user, db);
                yield register.execute(cm);
            }
            default -> new Response(ResponseType.COMMAND_ERROR, "Такой команды нет! Используйте команду help, чтобы посмотреть список команд");
        };
    }

    public Response executeAuthCommand(String nameCommand) {
        return switch (nameCommand) {
            case "login" -> {
                Login login = new Login(user, db);
                yield login.execute(cm);
            }
            case "register" -> {
                Register register = new Register(user, db);
                yield register.execute(cm);
            }
            default -> new Response(ResponseType.COMMAND_ERROR, "Такой команды нет! Используйте команду help, чтобы посмотреть список команд");
        };
    }
}
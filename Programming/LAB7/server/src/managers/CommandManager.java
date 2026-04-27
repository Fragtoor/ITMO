package managers;


import commands.auth.Login;
import commands.auth.Register;
import commands.collection.*;
import common.general.Response;
import common.general.ResponseType;
import common.general.User;
import dao.DAO;

public class CommandManager {
    private final ServerManagers sm;
    private final User user;
    private final DAO dao;

    public CommandManager(ServerManagers sm, DAO dao, User user) {
        this.sm = sm;
        this.dao = dao;
        this.user = user;
    }

    public Response execute(String cm, Object param, Object obj) {
        return switch (cm) {
            case "help" -> {
                Help help = new Help(user);
                yield help.execute(sm.collectionManager, dao);
            }
            case "clear" -> {
                Clear clear = new Clear(user);
                yield clear.execute(sm.collectionManager, dao);
            }
            case "info" -> {
                Info info = new Info(user);
                yield info.execute(sm.collectionManager, dao);
            }
            case "show" -> {
                Show show = new Show(user);
                yield show.execute(sm.collectionManager, dao);
            }
            case "add" -> {
                Add add = new Add(user);
                if (add.validateParams(obj)) yield add.execute(sm.collectionManager, dao, obj);
                yield new Response(ResponseType.COMMAND_ERROR, "Получены некорректные или поврежденные данные объекта MusicBand.");
            }
            case "back" -> {
                Back back = new Back(user);
                if (back.validateParams(param)) yield back.execute(sm.collectionManager, dao, param);
                yield new Response(ResponseType.COMMAND_ERROR, "n - не положительное целое число.");
            }
            case "update" -> {
                Update update = new Update(user);
                if (update.validateParams(param, obj)) yield update.execute(sm.collectionManager, dao, param, obj);
                yield new Response(ResponseType.COMMAND_ERROR, "Получены некорректные или поврежденные данные");
            }
            case "remove_by_id" -> {
                RemoveById removeById = new RemoveById(user);
                if (removeById.validateParams(param)) yield removeById.execute(sm.collectionManager, dao, param);
                yield new Response(ResponseType.COMMAND_ERROR, "id - не положительное целое число.");
            }
            case "history" -> {
                History history = new History(user);
                yield history.execute(sm.collectionManager, dao);
            }
            case "add_if_min" -> {
                AddIfMin addIfMin = new AddIfMin(user);
                if (addIfMin.validateParams(obj)) yield addIfMin.execute(sm.collectionManager, dao, obj);
                yield new Response(ResponseType.COMMAND_ERROR, "Получены некорректные или поврежденные данные объекта MusicBand.");
            }
            case "remove_greater" -> {
                RemoveGreater removeGreater = new RemoveGreater(user);
                if (removeGreater.validateParams(obj)) yield removeGreater.execute(sm.collectionManager, dao, obj);
                yield new Response(ResponseType.COMMAND_ERROR, "Получены некорректные или поврежденные данные объекта MusicBand.");
            }
            case "sum_of_number_of_participants" -> {
                SumOfNumberOfParticipants sumOfNumberOfParticipants = new SumOfNumberOfParticipants(user);
                yield sumOfNumberOfParticipants.execute(sm.collectionManager, dao);
            }
            case "average_of_number_of_participants" -> {
                AverageOfNumberOfParticipants averageOfNumberOfParticipants = new AverageOfNumberOfParticipants(user);
                yield averageOfNumberOfParticipants.execute(sm.collectionManager, dao);
            }
            case "filter_contains_name" -> {
                FilterContainsName filterContainsName = new FilterContainsName(user);
                if (filterContainsName.validateParams(param)) yield filterContainsName.execute(sm.collectionManager, dao, param);
                yield new Response(ResponseType.COMMAND_ERROR, "Получены некорректные или поврежденные данные подстроки.");
            }
            case "login" -> {
                Login login = new Login(user);
                yield login.execute(sm.userManager, dao, obj);
            }
            case "register" -> {
                Register register = new Register(user);
                yield register.execute(sm.userManager, dao, obj);
            }
            default -> new Response(ResponseType.COMMAND_ERROR, "Такой команды нет! Используйте команду help, чтобы посмотреть список команд");
        };
    }
}
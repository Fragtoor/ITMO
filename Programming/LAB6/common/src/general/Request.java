package general;

import java.io.Serializable;

public class Request<T, S> implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String commandName;
    private final T argumentParam;
    private final S argumentObject;
    private final boolean fromTheFile;

    public Request(String commandName, T argumentParam, S argumentObject, boolean fromTheFile) {
        this.commandName = commandName;
        this.argumentParam = argumentParam;
        this.argumentObject = argumentObject;
        this.fromTheFile = fromTheFile;
    }

    public String getCommandName() {
        return commandName;
    }

    public T getArgumentParam() {
        return argumentParam;
    }

    public S getArgumentObject() {
        return argumentObject;
    }

    public boolean getFromTheFile() {
        return fromTheFile;
    }
}
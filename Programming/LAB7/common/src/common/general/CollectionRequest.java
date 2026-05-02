package common.general;

public class CollectionRequest<T, S> extends Request {
    private static final long serialVersionUID = 1L;
    private final T argumentParam;
    private final S argumentObject;
    private final boolean fromTheFile;

    public CollectionRequest(User user, String commandName, T argumentParam, S argumentObject, boolean fromTheFile) {
        super(user, commandName);
        this.argumentParam = argumentParam;
        this.argumentObject = argumentObject;
        this.fromTheFile = fromTheFile;
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

package commands;
import common.general.Request;

import java.io.Serializable;

public class CommandClient implements Serializable {
    private Object parameter;
    private boolean fromTheFile;

    public CommandClient(Object parameter) {
        this.parameter = parameter;
    }

    public void validate() {
    }

    public Request<?, ?> toRequest() {
        return new Request<>("command", null, null, fromTheFile);
    }

    public Object getParameter() {
        return parameter;
    }

    public boolean getFromTheFile() {
        return fromTheFile;
    }

    public void setFromTheFile(boolean fromTheFile) {
        this.fromTheFile = fromTheFile;
    }

}

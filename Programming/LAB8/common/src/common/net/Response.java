package common.net;

import java.io.Serializable;

public class Response implements Serializable {
    private final ResponseType type;
    private final String message;
    private final String details;

    public Response(ResponseType type, String message) {
        this.type = type;
        this.message = message;
        details = "";
    }

    public Response(ResponseType type, String message, String details) {
        this.type = type;
        this.message = message;
        this.details = details;
    }

    public ResponseType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
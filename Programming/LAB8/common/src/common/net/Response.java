package common.net;

import java.io.Serializable;

public class Response implements Serializable {
    private final Object obj;
    private final ResponseType type;
    private final String message;

    private Response(Builder builder) {
        this.obj = builder.obj;
        this.type = builder.type;
        this.message = builder.message;
    }

    public ResponseType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }


    public Object getObj() {
        return obj;
    }

    public static class Builder {
        private Object obj;
        private final ResponseType type;
        private String message = "";

        public Builder(ResponseType type) {
            this.type = type;
        }

        public Builder obj(Object obj) {
            this.obj = obj;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Response build() {
            return new Response(this);
        }
    }
}
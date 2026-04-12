package util;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

public class ConnectionContext {
    public enum Type { CLIENT, SERVER }
    public final ByteBuffer buffer = ByteBuffer.allocate(16384);
    private final Type type;
    public SelectionKey partnerKey;

    public ConnectionContext(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public SelectionKey getPartner() {
        return partnerKey;
    }

    public void setPartner(SelectionKey partnerKey) {
        this.partnerKey = partnerKey;
    }
}
package util;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;

public class ServerContext {
    private final InetSocketAddress address;
    private SelectionKey key;
    private boolean isOnline;
    private int loadCount;

    public ServerContext(InetSocketAddress address) {
        this.address = address;
        this.key = null;
        this.isOnline = false;
        this.loadCount = 0;
    }

    public InetSocketAddress getAddress() {return address;}

    public SelectionKey getKey() {return key;}

    public void setKey(SelectionKey key) {this.key = key;}

    public boolean getIsOnline() {return isOnline;}

    public int getLoadCount() {return loadCount;}

    public void setLoadCount(int loadCount) {this.loadCount = loadCount;}

    public void setIsOnline(boolean isOnline) {this.isOnline = isOnline;}

    public String toString() {

        return "address: " +
                getAddress() +
                "\n" +
                "channel: " +
                getKey() +
                "\n" +
                "isOnline: " +
                getIsOnline() +
                "\n" +
                "loadCount: " +
                getLoadCount() +
                "\n";
    }

}
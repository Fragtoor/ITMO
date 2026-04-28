package managers;

public class ServerManagers {
    public final CollectionManager collectionManager;
    public final UserManager userManager;

    public ServerManagers(CollectionManager cm, UserManager um) {
        this.collectionManager = cm;
        this.userManager = um;
    }

}

package common.net;

import java.io.Serializable;


public class User implements Serializable {
    private static final long serialVersionUID = 2L;
    private String userName;
    private String login;
    private String password;
    private boolean confirm;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User(String userName, String login, String password) {
        this.userName = userName;
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return this.login;
    }

    public String getPassword() {
        return this.password;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    public boolean isConfirm() {
        return this.confirm;
    }

}

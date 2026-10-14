package dev.zeevenn.ioc.lesson02.xmlconfig;

public class UserDao {

    private final String url;
    private final String username;
    private final String password;
    private final int connectionTimeoutSeconds;

    public UserDao(String url, String username, String password, int connectionTimeoutSeconds) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.connectionTimeoutSeconds = connectionTimeoutSeconds;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getConnectionTimeoutSeconds() {
        return connectionTimeoutSeconds;
    }

    public String findName() {
        return "Ada";
    }
}

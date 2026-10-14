package dev.zeevenn.ioc.lesson03.annotationconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

    private final String url;
    private final String username;
    private final String password;
    private final int connectionTimeoutSeconds;

    public UserDao(
            @Value("${database.url}") String url,
            @Value("${database.username}") String username,
            @Value("${database.password}") String password,
            @Value("${database.connection-timeout-seconds}") int connectionTimeoutSeconds) {
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

package dev.zeevenn.ioc.lesson05.autowiring.dao.impl;

import dev.zeevenn.ioc.lesson05.autowiring.dao.UserDao;
import org.springframework.stereotype.Repository;

@Repository("memoryUserDao")
public class MemoryUserDao implements UserDao {

    @Override
    public String findName() {
        return "Ada (memory)";
    }
}

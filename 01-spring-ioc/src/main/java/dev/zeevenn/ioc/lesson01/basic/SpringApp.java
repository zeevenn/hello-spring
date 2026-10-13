package dev.zeevenn.ioc.lesson01.basic;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * SpringApp
 */
public class SpringApp {

    public static void main(String[] args) {
        try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("lesson01/basic/beans.xml")) {
            UserService userService = context.getBean("userService", UserService.class);
            System.out.println(userService.findName());
        }
    }
}

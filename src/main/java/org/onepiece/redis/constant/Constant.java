package org.onepiece.redis.constant;

import org.onepiece.redis.model.User;

import java.util.HashMap;
import java.util.Map;

public class Constant {
    private static final Map<Integer, User> userTable = new HashMap<>();
    static {
        userTable.put(1, new User("manman", "521", 1));
        userTable.put(2, new User("luffy", "123", 2));
    }

    public static Map<Integer, User> getUserTable() {
        return userTable;
    }

    public static void addUser(User user) {
        userTable.put(user.getId(), user);
    }
}

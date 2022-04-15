package com.goncaloacbs.rest.webservices.restfulwebservices.user;


import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserDaoService {
    private static List<User> users = new ArrayList<>();

    private static int usersCount = 3;

    static {
        users.add(new User(1, "Usr1", new Date()));
        users.add(new User(2, "Usr2", new Date()));
        users.add(new User(3, "Usr3", new Date()));
    }

    public List<User> findAll() {
        return users;
    }

    public User findOne(int id) {
        for (User user: users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;

    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(++usersCount);
        }
        users.add(user);
        return user;
    }


    public User deleteUserById(int id) {
        Iterator<User> iterator = users.iterator();

        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user.getId() == id) {
                users.remove(user);
                return user;
            }
        }

        return null;
    }

}

package tech.itpark.http.service;

import com.google.gson.Gson;
import tech.itpark.http.model.User;
import tech.itpark.http.model.infrastructure.HttpRequest;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final Gson gson;

    public UserService(Gson gson) {
        this.gson = gson;
    }

    public List<User> getAllUsers() {
        final List<User> users = new ArrayList<>();

        User userKot = new User();
        userKot.setName("Kot");
        userKot.setLastName("Mew");
        userKot.setAge(3);
        users.add(userKot);

        User userIvan = new User();
        userKot.setName("Ivan");
        userKot.setLastName("Petrov");
        userKot.setAge(33);
        users.add(userIvan);

        User userPetr = new User();
        userPetr.setName("Petr");
        userPetr.setLastName("Ivanov");
        userPetr.setAge(66);
        userPetr.setRelatives(List.of(userKot));
        users.add(userPetr);

        return users;
    }

    public List<User> getAllUsersWithCopycat(HttpRequest httpRequest) {
        final List<User> allUsers = getAllUsers();
        allUsers.add(gson.fromJson(new String(httpRequest.getBody()), User.class));
        return allUsers;
    }
}

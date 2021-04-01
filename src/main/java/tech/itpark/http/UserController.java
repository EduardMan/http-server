package tech.itpark.http;

import org.springframework.stereotype.Controller;
import tech.itpark.http.annotation.GetMapping;
import tech.itpark.http.annotation.PostMapping;
import tech.itpark.http.annotation.header.RequestBody;
import tech.itpark.http.annotation.header.RequestHeader;
import tech.itpark.http.model.User;
import tech.itpark.http.model.infrastructure.HttpRequest;
import tech.itpark.http.service.UserService;

import java.util.List;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/users")
    public List<User> getAll(HttpRequest httpRequest) {
        return userService.getAllUsers();
    }

    @GetMapping(path = "/bytes")
    public byte[] getBytes(HttpRequest httpRequest) {
        return "Does it work?".getBytes();
    }

    @PostMapping(path = "/hey")
    public List<User> addCopycatUser(HttpRequest httpRequest) {
        return userService.getAllUsersWithCopycat(httpRequest);
    }

    @GetMapping(path = "/headers-test")
    public String headerTest(@RequestHeader("testHeader") String testHeader) {
        return testHeader;
    }

    @PostMapping(path = "/headers-test")
    public String headerTest(@RequestHeader("testHeader") String testHeader,
                             @RequestBody User user) {
        System.out.println(user);
        return testHeader;
    }
}

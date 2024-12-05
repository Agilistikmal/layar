package zip.agil.layar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zip.agil.layar.entity.User;
import zip.agil.layar.model.WebResponse;
import zip.agil.layar.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/")
    public WebResponse<List<User>> findAll() {
        List<User> users = userService.findAll();

        WebResponse<List<User>> response = new WebResponse<>();
        response.setData(users);

        return response;
    }
}

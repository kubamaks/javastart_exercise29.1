package pl.km.exercise291.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class LoginController {

    @GetMapping("/logowanie")
    public String longin() {
        return "login-form";
    }
}

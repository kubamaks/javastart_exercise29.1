package pl.km.exercise291.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import static pl.km.exercise291.templates.ViewTemplates.ACCOUNT_DELETION_SUCCESS_MESSAGE;
import static pl.km.exercise291.templates.ViewTemplates.PASSWORD_CHANGE_SUCCESS_MESSAGE;

@Controller
public class HomeController {

    @GetMapping("/")
    String home(@RequestParam(name = "status", required = false) String status, Model model) {
        if (status != null) {
            if (status.equals("kontousuniete")) {
                model.addAttribute("message", ACCOUNT_DELETION_SUCCESS_MESSAGE);
            }
            if (status.equals("haslozmienione")) {
                model.addAttribute("message", PASSWORD_CHANGE_SUCCESS_MESSAGE);
            }
        }
        return "index";
    }
}

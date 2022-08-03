package pl.km.exercise291.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.km.exercise291.user.UserService;
import pl.km.exercise291.user.dto.UserRegistrationDto;
import pl.km.exercise291.user.UserOperationValidator;
import static pl.km.exercise291.templates.ViewTemplates.*;

@Controller
class RegistrationController {
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/rejestracja")
    public String registration(Model model) {
        UserRegistrationDto createdUser = new UserRegistrationDto();
        model.addAttribute("user", createdUser);
        return "registration-form";
    }

    @PostMapping("/zarejestruj")
    public String saveNewUser(
            @ModelAttribute("user") UserRegistrationDto user,
            Model model,
            RedirectAttributes redirectAttrs) {
        UserOperationValidator validator = userService.getRegistrationValidation(user);
        if (!validator.isValidated()) {
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", validator.getMessage());
            return "registration-form";
        }
        userService.register(user);
        redirectAttrs.addFlashAttribute("message", REGISTRATION_SUCCESS_MESSAGE);
        return "redirect:/";
    }

}

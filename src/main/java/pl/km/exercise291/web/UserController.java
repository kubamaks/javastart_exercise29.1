package pl.km.exercise291.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.km.exercise291.user.UserService;
import pl.km.exercise291.user.dto.UserDto;
import pl.km.exercise291.user.UserOperationValidator;
import static pl.km.exercise291.templates.ViewTemplates.*;

@RequestMapping("/uzytkownik")
@Controller
class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String showUserPanel(Model model) {
        UserDto user = userService.findCurrentUserDto();
        model.addAttribute("user", user);
        return "user-panel";
    }

    @GetMapping("/aktualizacja")
    public String editData(Model model) {
        UserDto user = userService.findCurrentUserDto();
        model.addAttribute("user", user);
        return "actualization-form";
    }

    @PostMapping("/aktualizuj")
    public String updateUserData(
            @ModelAttribute("user") UserDto user,
            Model model,
            RedirectAttributes redirectAttrs) {
        UserOperationValidator validator = userService.getUpdateValidation(user);
        if (!validator.isValidated()) {
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", validator.getMessage());
            return "actualization-form";
        }
        userService.update(user);
        redirectAttrs.addFlashAttribute("message", USER_UPDATE_SUCCESS_MESSAGE);
        return "redirect:/";
    }

    @GetMapping("/zmianahasla")
    public String changePassword() {
        return "password-form";
    }

    @PostMapping("/zmienhaslo")
    public String updatePassword(@RequestParam(name = "password") String password,
                                 Model model) {
        UserOperationValidator validator = userService.getPasswordValidation(password);
        if (!validator.isValidated()) {
            model.addAttribute("errorMessage", validator.getMessage());
            return "password-form";
        }
        userService.changePassword(password);
        return "redirect:/wylogowano?status=haslozmienione";
    }

    @GetMapping("potwierdzusuniecie")
    public String accountRemovingConfirmation() {
        return "current-user-delete-confirmation-form";
    }

    @GetMapping("usun")
    public String removeAccountForCurrentUser() {
        userService.removeCurrentUser();
        return "redirect:/wylogowano?status=kontousuniete";
    }

}

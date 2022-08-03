package pl.km.exercise291.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.km.exercise291.user.UserService;

import java.util.NoSuchElementException;

import static pl.km.exercise291.templates.ViewTemplates.*;

@Controller
@RequestMapping("/admin")
class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String getAdminPanel(Model model) {
        model.addAttribute("users", userService.allUsersBesidesCurrent());
        return "admin-panel";
    }

    @GetMapping("/{id}/zmienuprawnienia")
    public String changeUserAdminRoleStatus(
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttrs) {
        userService.changeUserAdminRoleStatusById(id);
        redirectAttrs.addFlashAttribute("message", ACCOUNT_STATUS_CHANGE_SUCCESS_MESSAGE);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/usun")
    public String deleteUserById(
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttrs) {
        try {
            userService.deleteUserById(id);
        } catch (NoSuchElementException e) {
            redirectAttrs.addFlashAttribute("errorMessage", NO_USER_FOUND_BY_ID);
            return "redirect:/admin";
        }
        redirectAttrs.addFlashAttribute("message", ACCOUNT_DELETION_SUCCESS_MESSAGE);
        return "redirect:/admin";
    }
}

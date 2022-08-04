package pl.km.exercise291.web;

import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static pl.km.exercise291.templates.ViewTemplates.*;

@Controller
public class CustomLogoutController {

    @GetMapping("/wylogowano")
    public String performCustomLogout(
            @RequestParam(name = "status", required = false) String status,
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttr) {
        CookieClearingLogoutHandler cookieClearingLogoutHandler = new CookieClearingLogoutHandler(
                AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY);
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        cookieClearingLogoutHandler.logout(request, response, null);
        securityContextLogoutHandler.logout(request, response, null);
        if (status.equals("kontousuniete")) {
            redirectAttr.addFlashAttribute("message", ACCOUNT_DELETION_SUCCESS_MESSAGE);
        }
        if (status.equals("haslozmienione")) {
            redirectAttr.addFlashAttribute("message", PASSWORD_CHANGE_SUCCESS_MESSAGE);
        }
        return "redirect:/";
    }

}

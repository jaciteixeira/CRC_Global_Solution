package edu.opengroup.crc.controller;

import edu.opengroup.crc.repository.AuthRepository;
import edu.opengroup.crc.repository.MoradorRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
@Controller
public class AuthController {

    @Autowired
    AuthRepository authRepository;
    @Autowired
    MoradorRepository moradorRepository;

    @GetMapping("/teste")
    public String teste(){
        return "teste";
    }

    @GetMapping("/")
    public String index(HttpSession session ) {
        if (session.getAttribute("user") != null) {
            return "redirect:/home";
        }
        return "index";
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        if (session.getAttribute("user") != null ) {
            return "redirect:/home";
        }
        return "login";
    }

    @GetMapping("/home")
    public ModelAndView transfToHome(HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() &&
                !"anonymousUser".equals(auth.getName());

        assert auth != null;
        String username = auth.getName();
        var authUser = authRepository.findByEmail(username);
        var morador = moradorRepository.findByAuthUser(authUser.get());

        ModelAndView mv = new ModelAndView("home");
        mv.addObject("user", morador);
        mv.addObject("isAuthenticated", isAuthenticated);
        session.setAttribute("user", morador);

        var role = morador.getAuthUser().getRole();
        mv.addObject("role", role);

        return mv;
    }


}

package edu.opengroup.crc.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public String handleUsernameNotFoundException(
            HttpServletRequest request,
            RedirectAttributes redirectAttributes,
            UsernameNotFoundException e) {
        redirectAttributes.addFlashAttribute("error_user_not_found", e.getMessage());
        System.out.println("Usuario não encontrado!");
        return "redirect:/login";
    }

    @ExceptionHandler(UsernameInactiveException.class)
    public String handleUsernameInactiveException(
            UsernameInactiveException e,
            RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("error_user_inactive", e.getMessage());
        System.out.println("Usuario inactivado!");
        return "redirect:/login";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgumentException(IllegalArgumentException ex) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }

}

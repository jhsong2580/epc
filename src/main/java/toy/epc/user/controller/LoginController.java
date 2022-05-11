package toy.epc.user.controller;

import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import toy.epc.user.domain.User;
import toy.epc.user.form.LoginForm;
import toy.epc.user.form.RegisterForm;
import toy.epc.user.service.LoginService;
import toy.epc.user.service.UserService;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(@ModelAttribute LoginForm loginForm) {
        return "user/login";
    }

    @PostMapping("/login")
    public String validateLogin(@Valid @ModelAttribute LoginForm loginForm, BindingResult bindingResult, HttpServletResponse response,
        @RequestParam(defaultValue = "/") String redirect) {
        if (bindingResult.hasErrors()) {
            return "user/login";
        }
        User user = loginService.checkValidateUser(loginForm);
        if (user == null) {
            bindingResult.addError(new ObjectError("LoginForm", "로그인에 실패하였습니다."));
        } else {
            loginService.addJwtTokenToCookie(response, user);
            return "redirect:" + redirect;
        }
        return "user/login";
    }

    @GetMapping("/register")
    public String registerPage(@ModelAttribute RegisterForm registerForm) {
        return "user/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute RegisterForm registerForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/register";
        }

        Optional<User> userOptional = userService.getUserByIdentification(registerForm.getIdentification());
        if (userOptional.isPresent()) {
            bindingResult.addError(new ObjectError("RegisterForm", "아이디가 중복됩니다"));
        }
        loginService.createUser(registerForm);

        return "redirect:/login";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        loginService.deleteJwtTokenFromCookie(response);
        return "redirect:/login";
    }
}

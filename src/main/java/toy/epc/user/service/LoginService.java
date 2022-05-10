package toy.epc.user.service;

import static toy.epc.user.jwtUtils.JwtConstant.JWT_EXPIRE_TIME;

import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.epc.user.domain.User;
import toy.epc.user.form.LoginForm;
import toy.epc.user.jwtUtils.JwtTokenProvider;
import toy.epc.user.repository.JpaUserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {

    private final JpaUserRepository jpaUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public User checkValidateUser(LoginForm loginForm) {
        Optional<User> userOptional = jpaUserRepository.findByIdentification(
            loginForm.getIdentification());
        if (userOptional.isEmpty() || passwordEncoder.matches(loginForm.getPassword(),
            userOptional.get().getPassword())) {
            return null;
        }
        return userOptional.get();
    }

    public void addJwtTokenToCookie(HttpServletResponse response, User user) {
        Cookie cookie = new Cookie("token", jwtTokenProvider.publishToken(user.getId()));
        cookie.setMaxAge(JWT_EXPIRE_TIME);
        response.addCookie(cookie);
    }


}

package toy.epc.user.jwtUtils;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        log.info("filter access = {}", request.getRequestURI());
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

        if (token != null && jwtTokenProvider.validateToken(token) && !urlIsWhitelist(request)) {
            SecurityContextHolder.getContext()
                .setAuthentication(jwtTokenProvider.getAuthentication(token));
        }
        checkAccessWhitelistWithValidToken(request, response, token);
        log.info("filter access end= {}", request.getRequestURI());
        filterChain.doFilter(request, response);
    }

    private void checkAccessWhitelistWithValidToken(HttpServletRequest request,
        HttpServletResponse response, String token)
        throws IOException {
        if (token != null && jwtTokenProvider.validateToken(token) && urlIsWhitelist(request)) {
            response.sendRedirect("/");
        }
    }

    private boolean urlIsWhitelist(HttpServletRequest request) {
        String[] accessWithoutToken = {"/login", "/register", "logout", "/js/", "/error"};
        for (String whitelist : accessWithoutToken) {
            if (request.getRequestURI().startsWith(whitelist)) {
                return true;
            }
        }
        return false;
    }


}

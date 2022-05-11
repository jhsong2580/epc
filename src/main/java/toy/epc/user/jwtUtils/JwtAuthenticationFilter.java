package toy.epc.user.jwtUtils;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String[] whitelist;

    private final String[] whitelistPage;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        log.info("filter access = {}", request.getRequestURI());
        String token = jwtTokenProvider.resolveToken(request);

        if (accessAuthRequirePageWithValidateToken(request, token, whitelist)) {
            SecurityContextHolder.getContext().setAuthentication(jwtTokenProvider.getAuthentication(token));
        }

        if (accessWhitelistePageWithValidateToken(request, token, whitelistPage)) {
            response.sendRedirect("/");
        }

        log.info("filter access end= {}", request.getRequestURI());
        filterChain.doFilter(request, response);
    }

    private boolean accessAuthRequirePageWithValidateToken(HttpServletRequest request, String token, String[] whitelist) {
        return token != null && jwtTokenProvider.validateToken(token) && !urlIsWhitelist(request, whitelist);
    }

    private boolean accessWhitelistePageWithValidateToken(HttpServletRequest request, String token, String[] whitelist) {
        return token != null && jwtTokenProvider.validateToken(token) && urlIsWhitelist(request, whitelist);
    }

    private boolean urlIsWhitelist(HttpServletRequest request, String[] whitelist) {
        for (String uri : whitelist) {
            if (request.getRequestURI().startsWith(uri)) {
                return true;
            }
        }
        return false;
    }
}

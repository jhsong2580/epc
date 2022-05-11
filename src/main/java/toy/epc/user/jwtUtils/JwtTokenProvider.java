package toy.epc.user.jwtUtils;

import static toy.epc.user.jwtUtils.JwtConstant.JWT_EXPIRE_TIME;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import toy.epc.user.domain.User;
import toy.epc.user.service.UserService;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserService usersService;
    private String secretKey = "thisistestusersecretkeyprojectnameismologaaaaaaaaaaaaaaaa";

    @PostConstruct
    public void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String publishToken(Long id) {
        Date now = new Date();
        Claims claims = Jwts.claims().setSubject(id.toString());

        return Jwts.builder().setHeaderParam("typ", "JWT").setClaims(claims) // 정보저장
            .setIssuedAt(now) // 토큰 발행 시간 정보
            .setExpiration(new Date(now.getTime() + JWT_EXPIRE_TIME))
            .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    public Jws<Claims> decodeToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
    }

    public String getDataFromClaim(String token, String key) {
        Jws<Claims> claimsJws = decodeToken(token);
        try {
            return String.valueOf(claimsJws.getBody().get(key));
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean validateToken(String token) {
        Boolean isValid = true;
        try {
            isValid = decodeToken(token).getBody().getExpiration().after(new Date());
        } catch (ExpiredJwtException e) {
            isValid = false;
        } catch (Exception e) {
            isValid = false;
        } finally {
            return isValid;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        try {
            Cookie[] cookies = request.getCookies();
            return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("token"))
                .findFirst().get().getValue();
        } catch (Exception e) {
            return null;
        }
    }

    public String getUserPK(String token) {
        Jws<Claims> claimsJws = decodeToken(token);
        try {
            return claimsJws.getBody().getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        try {
            Long userKey = Long.parseLong(getUserPK(token));
            ArrayList<GrantedAuthority> authorites = new ArrayList<>();

            User user = usersService.getUserById(userKey);
            authorites.add(new SimpleGrantedAuthority(user.getPower().toString()));

            return new UsernamePasswordAuthenticationToken(user.getIdentification(),
                user.getPassword(), authorites);

        } catch (Exception e) {
            return null;
        }

    }
}

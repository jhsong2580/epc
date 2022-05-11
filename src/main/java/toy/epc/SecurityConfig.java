package toy.epc;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import toy.epc.user.jwtUtils.JwtAuthenticationEntryPoint;
import toy.epc.user.jwtUtils.JwtAuthenticationFilter;
import toy.epc.user.jwtUtils.JwtTokenProvider;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final String[] whitelist = {"/login", "/register", "/logout", "/error", "/js/"};
    private final String[] whitelistPage = {"/login", "/register"};
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
            .sessionCreationPolicy(STATELESS).and().authorizeRequests().antMatchers("/login**", "/register**", "/logout**", "/error**", "/js/**")
            .permitAll().anyRequest().authenticated().and()
            .addFilterAfter(new JwtAuthenticationFilter(whitelist, whitelistPage, jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
            //  .addFilterBefore(new AccessWhitelistWithTokenRedirectHome(whitelistPage, jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
            .logout().disable().formLogin().disable();
    }
}

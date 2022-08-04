package pl.km.exercise291.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static pl.km.exercise291.templates.AppTemplates.*;

@Configuration
class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests -> requests
                .mvcMatchers("/").permitAll()
                .mvcMatchers("/rejestracja", "/zarejestruj", "/h2-console/**").permitAll()
                .mvcMatchers("/uzytkownik/***").hasAnyRole(USER_ROLE, ADMIN_ROLE)
                .mvcMatchers("/admin/**").hasAnyRole(ADMIN_ROLE)
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
        );

        http.formLogin(login -> login
                .loginPage("/logowanie")
                .loginProcessingUrl("/zaloguj")
                .defaultSuccessUrl("/", true)
                .permitAll()
        );
        http.logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/wyloguj/**", HttpMethod.GET.name()))
                .logoutSuccessHandler(new CustomLogoutSuccessHandler())
                .logoutSuccessUrl("/")
                .permitAll()
        );
        http.csrf().ignoringAntMatchers("/h2-console/**");
        http.headers().frameOptions().sameOrigin();
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().mvcMatchers(
                "/img/**",
                "/styles/**"
        );
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}

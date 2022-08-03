package pl.km.exercise291.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
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
                .anyRequest().authenticated()
        );

        http.formLogin(login -> login
                .loginPage("/logowanie")
                .loginProcessingUrl("/zaloguj")
                .defaultSuccessUrl("/", true)
                .permitAll()
        );
        http.logout(logout -> logout
                .logoutUrl("/wyloguj")
                .logoutSuccessUrl("/")
                .permitAll()
        );
        http.csrf().disable();
        http.headers().frameOptions().disable();
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

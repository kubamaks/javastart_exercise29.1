package pl.km.exercise291.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import pl.km.exercise291.user.UserService;
import pl.km.exercise291.user.dto.UserCredentialsDto;

@Service
public class CustomInMemoryUserDetailManager extends InMemoryUserDetailsManager {
    private final UserService userService;

    public CustomInMemoryUserDetailManager(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.getUserCredentialsByUserName(username)
                .map(this::createUserDetails)
                .orElseThrow(
                        () -> new UsernameNotFoundException(String.format("User with login %s not found", username))
                );
    }

    private UserDetails createUserDetails(UserCredentialsDto credentials) {
        return User.builder()
                .username(credentials.getLogin())
                .password(credentials.getPassword())
                .roles(credentials.getRoles().toArray(String[]::new))
                .build();
    }

}

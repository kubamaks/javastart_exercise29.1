package pl.km.exercise291.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.km.exercise291.user.dto.UserDto;
import pl.km.exercise291.user.dto.UserDtoWithId;
import pl.km.exercise291.user.dto.UserRegistrationDto;
import pl.km.exercise291.user.dto.UserCredentialsDto;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static pl.km.exercise291.templates.AppTemplates.*;
import static pl.km.exercise291.templates.ViewTemplates.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public Optional<UserCredentialsDto> getUserCredentialsByUserName(String login) {
        return userRepository.findByLogin(login).map(UserMapper::mapUserCredentialsDto);
    }

    public UserOperationValidator getRegistrationValidation(UserRegistrationDto user) {
        UserOperationValidator validator = new UserOperationValidator();
        if (isLoginUsed(user)) {
            validator.setValidated(false);
            validator.setMessage(UNIQUE_LOGIN_VALIDATION_FAIL_MESSAGE);
        } else if (isEmailUsed(user)) {
            validator.setValidated(false);
            validator.setMessage(UNIQUE_EMAIL_VALIDATION_FAIL_MESSAGE);
        } else if (!areAllDataFilled(user)) {
            validator.setValidated(false);
            validator.setMessage(ALL_FIELDS_FILLED_VALIDATION_FAIL_MESSAGE);
        } else {
            validator.setValidated(true);
        }
        return validator;
    }

    public UserOperationValidator getUpdateValidation(UserDto user) {
        UserOperationValidator validator = new UserOperationValidator();
        if (!areAllDataFilled(user)) {
            validator.setValidated(false);
            validator.setMessage(ALL_FIELDS_FILLED_VALIDATION_FAIL_MESSAGE);
            return validator;
        }
        validator.setValidated(true);
        return validator;
    }

    public UserOperationValidator getPasswordValidation(String password) {
        UserOperationValidator validator = new UserOperationValidator();
        if (password.equals("")) {
            validator.setValidated(false);
            validator.setMessage(NON_EMPTY_PASSWORD_VALIDATION_FAIL_MESSAGE);
            return validator;
        }
        validator.setValidated(true);
        return validator;
    }

    @Transactional
    public void register(UserRegistrationDto userDto) {
        User user = new User();
        user.setLogin(userDto.getLogin());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        String passwordHash = passwordEncoder.encode(userDto.getPassword());
        user.setPassword(passwordHash);
        Role role = roleRepository.getRoleByName(USER_ROLE).orElseThrow();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
    }

    public void update(UserDto userDto) {
        User user = userRepository.findByLogin(userDto.getLogin()).orElseThrow();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        userRepository.save(user);
    }

    public UserDto findCurrentUserDto() {
        return UserMapper.mapUserDto(findCurrentUser());
    }

    public List<UserDtoWithId> allUsersBesidesCurrent() {
        return userRepository.findAll().stream()
                .filter(u -> !u.equals(findCurrentUser()))
                .map(UserMapper::mapUserDtoWithId).toList();
    }

    @Transactional
    public void removeCurrentUser() {
        User currentUser = findCurrentUser();
        deleteUser(currentUser);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void changeUserAdminRoleStatusById(Long id) {
        User user = findById(id).orElseThrow();
        if (isUserAdmin(user)) {
            Set<Role> roles = user.getRoles()
                    .stream()
                    .filter(r -> !r.name.equals(ADMIN_ROLE))
                    .collect(Collectors.toSet());
            user.setRoles(roles);
            userRepository.save(user);
        } else {
            Set<Role> roles = user.getRoles();
            Role adminRole = roleRepository.getRoleByName(ADMIN_ROLE).orElseThrow();
            roles.add(adminRole);
            userRepository.save(user);
        }
    }

    @Transactional
    public void deleteUserById(Long id) {
        User user = findById(id).orElseThrow();
        userRepository.delete(user);
    }

    private void deleteUser(User user) {
        userRepository.delete(user);
    }

    private User findCurrentUser() {
        Authentication currentUserAuthentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByLogin(currentUserAuthentication.getName()).orElseThrow();
    }

    private boolean isLoginUsed(UserRegistrationDto userDto) {
        return userRepository.findByLogin(userDto.getLogin()).isPresent();
    }

    private boolean isEmailUsed(UserRegistrationDto userDto) {
        return userRepository.findByEmail(userDto.getEmail()).isPresent();
    }

    private boolean areAllDataFilled(UserRegistrationDto user) {
        String login = user.getLogin();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        String password = user.getPassword();
        if (login.equals("")
                || firstName.equals("")
                || lastName.equals("")
                || email.equals("")
                || password.equals("")) {
            return false;
        }
        return true;
    }

    private boolean areAllDataFilled(UserDto user) {
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        if (firstName.equals("")
                || lastName.equals("")
                || email.equals("")) {
            return false;
        }
        return true;
    }

    public void changePassword(String password) {
        Authentication currentUserAuthentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByLogin(currentUserAuthentication.getName()).orElseThrow();
        String passwordHash = passwordEncoder.encode(password);
        currentUser.setPassword(passwordHash);
        userRepository.save(currentUser);
    }

    static boolean isUserAdmin(User user) {
        return user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet())
                .contains(ADMIN_ROLE);
    }
}

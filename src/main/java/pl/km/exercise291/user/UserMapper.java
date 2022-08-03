package pl.km.exercise291.user;

import pl.km.exercise291.user.dto.UserCredentialsDto;
import pl.km.exercise291.user.dto.UserDto;
import pl.km.exercise291.user.dto.UserDtoWithId;

import java.util.Set;
import java.util.stream.Collectors;

class UserMapper {

    static UserCredentialsDto mapUserCredentialsDto(User user) {
        String login = user.getLogin();
        String password = user.getPassword();
        Set<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        return new UserCredentialsDto(login, password, roles);
    }

    static UserDto mapUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setLogin(user.getLogin());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setAdmin(UserService.isUserAdmin(user));
        return userDto;
    }

    static UserDtoWithId mapUserDtoWithId(User user) {
        UserDtoWithId userDto = new UserDtoWithId();
        userDto.setId(user.getId());
        userDto.setLogin(user.getLogin());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setAdmin(UserService.isUserAdmin(user));
        return userDto;
    }
}

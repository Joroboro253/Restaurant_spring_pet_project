package restaurant.petproject.service;

import restaurant.petproject.dto.UserDto;
import restaurant.petproject.models.User;

import java.util.List;


public interface UserService {
    void saveUser(UserDto userDto);

    User findByEmail(String email);

    List<UserDto> findAllUsers();
}

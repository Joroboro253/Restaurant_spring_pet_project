package restaurant.petproject.service;

import restaurant.petproject.dto.UserDto;
import restaurant.petproject.entity.User;

import java.util.List;
import java.util.Optional;


public interface UserService {
    void saveUser(UserDto userDto);
    User findByEmail(String email);
    List<UserDto> findAllUsers();
    Optional<User> getUserById(Long id);
}

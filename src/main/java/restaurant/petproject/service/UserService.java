package restaurant.petproject.service;

import org.springframework.stereotype.Component;
import restaurant.petproject.dto.UserDto;
import restaurant.petproject.entity.User;

import java.util.List;


public interface UserService {
    void saveUser(UserDto userDto);

    User findByEmail(String email);

    List<UserDto> findAllUsers();
}

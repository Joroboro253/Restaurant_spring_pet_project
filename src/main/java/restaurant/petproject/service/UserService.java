package restaurant.petproject.service;

import restaurant.petproject.dto.UserDto;
import restaurant.petproject.entity.UserEntity;

import java.util.List;


public interface UserService {
    void saveUser(UserDto userDto);

    UserEntity findByEmail(String email);

    List<UserDto> findAllUsers();
}

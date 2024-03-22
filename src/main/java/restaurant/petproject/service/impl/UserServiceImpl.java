package restaurant.petproject.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import restaurant.petproject.dto.UserDto;

import restaurant.petproject.entity.User;
import restaurant.petproject.entity.Role;
import restaurant.petproject.repository.RoleRepository;
import restaurant.petproject.repository.UserRepository;
import restaurant.petproject.service.UserService;



import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Only user with id 1 will have admin role
    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        // Encrypt the password using spring security
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // If it is first user -> user admin else user
        Role role;

        // проверка, первый ли это пользователь
        long userCount = userRepository.count();
        if(userCount == 0) {
            role = roleRepository.findByName("ROLE_ADMIN");
            if(role == null) {
                role = new Role();
                role.setName("ROLE_ADMIN");
                roleRepository.save(role);
            }

        } else {
            role = roleRepository.findByName("ROLE_USER");
            if(role == null) {
                role = new Role();
                role.setName("ROLE_USER");
                role.setName("ROLE_USER");
            }
        }

        user.setRoles(Arrays.asList(role));

        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertEntityToDto)
                        .collect(Collectors.toList());
    }

    private UserDto convertEntityToDto(User user) {
        UserDto userDto = new UserDto();
        String[] str = user.getName().split(" ");
        userDto.setFirstName(str[0]);
        userDto.setLastName(str[1]);
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public User getCurrentUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            //возможно не будет рабоатть в связи с тем, что поиск пл имени пользователя, в у меня по эмейл
            return userRepository.findByEmail(username);
        }
        return null;
    }
}

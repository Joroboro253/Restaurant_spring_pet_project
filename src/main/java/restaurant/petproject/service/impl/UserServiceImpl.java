package restaurant.petproject.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import restaurant.petproject.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import restaurant.petproject.models.User;
import restaurant.petproject.models.Role;
//import restaurant.petproject.models.enums.RoleEnum;
import restaurant.petproject.repository.RoleRepository;
import restaurant.petproject.repository.UserRepository;
import restaurant.petproject.service.UserService;


import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Component
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean createUser(User user) {
        String email = user.getEmail();
        if (userRepository.findByEmail(email) != null) return false;
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role roleUser = new Role();
        roleUser.setName("ROLE_USER");
        user.getRoles().add(Role.Roles.ROLE_USER);

        log.info("Saving new User with email: {}", email);
        userRepository.save(user);
        return true;
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        //encrypt the password using spring security
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findByName("ROLE_ADMIN");
        if(role == null){
            role = checkRoleExist();
        }
        user.setRoles((Set<Role.Roles>) role);
        //Arrays.asSet(role)
        userRepository.save(user);
    }

    public void banUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if(user != null) {
            if(user != null) {
                if(user.isActive()) {
                    user.setActive(false);
                    log.info("Ban user with id = {}; email: {}", user.getId(), user.getEmail());
                } else {
                    user.setActive(true);
                    log.info("Unban user with id = {}; email: {}", user.getId(), user.getEmail());
                }
            }
        }
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map((user) -> convertEntityToDto(user))
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

    private Role checkRoleExist(){
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }

    public List<User> list() {
        return userRepository.findAll();
    }

    public User getUserByPrincipal(Principal principal) {
        if(principal == null) return new User();
        return userRepository.findByEmail(principal.getName());
    }

    public void changeUserRoles(User user, Map<String, String> form) {
        Set<String> roles = Arrays.stream(Role.Roles.values())
                .map(Role.Roles::name)
                .collect(Collectors.toSet());
        user.getRoles().clear();
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                Role role = new Role();
                role.setName(key);
                user.getRoles().add(Role.Roles.ROLE_USER);
            }
        }
        userRepository.save(user);
    }



}

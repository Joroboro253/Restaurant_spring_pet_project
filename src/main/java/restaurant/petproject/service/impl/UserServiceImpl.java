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
        user.setRoles(Arrays.asList(determineUserRole()));

        userRepository.save(user);
    }

    private Role determineUserRole() {
        // Check if any user exists, if not, the first user is admin
        if (userRepository.count() == 0) {
            return roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setName("ROLE_ADMIN");
                        return roleRepository.save(newRole);
                    });
        } else {
            return roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setName("ROLE_USER");
                        return roleRepository.save(newRole);
                    });
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
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    private UserDto convertEntityToDto(User user) {
        UserDto userDto = new UserDto();
        String[] nameParts = user.getName().split(" ");
        userDto.setFirstName(nameParts.length > 0 ? nameParts[0] : "");
        userDto.setLastName(nameParts.length > 1 ? nameParts[1] : "");
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepository.findByEmail(username);
        }
        return null;
    }
}

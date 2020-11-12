package be.juvo.paul.formpractice.services;

import be.juvo.paul.formpractice.entities.User;
import be.juvo.paul.formpractice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    public User saveUser(String firstName) {
        User user = User.builder()
                .firstName(firstName)
                .build();
        userRepository.save(user);
        return user;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
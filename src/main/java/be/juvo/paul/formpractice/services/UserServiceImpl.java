package be.juvo.paul.formpractice.services;

import be.juvo.paul.formpractice.entities.User;
import be.juvo.paul.formpractice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
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

    public User findByLastName(String lastName) {
        return userRepository.count() > 0 ? userRepository.findByLastName(lastName).get(0) : new User();
    }

    public User findByFunction(String function) {
        return userRepository.count() > 0 ? userRepository.findByFunction(function).get(0) : new User();
    }

    public User createDummyUser() {
        return User.builder()
                .firstName("Paul")
                .lastName("Potato")
                .department("IT")
                .function("Programmer")
                .seniority(2)
                .dateOfHire(Date.from(Instant.now()))
                .build();
    }

    public void save(User user) {
        userRepository.save(user);
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

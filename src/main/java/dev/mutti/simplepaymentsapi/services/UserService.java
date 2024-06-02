package dev.mutti.simplepaymentsapi.services;

import dev.mutti.simplepaymentsapi.domain.User;
import dev.mutti.simplepaymentsapi.exception.UserNotFoundException;
import dev.mutti.simplepaymentsapi.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialException;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public User createUser(@Valid User user) {
        return userRepository.save(user);
    }

    public User loginUser(User user) throws CredentialException {
        User existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser == null) {
            throw new UserNotFoundException("User not found");
        }

        if (!existingUser.getPassword().equals(user.getPassword())) {
            throw new CredentialException("Invalid password");
        }

        return existingUser;
    }

    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public double updateBalance(long userId, double amount) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.updateBalanceById(userId, user.getBalance() + amount);
        return user.getBalance();
    }
}

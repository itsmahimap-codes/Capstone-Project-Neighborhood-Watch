package NeighborhoodWatch.service;
import java.util.Optional;
import NeighborhoodWatch.entity.User;
import NeighborhoodWatch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        return userRepository.save(user);
    }
    public User loginUser(String email, String password) {

    Optional<User> user = userRepository.findByEmail(email);

    if (user.isPresent() && user.get().getPassword().equals(password)) {
        return user.get();
    }

    return null;
}
}
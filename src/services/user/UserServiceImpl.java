package services.user;

import database.UserRepository;
import database.classes.UserData;
import database.exceptions.DatabaseNotLoadedException;
import services.user.interfaces.UserService;

import java.util.List;
import java.util.Set;

public class UserServiceImpl implements UserService {

    private UserRepository userRepository = new UserRepository();

    public UserData findByUsername(String username) {
        return userRepository.find(username);
    }

    public List<UserData> findByUsernames(Set<String> usernames) {
        return userRepository.find(usernames);
    }

    public List<UserData> findAll() throws DatabaseNotLoadedException {
        return userRepository.findAll();
    }

}

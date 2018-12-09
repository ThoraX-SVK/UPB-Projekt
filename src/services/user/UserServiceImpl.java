package services.user;

import database.UserRepository;
import database.classes.UserData;
import database.exceptions.DatabaseNotLoadedException;
import database.exceptions.UserAlreadyExistsException;
import services.user.interfaces.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class UserServiceImpl implements UserService {

    private UserRepository userRepository = new UserRepository();

    @Override
    public UserData findByUsername(String username) {
        return userRepository.find(username);
    }

    @Override
    public List<UserData> findByUsernames(Set<String> usernames) {
        return userRepository.find(usernames);
    }

    @Override
    public List<UserData> findAll() throws DatabaseNotLoadedException {
        return userRepository.findAll();
    }

    @Override
    public void save(UserData userData) throws UserAlreadyExistsException, DatabaseNotLoadedException {
        userRepository.add(userData);
    }

}

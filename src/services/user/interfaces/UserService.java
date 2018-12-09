package services.user.interfaces;

import database.classes.UserData;
import database.exceptions.DatabaseNotLoadedException;
import database.exceptions.UserAlreadyExistsException;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface UserService {

    UserData findByUsername(String username);

    List<UserData> findByUsernames(Set<String> usernames);

    List<UserData> findAll() throws DatabaseNotLoadedException;

    void save(UserData newUser) throws UserAlreadyExistsException, DatabaseNotLoadedException;
}

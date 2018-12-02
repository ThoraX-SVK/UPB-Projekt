package services.auth.interfaces;

import database.classes.UserData;
import database.exceptions.DatabaseNotLoadedException;

import java.util.List;

public interface IUserService {

    UserData find(String username);

    List<UserData> findAll() throws DatabaseNotLoadedException;

}

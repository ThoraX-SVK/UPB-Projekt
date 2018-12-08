package database;

import com.google.gson.reflect.TypeToken;
import database.classes.FileData;
import database.classes.UserData;
import database.exceptions.DatabaseNotLoadedException;
import database.exceptions.UserAlreadyExistsException;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

import config.SystemFilePaths;

public class UserRepository extends VeryBasicJsonDataRepository {

    private static final String DATA_FILE = SystemFilePaths.DATABASE_LOCATION + File.separator + "userDB.json";
    private static final String DATA_FILE_LOCATION = SystemFilePaths.DATABASE_LOCATION;

    public UserRepository() {
        super(DATA_FILE, DATA_FILE_LOCATION);
    }

    public void add(UserData userData) throws DatabaseNotLoadedException, UserAlreadyExistsException, IOException {
        createIfNotExists();
        Map<String, UserData> dbContent = load();

        if (dbContent.containsKey(userData.getUsername()))
            throw UserAlreadyExistsException.fromUsername(userData.getUsername());

        dbContent.put(userData.getUsername(), userData);
        save(dbContent);
    }

    public UserData find(String username) {
        Map<String, UserData> dbContent;
        try {
            dbContent = load();
        } catch (DatabaseNotLoadedException e) {
            return null;
        }

        if (dbContent.containsKey(username)) {
            UserData userData = dbContent.get(username);
            return UserData.fromUserData(username, userData.getPassword(), userData.getSalt());
        } else {
            return null;
        }
    }

    public List<UserData> find(Set<String> usernames) {
        Map<String, UserData> users = null;
        try {
            users = load();
        } catch (DatabaseNotLoadedException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

        List<UserData> result = new ArrayList<>();

        for (String username : usernames) {
            if (users.containsKey(username)) {
                result.add(users.get(username));
            }
        }

        return result;
    }

    public List<UserData> findAll() throws DatabaseNotLoadedException {
        Map<String, UserData> dbContent;
        List<UserData> results = new ArrayList<>();
        dbContent = load();

        for (Map.Entry<String, UserData> userDataEntry : dbContent.entrySet()) {
            results.add(userDataEntry.getValue());
        }
        return results;
    }

     protected Type getDataMapType() {
        return new TypeToken<Map<String, UserData>>() {}.getType();
    }


}

package database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import database.classes.FileData;
import database.classes.UserData;
import database.exceptions.DatabaseNotLoadedException;
import database.exceptions.UserAlreadyExistsException;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import domain.utils.FileUtils;
import json.JsonSerializerImpl;
import config.SystemFilePaths;
import json.interfaces.MapToStringSerializer;

public class UserRepository extends VeryBasicJsonDataRepository {

    private String dataFile = SystemFilePaths.DATABASE_LOCATION + File.separator + "userDB.json";
    private String dataFileLocation = SystemFilePaths.DATABASE_LOCATION;


    public void add(String username, String password, String salt) throws DatabaseNotLoadedException, UserAlreadyExistsException, IOException {
        createIfNotExists(dataFile, dataFileLocation);
        Map<String, UserData> dbContent = load();

        if (dbContent.containsKey(username))
            throw UserAlreadyExistsException.fromUsername(username);

        UserData newUser = UserData.fromUserData(username, password, salt);
        dbContent.put(username, newUser);
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

    public List<UserData> findAll() throws DatabaseNotLoadedException {
        Map<String, UserData> dbContent;
        List<UserData> results = new ArrayList<>();
        dbContent = load();

        for (Map.Entry<String, UserData> userDataEntry : dbContent.entrySet()) {
            results.add(userDataEntry.getValue());
        }
        return results;
    }

     private Type getFileDataMapType() {
        return new TypeToken<Map<String, UserData>>() {}.getType();
    }

    @Override
    protected void save(Map dbContent) throws DatabaseNotLoadedException {
        Gson gson = getGsonInstance();
        String serializedJson = gson.toJson(dbContent);

        File db = new File(dataFile);
        try {
            FileUtils.writeToFile(serializedJson, db);
        } catch (IOException e) {
            throw DatabaseNotLoadedException.generic(e);
        }
    }

    @Override
    protected Map<String, UserData> load() throws DatabaseNotLoadedException {
       Gson gson = getGsonInstance();

        File db = new File(dataFile);
        String content;
        try {
            content = FileUtils.readFile(db);
        } catch (IOException e) {
            throw DatabaseNotLoadedException.generic(e);
        }
        Type listType = getFileDataMapType();

        Map<String, UserData> files = gson.fromJson(content, listType);
        return files;
    }

}

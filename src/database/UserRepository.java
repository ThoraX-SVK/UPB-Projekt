package database;

import database.classes.UserData;
import database.exceptions.DatabaseNotLoadedException;
import database.exceptions.UserAlreadyExistsException;

import java.io.*;
import java.util.Map;

import domain.utils.FileUtils;
import json.JsonSerializerImpl;
import config.SystemFilePaths;
import json.interfaces.MapToStringSerializer;

public class UserRepository extends VeryBasicJsonDataRepository {

    private String dataFile = SystemFilePaths.DATABASE_LOCATION + File.separator + "userDB.json";
    private String dataFileLocation = SystemFilePaths.DATABASE_LOCATION;


    public void add(String username, String passwordAndSalt) throws DatabaseNotLoadedException, UserAlreadyExistsException, IOException {
        createIfNotExists(dataFile, dataFileLocation);
        Map<String, String> dbContent = load();

        if (dbContent.containsKey(username))
            throw UserAlreadyExistsException.fromUsername(username);

        dbContent.put(username, passwordAndSalt);
        save(dbContent);
    }

    public UserData find(String username) {

        Map<String, String> dbContent;
        try {
            dbContent = load();
        } catch (DatabaseNotLoadedException e) {
            return null;
        }

        if (dbContent.containsKey(username)) {
            String passAndSalt = dbContent.get(username);
            String[] passAndSaltSplit = passAndSalt.split(":");
            return UserData.fromUserData(username, passAndSaltSplit[0], passAndSaltSplit[1]);
        } else {
            return null;
        }

    }

    @Override
    protected void save(Map dbContent) throws DatabaseNotLoadedException {
        File db = new File(dataFile);
        MapToStringSerializer mapToFileSerializer = new JsonSerializerImpl();
        String fileContent = mapToFileSerializer.serializeMap(dbContent);
        try {
            FileUtils.writeToFile(fileContent, db);
        } catch (IOException e) {
            throw DatabaseNotLoadedException.generic(e);
        }
    }

    @Override
    protected Map<String, String> load() throws DatabaseNotLoadedException {
        MapToStringSerializer serializer = new JsonSerializerImpl();

        File db = new File(dataFile);
        String content;
        try {
            content = FileUtils.readFile(db);
        } catch (IOException e) {
            throw DatabaseNotLoadedException.generic(e);
        }

        return serializer.deserializeMap(content);
    }
}

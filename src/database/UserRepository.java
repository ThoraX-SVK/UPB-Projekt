package database;

import database.classes.UserData;
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


    public void add(String username, String passwordAndSalt) throws IOException, UserAlreadyExistsException {
        createIfNotExists(dataFile, dataFileLocation);
        Map<String, String> dbContent = getContent();

        if (dbContent.containsKey(username))
            throw UserAlreadyExistsException.fromUsername(username);

        dbContent.put(username, passwordAndSalt);
        save(dbContent);
    }

    public UserData find(String username) {

        Map<String, String> dbContent;
        try {
            dbContent = getContent();
        } catch (IOException e) {
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

    private Map<String, String> getContent() throws IOException {
        MapToStringSerializer serializer = new JsonSerializerImpl();

        File db = new File(dataFile);
        String content = FileUtils.readFile(db);

        return serializer.deserializeMap(content);
    }

    @Override
    void save(Map dbContent) throws IOException {
        File db = new File(dataFile);
        MapToStringSerializer mapToFileSerializer = new JsonSerializerImpl();
        String fileContent = mapToFileSerializer.serializeMap(dbContent);
        FileUtils.writeToFile(fileContent, db);
    }
}

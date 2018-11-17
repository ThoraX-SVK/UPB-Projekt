package database;

import database.exceptions.UserAlreadyExistsException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import domain.utils.FileUtils;
import json.JsonSerializerImpl;
import config.SystemFilePaths;
import json.interfaces.MapToStringSerializer;

public class Database {

    public static final String DATABASE_FILE = SystemFilePaths.DATABASE_LOCATION + File.separator + "db.json";


    private static void createIfNotExists() throws IOException {
        FileUtils.createDirectoryIfNotExists(SystemFilePaths.DATABASE_LOCATION);
        File db = new File(DATABASE_FILE);
        try {
            db.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void add(String username, String passwordAndSalt) throws IOException, UserAlreadyExistsException {
        MapToStringSerializer mapToFileSerializer = new JsonSerializerImpl();
        createIfNotExists();
        Map<String, String> dbContent = getContent_FAKE();

        if (dbContent.containsKey(username))
            throw UserAlreadyExistsException.fromUsername(username);

        dbContent.put(username, passwordAndSalt);
        String fileContent = mapToFileSerializer.serializeMap(dbContent);

        File db = new File(DATABASE_FILE);
        FileUtils.writeToFile(fileContent, db);
    }

    public static Result find(String username) {

        Map<String, String> dbContent;
        try {
            dbContent = getContent_FAKE();
        } catch (IOException e) {
            return null;
        }

        if (dbContent.containsKey(username)) {
            String passAndSalt = dbContent.get(username);
            String[] passAndSaltSplit = passAndSalt.split(":");
            return Result.fromUserData(username, passAndSaltSplit[0], passAndSaltSplit[1]);
        } else {
            return null;
        }

    }

    private static Map<String, String> getContent() throws IOException {
        MapToStringSerializer serializer = new JsonSerializerImpl();

        File db = new File(DATABASE_FILE);
        String content = FileUtils.readFile(db);

        return serializer.deserializeMap(content);
    }

    private static Map<String, String> getContent_FAKE() throws IOException {
        Map<String, String> users = new HashMap<>();
        users.put(
                "jozko",
                "eouKVjwnos3sg2J9kA1/yAlOA4DHqIfHOXEOsGOCN8k=:PN90S2/58Jnf8HTJzowhtbQe5K+S3cSjAXozKKyt+d8XBD2TLWT+ZbjZAdzvezDRFc1ATQQWnmUiNuV36p6Gr8ZnRo42yaiH9cxEG3VP3zKxd53bynTdS704FYz9zWISwJmQRA=="
        );
        return users;
    }

}

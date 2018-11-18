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

    private static final String DATABASE_FILE = SystemFilePaths.DATABASE_LOCATION + File.separator + "db.json";

    private static void createIfNotExists() throws IOException {
        FileUtils.createDirectoryIfNotExists(SystemFilePaths.DATABASE_LOCATION);
        File db = new File(DATABASE_FILE);

        if (!db.exists()) {
            db.createNewFile();
            FileOutputStream fos = new FileOutputStream(db);
            fos.write("{}".getBytes());
            fos.close();
        }
    }

    public static void add(String username, String passwordAndSalt) throws IOException, UserAlreadyExistsException {
        MapToStringSerializer mapToFileSerializer = new JsonSerializerImpl();
        createIfNotExists();
        Map<String, String> dbContent = getContent();

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
            dbContent = getContent();
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

}

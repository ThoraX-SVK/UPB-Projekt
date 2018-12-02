package database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import config.SystemFilePaths;
import database.classes.FileData;
import database.classes.UserFileRelationship;
import database.exceptions.DatabaseNotLoadedException;
import domain.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserFileRelationshipRepository extends VeryBasicJsonDataRepository {

    private String dataFile = SystemFilePaths.DATABASE_LOCATION + File.separator + "userFileDB.json";
    private String dataFileLocation = SystemFilePaths.DATABASE_LOCATION;


    public void addRecord(String username) throws IOException, DatabaseNotLoadedException {
        createIfNotExists(dataFile, dataFileLocation);

        Map<String, UserFileRelationship> content = load();
        if (!content.containsKey(username)) {
            content.put(username, UserFileRelationship.empty(username));
        }
        save(content);
    }

    public void addOwnerToFile(String username, String fileId) throws DatabaseNotLoadedException {
        Map<String, UserFileRelationship> content = load();

        UserFileRelationship userFileRelationship = content.getOrDefault(fileId, UserFileRelationship.empty(username));
        userFileRelationship.addOwnedFile(fileId);

        if (!content.containsKey(username)) {
            content.put(username, userFileRelationship);
        }

        save(content);
    }

    public void addGuestToFile(String username, String fileId) throws DatabaseNotLoadedException {
        Map<String, UserFileRelationship> content = load();

        UserFileRelationship userFileRelationship = content.getOrDefault(fileId, UserFileRelationship.empty(username));
        userFileRelationship.addAccessibleFile(fileId);

        if (!content.containsKey(username)) {
            content.put(username, userFileRelationship);
        }

        save(content);
    }

    public UserFileRelationship findByUsername(String username) throws DatabaseNotLoadedException {
        Map<String, UserFileRelationship> content = load();

        return content.getOrDefault(username, null);
    }


    public List<UserFileRelationship> findAll() throws DatabaseNotLoadedException {
        Map<String, UserFileRelationship> dbContent;
        List<UserFileRelationship> results = new ArrayList<>();
        dbContent = load();

        for (Map.Entry<String, UserFileRelationship> fileDataEntry : dbContent.entrySet()) {
            results.add(fileDataEntry.getValue());
        }
        return results;
    }

    private Type getUserFileDataMapType() {
        return new TypeToken<Map<String, UserFileRelationship>>() {}.getType();
    }

    @Override
    protected void save(Map files) throws DatabaseNotLoadedException {
        Gson gson = getGsonInstance();
        String serializedJson = gson.toJson(files);

        File db = new File(dataFile);
        try {
            FileUtils.writeToFile(serializedJson, db);
        } catch (IOException e) {
            throw DatabaseNotLoadedException.generic(e);
        }
    }

    @Override
    protected Map<String, UserFileRelationship> load() throws DatabaseNotLoadedException {
        Gson gson = getGsonInstance();

        File db = new File(dataFile);
        String content;
        try {
            content = FileUtils.readFile(db);
        } catch (IOException e) {
            throw DatabaseNotLoadedException.generic(e);
        }
        Type listType = getUserFileDataMapType();

        Map<String, UserFileRelationship> files = gson.fromJson(content, listType);
        return files;
    }
}

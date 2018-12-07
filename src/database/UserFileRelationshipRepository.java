package database;

import com.google.gson.reflect.TypeToken;
import config.SystemFilePaths;
import database.classes.UserFileRelationship;
import database.exceptions.DatabaseNotLoadedException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserFileRelationshipRepository extends VeryBasicJsonDataRepository {

    private static final String DATA_FILE = SystemFilePaths.DATABASE_LOCATION + File.separator + "userFileDB.json";
    private static final String DATA_FILE_LOCATION = SystemFilePaths.DATABASE_LOCATION;

    public UserFileRelationshipRepository() {
        super(DATA_FILE, DATA_FILE_LOCATION);
    }

    public void addRecord(String username) throws IOException, DatabaseNotLoadedException {
        createIfNotExists();

        Map<String, UserFileRelationship> content = load();
        if (!content.containsKey(username)) {
            content.put(username, UserFileRelationship.empty(username));
        }
        save(content);
    }

    public void addOwnerToFile(String username, String fileId) throws DatabaseNotLoadedException {
        Map<String, UserFileRelationship> content = load();

        UserFileRelationship userFileRelationship = content.getOrDefault(username, UserFileRelationship.empty(username));
        userFileRelationship.addOwnedFile(fileId);

        if (!content.containsKey(username)) {
            content.put(username, userFileRelationship);
        }

        save(content);
    }

    public void addGuestToFile(String username, String fileId) throws DatabaseNotLoadedException {
        Map<String, UserFileRelationship> content = load();

        UserFileRelationship userFileRelationship = content.getOrDefault(username, UserFileRelationship.empty(username));
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

    @Override
    protected Type getDataMapType() {
        return new TypeToken<Map<String, UserFileRelationship>>() {}.getType();
    }

}

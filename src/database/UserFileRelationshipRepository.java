package database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import config.SystemFilePaths;
import database.classes.UserFileRelationship;
import database.exceptions.DatabaseNotLoadedException;
import domain.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
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

        if (content.containsKey(username)) {
            UserFileRelationship ufr = content.get(username);
            ufr.addOwnedFile(fileId);
        }

        save(content);
    }

    public void addGuestToFile(String username, String fileId) throws DatabaseNotLoadedException {
        Map<String, UserFileRelationship> content = load();

        if (content.containsKey(username)) {
            UserFileRelationship ufr = content.get(username);
            ufr.addAccessibleFile(fileId);
        }

        save(content);
    }

    public UserFileRelationship findByUsername(String username) throws DatabaseNotLoadedException {
        Map<String, UserFileRelationship> content = load();

        return content.getOrDefault(username, null);
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

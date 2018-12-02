package database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import config.SystemFilePaths;
import database.classes.Comment;
import database.exceptions.DatabaseNotLoadedException;
import domain.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class FileCommentsRepository extends VeryBasicJsonDataRepository {

    private String dataFile = SystemFilePaths.DATABASE_LOCATION + File.separator + "commentsDB.json";
    private String dataFileLocation = SystemFilePaths.DATABASE_LOCATION;

    public void publish(Comment comment, String fileId) throws IOException, DatabaseNotLoadedException {
        createIfNotExists(dataFile, dataFileLocation);

        Map<String, List<Comment>> allFileComments = load();

        List<Comment> comments = allFileComments.getOrDefault(fileId, new LinkedList<>());
        comments.add(comment);
        allFileComments.put(fileId, comments);

        save(allFileComments);
    }

    public List<Comment> getAllByFileId(String fileId) throws DatabaseNotLoadedException {
        Map<String, List<Comment>> fileComments = load();
        List<Comment> comments = fileComments.getOrDefault(fileId, new LinkedList<>());
        comments.sort(Comparator.comparing(Comment::getPublishDate));
        return comments;
    }

    private Type getCommentMapType() {
        return new TypeToken<Map<String, List<Comment>>>() {}.getType();

    }

    @Override
    protected void save(Map data) throws DatabaseNotLoadedException {
        Gson gson = getGsonInstance();
        String serializedJson = gson.toJson(data);

        File db = new File(dataFile);
        try {
            FileUtils.writeToFile(serializedJson, db);
        } catch (IOException e) {
            throw DatabaseNotLoadedException.generic(e);
        }
    }

    @Override
    protected Map<String, List<Comment>> load() throws DatabaseNotLoadedException {
        Gson gson = getGsonInstance();

        File db = new File(dataFile);
        String content;
        try {
            content = FileUtils.readFile(db);
        } catch (IOException e) {
            throw DatabaseNotLoadedException.generic(e);
        }
        Type listType = getCommentMapType();

        return gson.fromJson(content, listType);
    }
}

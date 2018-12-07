package database;

import com.google.gson.reflect.TypeToken;
import config.SystemFilePaths;
import database.classes.Comment;
import database.exceptions.DatabaseNotLoadedException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class FileCommentsRepository extends VeryBasicJsonDataRepository {

    private static final String DATA_FILE = SystemFilePaths.DATABASE_LOCATION + File.separator + "commentsDB.json";
    private static final String DATA_FILE_LOCATION = SystemFilePaths.DATABASE_LOCATION;

    public FileCommentsRepository() {
        super(DATA_FILE, DATA_FILE_LOCATION);
    }

    public void publish(Comment comment, String fileId) throws IOException, DatabaseNotLoadedException {
        createIfNotExists();

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

    protected Type getDataMapType() {
        return new TypeToken<Map<String, List<Comment>>>() {}.getType();

    }

}

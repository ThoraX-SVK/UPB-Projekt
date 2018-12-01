package database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import config.SystemFilePaths;
import database.classes.Comment;
import domain.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class FileCommentsRepository extends VeryBasicJsonDataRepository {

    private String dataFile = SystemFilePaths.DATABASE_LOCATION + File.separator + "commentsDB.json";
    private String dataFileLocation = SystemFilePaths.DATABASE_LOCATION;

    public void publish(Comment comment, String fileId) throws IOException {
        createIfNotExists(dataFile, dataFileLocation);

        Map<String, List<Comment>> allFileComments = getAll();

        List<Comment> comments = allFileComments.getOrDefault(fileId, new LinkedList<>());
        comments.add(comment);
        allFileComments.put(fileId, comments);

        save(allFileComments);
    }

    public List<Comment> getAllByFileId(String fileId) throws IOException {
        Map<String, List<Comment>> fileComments = getAll();
        List<Comment> comments = fileComments.getOrDefault(fileId, new LinkedList<>());
        comments.sort(Comparator.comparing(Comment::getPublishDate));
        return comments;
    }

    public Map<String, List<Comment>> getAll() throws IOException {
        Gson gson = getGsonInstance();

        File db = new File(dataFile);
        String content = FileUtils.readFile(db);
        Type listType = getCommentMapType();

        return gson.fromJson(content, listType);
    }

    private Type getCommentMapType() {
        return new TypeToken<Map<String, List<Comment>>>() {}.getType();

    }


    @Override
    void save(Map data) throws IOException {
        Gson gson = getGsonInstance();
        String serializedJson = gson.toJson(data);

        File db = new File(dataFile);
        FileUtils.writeToFile(serializedJson, db);
    }
}

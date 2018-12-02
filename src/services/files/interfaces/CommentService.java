package services.files.interfaces;

import database.classes.Comment;
import database.exceptions.DatabaseNotLoadedException;

import java.io.IOException;
import java.util.List;

public interface CommentService {

    List<Comment> findAll(String fileId) throws DatabaseNotLoadedException;

    void publish(Comment comment, String fileId) throws DatabaseNotLoadedException, IOException;
}

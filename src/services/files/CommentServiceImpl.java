package services.files;

import database.FileCommentsRepository;
import database.classes.Comment;
import database.exceptions.DatabaseNotLoadedException;
import services.files.interfaces.CommentService;

import java.io.IOException;
import java.util.List;

public class CommentServiceImpl implements CommentService {

    FileCommentsRepository commentsRepository = new FileCommentsRepository();

    public List<Comment> findAll(String fileId) throws DatabaseNotLoadedException {
        return commentsRepository.getAllByFileId(fileId);
    }

    @Override
    public void publish(Comment comment, String fileId) throws DatabaseNotLoadedException, IOException {
        commentsRepository.publish(comment, fileId);
    }
}

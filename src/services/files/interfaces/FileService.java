package services.files.interfaces;

import database.classes.EncryptionType;
import database.classes.FileData;
import database.exceptions.DatabaseNotLoadedException;
import database.exceptions.FileDataNotPersistedException;

import java.util.List;

public interface FileService {

    List<FileData> findAllOwnedFilesByUsername(String username) throws DatabaseNotLoadedException;

    List<FileData> findAllAccessibleFilesByUsername(String username) throws DatabaseNotLoadedException;

    FileData findFile(String fileId);

    String saveFile(String fileName, String encryptionKey, EncryptionType encryptionType) throws FileDataNotPersistedException, DatabaseNotLoadedException;

    String saveFile(FileData fileData) throws FileDataNotPersistedException, DatabaseNotLoadedException;

    void addOwnerToFile(String username, String fileId) throws DatabaseNotLoadedException;

    void addGuestToFile(String username, String fileId) throws DatabaseNotLoadedException;

    boolean isUserFileOwner(String username, String fileId) throws DatabaseNotLoadedException;

    boolean canUserAccessFile(String username, String fileId) throws DatabaseNotLoadedException;

}

package services.files;

import database.FileRepository;
import database.UserFileRelationshipRepository;
import database.classes.EncryptionType;
import database.classes.FileData;
import database.classes.UserFileRelationship;
import database.exceptions.DatabaseNotLoadedException;
import database.exceptions.FileDataNotPersistedException;
import services.files.interfaces.FileService;

import java.util.List;


public class FileServiceImpl implements FileService {

    private FileRepository fileRepository = new FileRepository();
    private UserFileRelationshipRepository userFileRelationshipRepository = new UserFileRelationshipRepository();

    public List<FileData> findAllOwnedFilesByUsername(String username) throws DatabaseNotLoadedException {
        try {
            UserFileRelationship ufr = userFileRelationshipRepository.findByUsername(username);
            return fileRepository.findByIds(ufr.getOwnedFiles());

        } catch (DatabaseNotLoadedException e) {
            throw e;
        }
    }

    public List<FileData> findAllAccessibleFilesByUsername(String username) throws DatabaseNotLoadedException {
        try {
            UserFileRelationship ufr = userFileRelationshipRepository.findByUsername(username);
            return fileRepository.findByIds(ufr.getAccessibleFiles());

        } catch (DatabaseNotLoadedException e) {
            throw e;
        }
    }

    public FileData findFile(String fileId) {
        FileData fileData = fileRepository.findById(fileId);
        return fileData;
    }

    @Override
    public String saveFile(String fileName, String encryptionKey, EncryptionType encryptionType) throws FileDataNotPersistedException, DatabaseNotLoadedException {
        try {
            String fileId = fileRepository.addFile(fileName, encryptionKey, encryptionType);
            return fileId;

        } catch (FileDataNotPersistedException e) {
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public String saveFile(FileData fileData) throws FileDataNotPersistedException, DatabaseNotLoadedException {
        return saveFile(fileData.getFileName(), fileData.getEncryptionKey(), fileData.getEncryptionType());
    }

    @Override
    public void addOwnerToFile(String username, String fileId) throws DatabaseNotLoadedException {
        userFileRelationshipRepository.addOwnerToFile(username, fileId);
    }

    @Override
    public void addGuestToFile(String username, String fileId) throws DatabaseNotLoadedException {
        userFileRelationshipRepository.addGuestToFile(username, fileId);
    }

    @Override
    public boolean isUserFileOwner(String username, String fileId) throws DatabaseNotLoadedException {
        UserFileRelationship userFileRelationship = userFileRelationshipRepository.findByUsername(username);
        if (userFileRelationship == null) {
            return false;
        }
        return userFileRelationship.getOwnedFiles().contains(fileId);
    }

    @Override
    public boolean canUserAccessFile(String username, String fileId) throws DatabaseNotLoadedException {
        UserFileRelationship userFileRelationship = userFileRelationshipRepository.findByUsername(username);
        if (userFileRelationship == null) {
            return false;
        }
        return userFileRelationship.getOwnedFiles().contains(fileId) || userFileRelationship.getAccessibleFiles().contains(fileId);
    }

}

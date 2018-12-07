package services.files;

import database.FileRepository;
import database.UserFileRelationshipRepository;
import database.classes.EncryptionType;
import database.classes.FileData;
import database.classes.UserFileRelationship;
import database.exceptions.DatabaseNotLoadedException;
import database.exceptions.FileDataNotPersistedException;
import services.files.interfaces.FileService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class FileServiceImpl implements FileService {

    private FileRepository fileRepository = new FileRepository();
    private UserFileRelationshipRepository userFileRelationshipRepository = new UserFileRelationshipRepository();


    @Override
    public List<String> findAllFileOwnersUsernames(String fileId) throws DatabaseNotLoadedException {
        List<UserFileRelationship> userFileRelationships = userFileRelationshipRepository.findAll();
        List<String> usernames = userFileRelationships.stream()
                .filter(userFileRelationship -> userFileRelationship.isFileOwnedByUser(fileId))
                .map(UserFileRelationship::getUsername)
                .collect(Collectors.toList());
        return usernames;
    }

    @Override
    public List<String> findAllFileGuestsUsernames(String fileId) throws DatabaseNotLoadedException {
        List<UserFileRelationship> userFileRelationships = userFileRelationshipRepository.findAll();
        List<String> usernames = userFileRelationships.stream()
                .filter(userFileRelationship -> userFileRelationship.isFileAccessibleByUser(fileId))
                .map(UserFileRelationship::getUsername)
                .collect(Collectors.toList());
        return usernames;
    }

    @Override
    public List<FileData> findAllOwnedFilesByUsername(String username) throws DatabaseNotLoadedException {
        try {
            UserFileRelationship ufr = userFileRelationshipRepository.findByUsername(username);
            if (ufr != null) {
                return fileRepository.findByIds(ufr.getOwnedFiles());
            } else {
                return Collections.emptyList();
            }

        } catch (DatabaseNotLoadedException e) {
            throw e;
        }
    }

    @Override
    public List<FileData> findAllAccessibleFilesByUsername(String username) throws DatabaseNotLoadedException {
        try {
            UserFileRelationship ufr = userFileRelationshipRepository.findByUsername(username);
            if (ufr != null) {
                return fileRepository.findByIds(ufr.getAccessibleFiles());
            } else {
                return Collections.emptyList();
            }

        } catch (DatabaseNotLoadedException e) {
            throw e;
        }
    }

    @Override
    public FileData findFile(String fileId) {
        FileData fileData = fileRepository.findById(fileId);
        return fileData;
    }

    @Override
    public String saveFile(String fileName, String encryptionKey, EncryptionType encryptionType, String uploadFolder) throws FileDataNotPersistedException, DatabaseNotLoadedException {
        return saveFile(
                new FileData(fileName, encryptionKey, encryptionType, uploadFolder)
        );
    }

    @Override
    public String saveFile(FileData fileData) throws FileDataNotPersistedException, DatabaseNotLoadedException {

        try {
            String fileId = fileRepository.addFile(fileData);
            return fileId;

        } catch (FileDataNotPersistedException e) {
            e.printStackTrace();
            throw e;
        }
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
        return userFileRelationship.isFileOwnedByUser(fileId);
    }

    @Override
    public boolean canUserAccessFile(String username, String fileId) throws DatabaseNotLoadedException {
        UserFileRelationship userFileRelationship = userFileRelationshipRepository.findByUsername(username);
        if (userFileRelationship == null) {
            return false;
        }
        return userFileRelationship.isFileOwnedByUser(fileId) || userFileRelationship.isFileAccessibleByUser(fileId);
    }

    @Override
    public boolean fileExists(String fileId) {
        FileData fileData = findFile(fileId);

        return fileData != null;
    }

}

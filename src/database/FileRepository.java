package database;

import com.google.gson.reflect.TypeToken;
import config.SystemFilePaths;
import database.classes.EncryptionType;
import database.classes.FileData;
import database.exceptions.DatabaseNotLoadedException;
import database.exceptions.FileDataNotPersistedException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class FileRepository extends VeryBasicJsonDataRepository {

    private static final String DATA_FILE = SystemFilePaths.DATABASE_LOCATION + File.separator + "fileDB.json";
    private static final String DATA_FILE_LOCATION = SystemFilePaths.DATABASE_LOCATION;

    public FileRepository() {
        super(DATA_FILE, DATA_FILE_LOCATION);
    }

    public String addFile(FileData fileData) throws FileDataNotPersistedException, DatabaseNotLoadedException {
        try {
            createIfNotExists();

            String newFileId = makeFileId(fileData.getFileName());
            Map<String, FileData> files = load();

            fileData.setId(newFileId);
            files.put(newFileId, fileData);
            save(files);

            return newFileId;

        } catch (IOException e) {
            throw FileDataNotPersistedException.generic(e);
        }
    }

    public FileData findById(String fileId) {
        Map<String, FileData> files = null;
        try {
            files = load();
        } catch (DatabaseNotLoadedException e) {
            return null;
        }

        return files.getOrDefault(fileId, null);
    }

    public List<FileData> findByIds(Set<String> fileIds) throws DatabaseNotLoadedException {
        Map<String, FileData> files = load();
        List<FileData> result = new ArrayList<>();

        for (String fileId : fileIds) {
            if (files.containsKey(fileId)) {
                result.add(files.get(fileId));
            }
        }

        return result;
    }

    public List<FileData> findAll() throws DatabaseNotLoadedException {
        Map<String, FileData> dbContent;
        List<FileData> results = new ArrayList<>();
        dbContent = load();

        for (Map.Entry<String, FileData> fileDataEntry : dbContent.entrySet()) {
            results.add(fileDataEntry.getValue());
        }
        return results;
    }

    private String makeFileId(String fileName) {
        fileName = fileName.replace(" ", "");
        return new Date().getTime() + "_" + fileName;
    }

    protected Type getDataMapType() {
        return new TypeToken<Map<String, FileData>>() {
        }.getType();
    }
}

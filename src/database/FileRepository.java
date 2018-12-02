package database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import config.SystemFilePaths;
import database.classes.EncryptionType;
import database.classes.FileData;
import database.classes.UserData;
import database.exceptions.DatabaseNotLoadedException;
import database.exceptions.FileDataNotPersistedException;
import domain.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class FileRepository extends VeryBasicJsonDataRepository {

    private String dataFile = SystemFilePaths.DATABASE_LOCATION + File.separator + "fileDB.json";
    private String dataFileLocation = SystemFilePaths.DATABASE_LOCATION;


    public String addFile(String fileName, String encryptionKey, EncryptionType encryptionType) throws FileDataNotPersistedException, DatabaseNotLoadedException {
        try {
            createIfNotExists(dataFile, dataFileLocation);

            String newFileId = makeFileId(fileName);
            Map<String, FileData> files = load();

            files.put(newFileId, new FileData(newFileId, fileName, encryptionKey, encryptionType));
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

    private Type getFileDataMapType() {
        return new TypeToken<Map<String, FileData>>() {
        }.getType();
    }

    @Override
    void save(Map files) throws DatabaseNotLoadedException {
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
    public Map<String, FileData> load() throws DatabaseNotLoadedException {
        Gson gson = getGsonInstance();

        File db = new File(dataFile);
        String content;
        try {
            content = FileUtils.readFile(db);
        } catch (IOException e) {
            throw DatabaseNotLoadedException.generic(e);
        }
        Type listType = getFileDataMapType();

        Map<String, FileData> files = gson.fromJson(content, listType);
        return files;
    }
}

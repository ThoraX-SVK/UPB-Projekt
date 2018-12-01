package database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import config.SystemFilePaths;
import database.classes.EncryptionType;
import database.classes.FileData;
import domain.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class FileRepository extends VeryBasicJsonDataRepository {

    private String dataFile = SystemFilePaths.DATABASE_LOCATION + File.separator + "fileDB.json";
    private String dataFileLocation = SystemFilePaths.DATABASE_LOCATION;


    public String addFile(String fileName, String encryptionKey, EncryptionType encryptionType) throws IOException {
        createIfNotExists(dataFile, dataFileLocation);
        String newFileId = makeFileId(fileName);
        Map<String, FileData> files = getAll();

        files.put(newFileId, new FileData(fileName, encryptionKey, encryptionType));
        save(files);

        return newFileId;
    }

    public Map<String, FileData> getAll() throws IOException {
        Gson gson = getGsonInstance();

        File db = new File(dataFile);
        String content = FileUtils.readFile(db);
        Type listType = getFileDataMapType();

        Map<String, FileData> files = gson.fromJson(content, listType);
        return files;
    }

    public FileData findById(String fileId) throws IOException {
        Map<String, FileData> files = getAll();

        return files.getOrDefault(fileId, null);
    }

    public List<FileData> findByIds(Set<String> fileIds) throws IOException {
        Map<String, FileData> files = getAll();
        List<FileData> result = new ArrayList<>();

        for (String fileId : fileIds) {
            if (files.containsKey(fileId)) {
                result.add(files.get(fileId));
            }
        }

        return result;
    }

    private String makeFileId(String fileName) {
        return new Date().getTime() + "_" + fileName;
    }

    private Type getFileDataMapType() {
        return new TypeToken<Map<String, FileData>>() {}.getType();
    }

    @Override
    void save(Map files) throws IOException {
        Gson gson = getGsonInstance();
        String serializedJson = gson.toJson(files);

        File db = new File(dataFile);
        FileUtils.writeToFile(serializedJson, db);
    }
}

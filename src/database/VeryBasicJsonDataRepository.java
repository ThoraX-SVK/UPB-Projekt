package database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import database.exceptions.DatabaseNotLoadedException;
import domain.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public abstract class VeryBasicJsonDataRepository {

    private String dataFile;
    private String dataFileLocation;

    protected VeryBasicJsonDataRepository(String dataFile, String dataFileLocation) {
        this.dataFile = dataFile;
        this.dataFileLocation = dataFileLocation;
    }

    protected void createIfNotExists() throws IOException {
        FileUtils.createDirectoryIfNotExists(dataFileLocation);
        File db = new File(dataFile);

        if (!db.exists()) {
            db.createNewFile();
            FileOutputStream fos = new FileOutputStream(db);
            fos.write("{}".getBytes());
            fos.close();
        }
    }

    private Gson getGsonInstance() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        return gson;
    }

    protected void save(Map data) throws DatabaseNotLoadedException {
        Gson gson = getGsonInstance();
        String serializedJson = gson.toJson(data);

        File db = new File(dataFile);
        try {
            FileUtils.writeToFile(serializedJson, db);
        } catch (IOException e) {
            throw DatabaseNotLoadedException.generic(e);
        }
    }

    protected Map load() throws DatabaseNotLoadedException {
        Gson gson = getGsonInstance();

        File db = new File(dataFile);
        String content;
        try {
            createIfNotExists();
            content = FileUtils.readFile(db);
        } catch (IOException e) {
            throw DatabaseNotLoadedException.generic(e);
        }
        Type listType = getDataMapType();

        return gson.fromJson(content, listType);
    }

    protected abstract Type getDataMapType();

}

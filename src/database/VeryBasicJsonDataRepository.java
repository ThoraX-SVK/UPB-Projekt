package database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domain.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public abstract class VeryBasicJsonDataRepository {


    public void createIfNotExists(String dataFile, String dataFileLocation) throws IOException {
        FileUtils.createDirectoryIfNotExists(dataFileLocation);
        File db = new File(dataFile);

        if (!db.exists()) {
            db.createNewFile();
            FileOutputStream fos = new FileOutputStream(db);
            fos.write("{}".getBytes());
            fos.close();
        }
    }


    protected Gson getGsonInstance() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        return gson;
    }

    abstract void save(Map data) throws IOException;
}

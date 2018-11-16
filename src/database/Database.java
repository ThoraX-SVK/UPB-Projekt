package database;

import database.exceptions.UserAlreadyExistsException;

import java.io.*;
import java.util.StringTokenizer;

import config.SystemFilePaths;

import static java.awt.SystemColor.text;

public class Database {

    public static final String DATABASE_FILE = SystemFilePaths.DATABASE_LOCATION + File.separator + "db.txt";


    private static void createIfNotExists() throws IOException {
        File db = new File(DATABASE_FILE);
        try {
            db.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public static void add(String username, String passwordAndSalt) throws IOException, UserAlreadyExistsException {
        if (exists(username))
            throw UserAlreadyExistsException.fromUsername(username);

        FileWriter fw = new FileWriter(DATABASE_FILE, true);
        fw.write(text + System.lineSeparator());
        fw.close();
    }

    public static Result find(String fileName) throws IOException {
        File frJm = new File(fileName);
        if (frJm.exists() == true){
            FileReader fr = new FileReader(frJm);
            BufferedReader bfr = new BufferedReader(fr);
            String riadok;
            String token;

            while ((riadok=bfr.readLine()) != null){
                StringTokenizer st = new StringTokenizer(riadok, ":");
                token = st.nextToken();
                if (token.equals(text)){
                    fr.close();
                }
            }
            fr.close();
        }
        return null;
    }

    public static boolean exists(String username) throws IOException {
        Result found = find(username);
        return !(found == null);
    }

}

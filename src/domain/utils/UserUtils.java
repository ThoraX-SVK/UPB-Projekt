package domain.utils;

import java.io.File;

import static config.SystemFilePaths.USER_FILES_UPLOAD_DIRECTORY;

public class UserUtils {

    public static String getUserFileDirectory(String username) {
        return USER_FILES_UPLOAD_DIRECTORY + File.separator + username;
    }
}

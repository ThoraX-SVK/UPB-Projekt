package domain.utils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MultiPartUtils {

    public static File getFileFromMultiParts(HttpServletRequest request, String dir) throws Exception {
        List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
        File temp = null;

        for (FileItem item : multiparts) {
            FileUtils.createDirectoryIfNotExists(dir);
            String name = new File(item.getName()).getName();
            temp = new File(dir + File.separator + name);
            item.write(temp);
        }

        if (temp == null) throw new IOException();

        return temp;
    }

    public static List<File> getFilesFromMultiParts(HttpServletRequest request, String dir) throws Exception {
        List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
        File temp = null;

        List<File> files = new ArrayList<>();
        for (FileItem item : multiparts) {
            String name = new File(item.getName()).getName();
            temp = new File(dir + File.separator + name);
            item.write(temp);
            files.add(temp);
        }

        if (temp == null) throw new IOException();

        return files;
    }

    public static String getSubmittedFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }

}

package domain.utils;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class FileUtils {

    public static byte[] fileToByteArray(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] dataToEncrypt = IOUtils.toByteArray(fis);
        fis.close();
        return dataToEncrypt;
    }

    public static byte[] constructZipFileByteArray(File file, Map<String, byte[]> dataToZip) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file));

        for (Map.Entry<String, byte[]> entry : dataToZip.entrySet()) {
            ZipEntry toZip = new ZipEntry(entry.getKey());
            zos.putNextEntry(toZip);
            zos.write(entry.getValue());
            zos.closeEntry();
        }

        zos.close();
        FileInputStream fis = new FileInputStream(file);
        byte[] result = IOUtils.toByteArray(fis);
        fis.close();
        return result;
    }

    public static void createDirectoryIfNotExists(String target) throws IOException {
        final Path path = Paths.get(target);

        if (Files.notExists(path)){
            Files.createDirectories(path);
        }
    }

    public static String getJavaDirectory() {
        return System.getProperty("user.dir");
    }


    public static String readFile(File file) throws IOException {

        StringBuilder fileContents = new StringBuilder((int)file.length());

        try (Scanner scanner = new Scanner(file)) {
            while(scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine() + System.lineSeparator());
            }
            return fileContents.toString();
        }
    }

    public static void writeToFile(String content, File file) throws IOException {
        writeToFile(content, file, false);
    }

    public static void writeToFile(String content, File file, boolean append) throws IOException {
        FileOutputStream fooStream = new FileOutputStream(file, append);
        byte[] myBytes = content.getBytes();
        fooStream.write(myBytes);
        fooStream.close();
    }

    public static void writeInputStreamToFile(InputStream is, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        IOUtils.copy(is, fos);
        fos.close();
    }
}

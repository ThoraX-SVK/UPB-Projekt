package database.classes;

public class FileData {

    private String fileName;
    private String encryptionKey;
    private EncryptionType encryptionType;

    public FileData() {
    }

    public FileData(String fileName, String encryptionKey, EncryptionType encryptionType) {
        this.fileName = fileName;
        this.encryptionKey = encryptionKey;
        this.encryptionType = encryptionType;
    }

    public String getFileName() {
        return fileName;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public EncryptionType getEncryptionType() {
        return encryptionType;
    }
}

package database.classes;

public class FileData {

    private String fileId;
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

    public FileData(String fileId, String fileName, String encryptionKey, EncryptionType encryptionType) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.encryptionKey = encryptionKey;
        this.encryptionType = encryptionType;
    }

    public String getFileId() {
        return fileId;
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

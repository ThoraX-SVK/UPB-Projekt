package database.classes;

public class FileData {

    private String fileId;
    private String fileName;
    private String encryptionKey;
    private EncryptionType encryptionType;
    private String uploadFolder;

    public FileData() {
    }

    public FileData(String fileName, String encryptionKey, EncryptionType encryptionType, String uploadFolder) {
        this.fileName = fileName;
        this.encryptionKey = encryptionKey;
        this.encryptionType = encryptionType;
        this.uploadFolder = uploadFolder;
    }

    public FileData(String fileId, String fileName, String encryptionKey, EncryptionType encryptionType, String uploadFolder) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.encryptionKey = encryptionKey;
        this.encryptionType = encryptionType;
        this.uploadFolder = uploadFolder;
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

    public String getUploadFolder() {
        return uploadFolder;
    }

    public void setId(String newFileId) {
        this.fileId = newFileId;
    }
}

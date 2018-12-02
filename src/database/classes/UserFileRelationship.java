package database.classes;

import java.util.HashSet;
import java.util.Set;

public class UserFileRelationship {

    private String username;
    private Set<String> ownedFiles;
    private Set<String> accessibleFiles;

    public UserFileRelationship(String username) {
        this.username = username;
        this.ownedFiles = new HashSet<>();
        this.accessibleFiles = new HashSet<>();
    }

    public UserFileRelationship(String username, Set<String> ownedFiles, Set<String> accessibleFiles) {
        this.username = username;
        this.ownedFiles = ownedFiles;
        this.accessibleFiles = accessibleFiles;
    }

    public static UserFileRelationship empty(String username) {
        return new UserFileRelationship(username);
    }

    public String getUsername() {
        return username;
    }

    public void addOwnedFile(String fileId) {
        if (ownedFiles == null) {
            ownedFiles = new HashSet<>();
        }
        ownedFiles.add(fileId);
    }

    public void addAccessibleFile(String fileId) {
        if (accessibleFiles == null) {
            accessibleFiles = new HashSet<>();
        }
        accessibleFiles.add(fileId);
    }

    public boolean isFileOwnedByUser(String fileId) {
        return getOwnedFiles().contains(fileId);
    }

    public boolean isFileAccessibleByUser(String fileId) {
        return getAccessibleFiles().contains(fileId);
    }

    public Set<String> getOwnedFiles() {
        return ownedFiles;
    }

    public Set<String> getAccessibleFiles() {
        return accessibleFiles;
    }
}

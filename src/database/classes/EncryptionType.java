package database.classes;

public enum EncryptionType {
    ASYMMETRICAL("asymmetrical"),
    SYMMETRICAL("symmetrical");

    private String text;

    EncryptionType(String text) {
        this.text = text;
    }

    public static EncryptionType fromString(String text) {
        for (EncryptionType encryptionType : EncryptionType.values()) {
            if (encryptionType.text.equalsIgnoreCase(text)) {
                return encryptionType;
            }
        }
        throw new IllegalArgumentException();
    }
}

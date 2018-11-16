package domain.crypto.symmetric;

import java.util.Arrays;

public class EncryptionData {

    private String key;
    private byte[] encryptedByteArray;

    private EncryptionData(byte[] encryptedData, String key) {
        this.encryptedByteArray = encryptedData;
        this.key = key;
    }

    public static EncryptionData fromStreamAndKey(byte[] encryptedInputStream, String keyForDecryption) {
        return new EncryptionData(encryptedInputStream, keyForDecryption);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public byte[] getEncryptedByteArray() {
        return encryptedByteArray;
    }

    @Override
    public String toString() {
        return "EncryptionData{" +
                "key='" + key + '\'' +
                ", encryptedByteArray=" + Arrays.toString(encryptedByteArray) +
                '}';
    }

    public String toReadableString() {
        String text;
        try {
            text = new String(encryptedByteArray);
        } catch (Exception e) {
            e.printStackTrace();
            text = "[no text]";
        }
        return "key='" + key + '\'' +
                ", encryptedByteArray=" + text +
                '}';
    }
}

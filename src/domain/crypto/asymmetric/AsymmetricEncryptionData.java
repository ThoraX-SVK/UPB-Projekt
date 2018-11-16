package domain.crypto.asymmetric;

import java.security.PublicKey;


public class AsymmetricEncryptionData {

    private PublicKey publicKey;
    private byte[] key;
    private byte[] encryptedByteArray;

    private AsymmetricEncryptionData(byte[] encryptedByteArray, byte[] key, PublicKey publicKey) {
        this.publicKey = publicKey;
        this.key = key;
        this.encryptedByteArray = encryptedByteArray;
    }

    public static AsymmetricEncryptionData fromStreamAndKeys(byte[] encryptedByteArray, byte[] key, PublicKey publicKey) {
        return new AsymmetricEncryptionData(encryptedByteArray, key, publicKey);
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public byte[] getKey() {
        return key;
    }

    public byte[] getEncryptedByteArray() {
        return encryptedByteArray;
    }
}

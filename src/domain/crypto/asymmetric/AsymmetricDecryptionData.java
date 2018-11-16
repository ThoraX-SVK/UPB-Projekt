package domain.crypto.asymmetric;

import java.security.PrivateKey;
import java.security.PublicKey;


public class AsymmetricDecryptionData {

    private PrivateKey privateKey;
    private byte[] decryptedByteArray;

    private AsymmetricDecryptionData(byte[] encryptedData, PrivateKey privateKey) {
        this.decryptedByteArray = encryptedData;
        this.privateKey = privateKey;
    }

    public static AsymmetricDecryptionData fromStreamAndKey(byte[] encryptedByteArray, PrivateKey privateKey) {
        return new AsymmetricDecryptionData(encryptedByteArray, privateKey);
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public byte[] getDecryptedByteArray() {
        return decryptedByteArray;
    }
}

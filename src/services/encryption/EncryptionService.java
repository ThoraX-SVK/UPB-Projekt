package services.encryption;

import domain.crypto.symmetric.EncryptionData;
import domain.crypto.symmetric.Decrypt;
import domain.crypto.symmetric.Encrypt;
import services.encryption.interfaces.IEncryptionService;

public class EncryptionService implements IEncryptionService {

    @Override
    public EncryptionData encryptByteArray(byte[] toEncrypt) {
        Encrypt encryption = new Encrypt();
        EncryptionData result;

        try {
            result = encryption.encrypt(toEncrypt);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    @Override
    public byte[] decryptData(EncryptionData toDecrypt) {
        Decrypt decryption = new Decrypt();
        byte[] result;
        try {
            result = decryption.decrypt(new String(toDecrypt.getEncryptedByteArray()), toDecrypt.getKey());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }
}

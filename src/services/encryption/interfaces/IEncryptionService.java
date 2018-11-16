package services.encryption.interfaces;

import domain.crypto.symmetric.EncryptionData;

public interface IEncryptionService {
    public EncryptionData encryptByteArray(byte[] toEncrypt);
    public byte[] decryptData(EncryptionData toDecrypt);
}

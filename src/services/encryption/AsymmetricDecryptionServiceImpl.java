package services.encryption;

import domain.crypto.asymmetric.AsymetricDecrypt;
import domain.crypto.asymmetric.AsymetricEncrypt;
import services.encryption.interfaces.AsymmetricDecryptionService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

public class AsymmetricDecryptionServiceImpl implements AsymmetricDecryptionService {

    @Override
    public byte[] decrypt(byte[] toDecrypt, PrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        AsymetricDecrypt asymetricDecrypt = new AsymetricDecrypt();
        byte[] decrypted = asymetricDecrypt.decrypt(toDecrypt, privateKey);
        return decrypted;
    }
}

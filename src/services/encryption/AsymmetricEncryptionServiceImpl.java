package services.encryption;

import services.encryption.interfaces.AsymmetricEncryptionService;
import domain.crypto.asymmetric.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

public class AsymmetricEncryptionServiceImpl implements AsymmetricEncryptionService {

    @Override
    public byte[] encrypt(byte[] toEncrypt, PublicKey puK) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        AsymetricEncrypt asymetricEncrypt = new AsymetricEncrypt();
        byte[] encrypted = asymetricEncrypt.encrypt(toEncrypt, puK);
        return encrypted;
    }
}

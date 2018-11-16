package domain.crypto.asymmetric;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

public class AsymetricDecrypt {

    private Cipher cipher;

    public AsymetricDecrypt() throws NoSuchPaddingException, NoSuchAlgorithmException {
        cipher = Cipher.getInstance("RSA");
    }

    public byte[] decrypt(byte[] toDecrypt, PrivateKey privateKey) throws InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(toDecrypt);
    }

    public void bytesToFile(File writeTo, byte[] bytes) throws IOException {
        FileOutputStream fos = new FileOutputStream(writeTo);
        fos.write(bytes);
        fos.flush();
        fos.close();
    }


}

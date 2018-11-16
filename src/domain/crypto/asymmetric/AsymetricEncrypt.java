package domain.crypto.asymmetric;

import org.apache.commons.io.IOUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

public class AsymetricEncrypt {

    private Cipher cipher;

    public AsymetricEncrypt() throws NoSuchPaddingException, NoSuchAlgorithmException {
        cipher = Cipher.getInstance("RSA");
    }

    public byte[] encrypt(byte[] contentToEncode, PublicKey publicKey) throws InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(contentToEncode);
    }

    public void bytesToFile(File writeTo, byte[] bytes) throws IOException {
        FileOutputStream fos = new FileOutputStream(writeTo);
        fos.write(bytes);
        fos.flush();
        fos.close();
    }
}

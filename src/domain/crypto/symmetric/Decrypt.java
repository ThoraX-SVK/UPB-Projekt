package domain.crypto.symmetric;

import jdk.internal.util.xml.impl.Input;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Decrypt {

    public byte[] decrypt(String encodedStringInBase64, String keyInBase64) throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, IOException, InvalidAlgorithmParameterException, InvalidKeyException {

        byte[] cipherMessage = Base64.getDecoder().decode(encodedStringInBase64.getBytes());
        byte[] key = Base64.getDecoder().decode(keyInBase64.getBytes());

        //This code decomposes the one byte array to initialization vector and actual encoded message
        ByteBuffer byteBuffer = ByteBuffer.wrap(cipherMessage);
        int ivLength = byteBuffer.getInt();
        if(ivLength < 12 || ivLength >= 16) { // check input parameter
            throw new IllegalArgumentException("invalid iv length");
        }
        byte[] initializationVector = new byte[ivLength];
        byteBuffer.get(initializationVector);
        byte[] cipherText = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherText);

        //Decrypt message
        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, initializationVector));
        byte[] plainText = cipher.doFinal(cipherText);

        return plainText;
    }


}

package domain.crypto.symmetric;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;


//This should be as service if we are to use Spring... if not just create new instance I guess
public class Encrypt {

    private SecureRandom secureRandom = new SecureRandom();

    public EncryptionData encrypt(byte[] dataToEncrypt) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {

        //Initialize all the good stuff here
        SecretKey key = generateNewSecretKey();
        byte[] initializationVector = generateNewInitializationVector();
        final Cipher cipher = initializeCipher(key, initializationVector);

        //Encrypt
        byte[] encryptedData = cipher.doFinal(dataToEncrypt);

        //Get it all together to one byte array
        ByteBuffer byteBuffer = constructFinalMessage(initializationVector, encryptedData);
        byte[] cipherMessage = byteBuffer.array();

        //Encode cipher message to Base64 so we can save it as text to file
        byte[] encryptedMessageBase64 = Base64.getEncoder().encode(cipherMessage);
        String sendThisAsTextOrSomethingToUser = new String(encryptedMessageBase64);

        //Encode key so user can save it somewhere
        byte[] KeyInBase64 = Base64.getEncoder().encode(key.getEncoded());
        String keyForDecryption = new String(KeyInBase64);

        return EncryptionData.fromStreamAndKey(encryptedMessageBase64, keyForDecryption);
    }

    private ByteBuffer constructFinalMessage(byte[] initializationVector, byte[] encryptedData) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4 + initializationVector.length + encryptedData.length);
        byteBuffer.putInt(initializationVector.length);
        byteBuffer.put(initializationVector);
        byteBuffer.put(encryptedData);
        return byteBuffer;
    }

    private byte[] transformInputStreamToByteArray(InputStream is) throws IOException {
        //Dunno if we will need this, I use it because I wanted to load things from file
        return org.apache.commons.io.IOUtils.toByteArray(is);
    }

    private SecretKey generateNewSecretKey() {
        byte[] key = new byte[16];
        secureRandom.nextBytes(key);
        return new SecretKeySpec(key, "AES");
    }

    private byte[] generateNewInitializationVector() {
        byte[] iv = new byte[12]; //NEVER REUSE THIS IV WITH SAME KEY
        secureRandom.nextBytes(iv);
        return iv;
    }

    private Cipher initializeCipher(SecretKey key, byte[] initializationVector) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, initializationVector); //128 bit auth tag length
        cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
        return cipher;
    }

    private FileInputStream loadInputStream() throws FileNotFoundException {
        return new FileInputStream(new File("./text.txt"));
    }
}

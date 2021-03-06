//////////////////////////////////////////////////////////////////////////
// TODO:                                                                //
// Uloha1: Vytvorit funkciu na bezpecne generovanie saltu.              //
// Uloha2: Vytvorit funkciu na hashovanie.                              //
// Je vhodne vytvorit aj dalsie pomocne funkcie napr. na porovnavanie   //
// hesla ulozeneho v databaze so zadanym heslom.                        //
//////////////////////////////////////////////////////////////////////////
package services.auth;


import services.auth.interfaces.PasswordSecurity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordSecurityImpl implements PasswordSecurity {

    private static final int ITERATIONS = 40000;
    public static final String DEFAULT_ALGORITHM = "SHA-256";

    public String createHashAndSaltString(String password) {

        byte[] salt = getSalt();
        String stringSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword =  this.hash(password, stringSalt);

        return hashedPassword + ":" + stringSalt;
    }

    public String hash(String password, String salt) {

        String hash = salt + password;
        MessageDigest hasher;
        try {
            hasher = MessageDigest.getInstance(DEFAULT_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            // should never happen as algorithm is not passed as parameter
            return null;
        }

        byte[] processing = hash.getBytes();
        for(int i = 0; i < ITERATIONS; i++) {
            processing = hasher.digest(processing);
        }

        return Base64.getEncoder().encodeToString(processing);
    }

    private static byte[] getSalt() {

        SecureRandom secureRandom = new SecureRandom();
        byte bytes[] = new byte[100];
        secureRandom.nextBytes(bytes);

        return bytes;
    }
}


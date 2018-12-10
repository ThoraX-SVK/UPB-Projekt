package services.encryption;

import domain.crypto.asymmetric.KeyGeneration;
import services.encryption.interfaces.KeyGenerationService;

import java.security.*;

public class KeyGenerationServiceImpl implements KeyGenerationService {

    public KeyPair generateKeys() throws NoSuchProviderException, NoSuchAlgorithmException {
        PrivateKey prK;
        PublicKey puK;
        try {
            KeyGeneration keyGeneration = new KeyGeneration(2048);
            keyGeneration.generateKeys();
            prK = keyGeneration.getPrivateKey();
            puK = keyGeneration.getPublicKey();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
            throw e;
        }

        return new KeyPair(puK, prK);
    }
}

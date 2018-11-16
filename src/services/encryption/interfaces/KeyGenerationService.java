package services.encryption.interfaces;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public interface KeyGenerationService {
    public KeyPair generateKeys() throws NoSuchProviderException, NoSuchAlgorithmException;
}

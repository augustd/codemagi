package com.codemagi.crypto;

import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;

/**
 *
 * @author Konstantin Spirov from
 * http://stackoverflow.com/questions/7953567/checking-if-unlimited-cryptography-is-available
 */
public class TestCipherStrength {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        boolean unlimited = Cipher.getMaxAllowedKeyLength("RC5") >= 256;
        System.out.println("Unlimited cryptography enabled? " + unlimited);
    }

}

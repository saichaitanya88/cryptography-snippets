package Hashes;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * src: https://gist.github.com/SimoneStefani/99052e8ce0550eb7725ca8681e4225c5#gistcomment-2214057
 */
public class AES {
    private static final String ALGO = "AES";
    private static final byte[] keyValue =
            new byte[]{'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};

    /**
     * Encrypt a string with AES algorithm.
     *
     * @param data is a string
     * @return the encrypted string
     */
    public String encrypt(String data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encVal);
    }

    /**
     * Decrypt a string with AES algorithm.
     *
     * @param encryptedData is a string
     * @return the decrypted string
     */
    public String decrypt(String encryptedData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = Base64.getDecoder().decode(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        return new String(decValue);
    }

    /**
     * Generate a new encryption key.
     */
    private Key generateKey() throws Exception {
        return new SecretKeySpec(keyValue, ALGO);
    }

    public static void main(String[] args) throws Exception {
        AES aes = new AES();
        String encrypted = aes.encrypt("The quick brown fox jumps over the lazy dog.");
        // Transmit encrypted message...

        // Receive encrypted message.. but can only decrypt if we have the secret key.
        String decrypted = aes.decrypt(encrypted);
        System.out.println("decrypted: " + decrypted + " len: " + decrypted.length());
        // decrypted: The quick brown fox jumps over the lazy dog.


        // What is bytes change?
        encrypted = "X" + encrypted.substring(1);
        decrypted = aes.decrypt(encrypted);
        System.out.println("decrypted: " + decrypted + " len: " + decrypted.length());
        //decrypted: U�QEAN�ǣC�Ѹ��fox jumps over the lazy dog.

    }

}
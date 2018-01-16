import java.security.SecureRandom;

public class PBKDF2 {
    /**
     * src: https://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
     *
     */
    public static void main(String[] args) throws Exception{
        PBKDF2 p = new PBKDF2();
    }

    public void test(){
        String password = "ThisIsAPassword";
        String generatedSecurePasswordHash = generateStrongPasswordHash(password);
    }

    public String generateStringPasswordHash(String password){
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();
    }

    public byte[] getSalt() throws NoSuchAlgorithmException{
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
    }
    
}

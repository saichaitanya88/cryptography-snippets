import java.security.MessageDigest;

public class SHA256 {
    /**
     * src: https://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
     *
     */
    public static void main(String[] args) throws Exception{
        String text = "The quick brown fox jumps over the lazy dog.";
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        //Add password bytes to digest
        md.update(text.getBytes());
        //Get the hash's bytes
        byte[] bytes = md.digest();
        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        //Get complete hashed password in hex format
        String hash = sb.toString();
        System.out.println("Hash: " + hash);
    }
}

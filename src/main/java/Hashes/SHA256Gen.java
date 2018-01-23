package Hashes;

import java.security.MessageDigest;

public class SHA256Gen{
    public static String hash(byte[] bytesToHash) {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(bytesToHash);
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            String hash = sb.toString();
            return hash;
        }
        catch (Exception ex){
            System.out.println("Error!: " + ex.getMessage());
            System.exit(-1);
        }
        return "";
    }

    public static byte[] hashBytes(byte[] bytesToHash) {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(bytesToHash);
            byte[] bytes = md.digest();
            return bytes;
        }
        catch (Exception ex){
            System.out.println("Error!: " + ex.getMessage());
            System.exit(-1);
        }
        return null;
    }
}

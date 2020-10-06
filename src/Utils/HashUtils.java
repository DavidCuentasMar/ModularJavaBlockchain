package Utils;
import java.security.MessageDigest;
public class HashUtils {
    //Taken from:
    //https://stackoverflow.com/questions/5531455/how-to-hash-some-string-with-sha256-in-java
    public static String calculateHash(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(Exception ex){
           throw new RuntimeException(ex);
        }
        
    }
    
}

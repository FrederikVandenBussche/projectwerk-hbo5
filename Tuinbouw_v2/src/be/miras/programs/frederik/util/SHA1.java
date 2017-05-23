package be.miras.programs.frederik.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Frederik Vanden Bussche
 * 
 * util class die gebruik maakt van SHA1 encryptie
 *
 */
public class SHA1 {

	
	public SHA1() {
	}
	
	/**
	 * @param input String
	 * @return String
	 * @throws NoSuchAlgorithmException
	 * 
	 * returnt de SHA1-encryptie van een string
	 */
	public static String Sha1(String input) throws NoSuchAlgorithmException {
        
		MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
         
        return sb.toString();
    }

	
}

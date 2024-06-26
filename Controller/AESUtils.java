package Controller;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
private static final String ALGORITHM = "AES";
private static final byte[] KEY = "MySuperSecretKey".getBytes();

public static String maHoa(String data) {
	byte[] maHoaData = null;
	try {
		SecretKeySpec secertkey = new SecretKeySpec(KEY, ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		
		cipher.init(cipher.ENCRYPT_MODE, secertkey);
		
		maHoaData = cipher.doFinal(data.getBytes());
		
		
		
	} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
		e.printStackTrace();
	} catch (InvalidKeyException e) {
		e.printStackTrace();
	} catch (IllegalBlockSizeException e) {
		e.printStackTrace();
	} catch (BadPaddingException e) {
		e.printStackTrace();
	}
	
	return Base64.getEncoder().encodeToString(maHoaData);
}
public static String giaiMa(String data) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
	
	byte[] gocData = null;
	SecretKeySpec secretKey = new SecretKeySpec(KEY, ALGORITHM);
	
	Cipher cipher;
	try {
		cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(cipher.DECRYPT_MODE, secretKey);
		
		data = data.replaceAll("[^A-Za-z0-9+/=]", "");
		
		byte[] giaiMaData = Base64.getDecoder().decode(data);
		 gocData = cipher.doFinal(giaiMaData);
		
	} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
		e.printStackTrace();
	}
	
	
	return new String(gocData);
}

}

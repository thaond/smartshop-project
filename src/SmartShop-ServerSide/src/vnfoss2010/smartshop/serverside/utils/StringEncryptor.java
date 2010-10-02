package vnfoss2010.smartshop.serverside.utils;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

/**
 * 
 * @author VoMinhTam
 */
public class StringEncryptor {
	private static final String DES = "DES";
	private static final String BLOWFISH = "Blowfish";
	private static final String DESEDE = "DESede";

	private static String algorithm = "DESede";
	private static Key key = null;
	private static Cipher cipher = null;

	private static void setUp() throws NoSuchAlgorithmException, NoSuchPaddingException {
		key = KeyGenerator.getInstance(algorithm).generateKey();
		cipher = Cipher.getInstance(algorithm);
	}

	static byte[] encrypt(String input) throws InvalidKeyException,
			BadPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, NoSuchPaddingException {
		setUp();
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] inputBytes = input.getBytes();
		return cipher.doFinal(inputBytes);
	}

	static String decrypt(byte[] encryptionBytes)
			throws InvalidKeyException, BadPaddingException,
			IllegalBlockSizeException, NoSuchAlgorithmException, NoSuchPaddingException {
		setUp();
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] recoveredBytes = cipher.doFinal(encryptionBytes);
		String recovered = new String(recoveredBytes);
		return recovered;
	}

	static final String HEXES = "0123456789abcdef";
	public static String toHex(byte[] raw) {
		if (raw == null) {
			return null;
		}
		final StringBuilder hex = new StringBuilder(2 * raw.length);
		for (final byte b : raw) {
			hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(
					HEXES.charAt((b & 0x0F)));
		}
		return hex.toString();
	}
	
	public static byte[] toArrayByte(String txt){
		return new BigInteger(txt, 16).toByteArray(); 
	}
}

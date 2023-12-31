package se.liu.password_manager.cryptography;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * This class is responsible for decrypting passwords with a given algorithm and key.
 */

public class Decrypter
{
    private Cipher cipher;
    private static final String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";  // This is not a file and the showstopper is therefore
    										// not valid.
    public Decrypter() throws NoSuchPaddingException, NoSuchAlgorithmException {
        cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
    }

    public byte[] decryptPassword(byte[] encryptedPassword, SecretKey key, byte[] initVector)
	    throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException
    {
	cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(initVector));

	byte[] originalPassword = cipher.doFinal(encryptedPassword);
	return originalPassword;
    }
}

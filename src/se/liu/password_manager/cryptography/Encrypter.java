package se.liu.password_manager.cryptography;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;


/**
 *  This class is responsible for handling the AES encryption of submitted passwords.
 */

public class Encrypter
{
    private Cipher cipher;
    private static final String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";      // This is not a file and the showstopper is therefore
                                                                                    // not valid.
    public Encrypter() throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
    }

    public byte[][] encryptPassword(byte[] password, SecretKey key)
            throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidParameterSpecException
    {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        AlgorithmParameters par = cipher.getParameters();
        byte[] initVector = par.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] result = cipher.doFinal(password);
        return new byte[][] {result, initVector};
    }
}

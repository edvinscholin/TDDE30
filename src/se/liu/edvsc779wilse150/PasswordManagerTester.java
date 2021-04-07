package se.liu.edvsc779wilse150;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class PasswordManagerTester
{
    public static void main(String[] args)
	    throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException
    {
        Account account = new Account("WilmerS", "fatt");
		EncryptedAccount encryptedAccount = new EncryptedAccount(account);
		System.out.println(encryptedAccount.getUserName() + "\n" + new String(encryptedAccount.getEncryptedPassword()));
    }
}
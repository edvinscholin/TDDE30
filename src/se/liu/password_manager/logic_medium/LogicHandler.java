package se.liu.password_manager.logic_medium;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import se.liu.password_manager.account_management.Account;
import se.liu.password_manager.account_management.AccountAdapter;
import se.liu.password_manager.account_management.AccountList;
import se.liu.password_manager.account_management.AccountType;
import se.liu.password_manager.visual_layer.ButtonOption;
import se.liu.password_manager.cryptography.Decrypter;
import se.liu.password_manager.cryptography.HashEngine;
import se.liu.password_manager.cryptography.KeyDeriver;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;


/**
 * This is a medium between the visual layer and the logical layer, that can give information
 * when the visual layer needs it.
 */

public class LogicHandler
{
    private static final String ACCOUNTS_FILE_NAME = "." + File.separator + "EncryptedAccounts.json";
    private static final String PASSWORD_FILE_NAME = "." + File.separator + "HashedPassword.txt";
    private AccountList accounts;
    private SecretKey key;
    private Decrypter decrypter;
    private byte[] hashSalt;
    private byte[] derivationSalt;
    private static final int SALTLENGTH = 18;

    public LogicHandler(String password) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException {
        this.accounts = readJsonAccountList();
        this.hashSalt = readSaltFromFile()[0];
        this.derivationSalt = readSaltFromFile()[1];
        KeyDeriver keyDeriver = new KeyDeriver(derivationSalt);
        this.key = keyDeriver.deriveKey(password);
        initDecrypter();
    }

    private void initDecrypter() throws NoSuchAlgorithmException, NoSuchPaddingException {
        decrypter = new Decrypter();
    }

    private AccountList readJsonAccountList() throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Account.class, new AccountAdapter());
        Gson gson = builder.create();

        try (Reader reader = new FileReader(ACCOUNTS_FILE_NAME)) {
            return gson.fromJson(reader, AccountList.class);
        }
        catch (FileNotFoundException ignored) {
            return new AccountList();
        }
    }

    public void saveHashToFile(String password) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException,
            FileNotFoundException
    {
        Gson gson = new Gson();
        HashEngine hashEngine = new HashEngine();
        byte[][] hashInfo = {hashEngine.generateHash(password, hashSalt), hashSalt, derivationSalt};

        try (PrintWriter printWriter = new PrintWriter(PASSWORD_FILE_NAME)) {
            gson.toJson(hashInfo, printWriter);
        }
    }

    private byte[] generateSalt() {                 // Generates random salt.
        SecureRandom random = new SecureRandom();   // We place this random number generator here because it is only used
        byte[] salt = new byte[SALTLENGTH];         // once at the setup of main password, it does not require a field for this.
        random.nextBytes(salt);
        return salt;
    }

    public void doAccountAction(ButtonOption buttonOption, Account account, String newUsername, String newPassword,
                                String newAccountNumber, String newEmail, String newDomain, AccountType accountType)
            throws FileNotFoundException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, InvalidParameterSpecException
    {
        switch (buttonOption) {
            case ADD:
                accounts.addAccount(key, newUsername, newPassword, accountType, newAccountNumber, newEmail, newDomain);
                break;
            case REMOVE:
                accounts.removeAccount(account);
                break;
            case EDIT:
                accounts.editAccount(account, key, newUsername, newPassword, newAccountNumber, newEmail, newDomain);
                break;
        }
    }

    public AccountList getAccounts() {
        return accounts;
    }

    public String getAccountPassword(Account account)
            throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException
    {
        return new String(decrypter.decryptPassword(account.getPassword(), key, account.getInitVector()));
    }

    private byte[][] readSaltFromFile() throws IOException {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(PASSWORD_FILE_NAME)) {
            byte[][] salt = gson.fromJson(reader, byte[][].class);
            return new byte[][] {salt[1], salt[2]};                         // This will not be simpler or easier to read with a
        } catch (FileNotFoundException ignored) {                           // constant decsribing the index of two elements.
            return new byte[][] {generateSalt(), generateSalt()};
        }
    }
}

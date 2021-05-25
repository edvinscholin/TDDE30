package se.liu.password_manager.visual_layer;

import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.security.NoSuchAlgorithmException;

/**
 * Acting as start up and window manager for the main program.
 */

public class WindowManager
{
    private PasswordManagerWindow window;
    private static final String FILE_NAME = "." + File.separator + "HashedPassword.txt";

    public WindowManager() {
        this.window = new PasswordManagerWindow();
    }

    private void initManager() throws NoSuchPaddingException, NoSuchAlgorithmException {
        if (isFirstTimeStartup()) {
            doFirstTimeStartup();
        }
        else {
            startLoginWindow();
        }
    }

    private boolean isFirstTimeStartup() {
        File hashedPassword = new File(FILE_NAME);
        return !hashedPassword.exists();
    }

    private void doFirstTimeStartup()  {
        window.show(Window.SETUP);
    }

    private void startLoginWindow()  {
        window.show(Window.LOGIN);
    }

    public static void main(String[] args)
    {
       WindowManager windowManager = new WindowManager();
        try {
            windowManager.initManager();
        }
        catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
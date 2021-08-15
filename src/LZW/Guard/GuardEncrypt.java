//source https://www.codejava.net/coding/file-encryption-and-decryption-simple-example

package LZW.Guard;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class GuardEncrypt {

    public static void encrypt(String key, File inputFile, File outputFile) throws GuardExecption {
        EncryptDecryptFile(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
    }

    public static void decrypt(String key, File inputFile, File outputFile) throws GuardExecption {
        EncryptDecryptFile(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
    }

    private static void EncryptDecryptFile(int cipherName, String password, File inputFile, File outputFile) throws GuardExecption {
        try {
            Key secretKey = new SecretKeySpec(password.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(cipherName, secretKey);

            FileInputStream file = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            file.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);

            file.close();
            outputStream.close();

        } catch (BadPaddingException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | IOException ex) {
            JFrame quick=new JFrame();
            JOptionPane.showMessageDialog(quick,"Guard encountered a problem, Double check your password.","Guard Error",JOptionPane.WARNING_MESSAGE);
            throw new GuardExecption("[Guard] Has run into a problem.", ex);
        }
    }
}

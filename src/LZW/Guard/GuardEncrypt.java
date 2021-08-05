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
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    public static void encrypt(String key, File inputFile, File outputFile)
            throws GuardExecption {
        EncryptDecryptFile(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
    }

    public static void decrypt(String key, File inputFile, File outputFile)
            throws GuardExecption {
        EncryptDecryptFile(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
    }

    private static void EncryptDecryptFile(int cipherName, String password, File inputFile,
                                           File outputFile) throws GuardExecption {
        try {
            Key secretKey = new SecretKeySpec(password.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherName, secretKey);

            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);

            inputStream.close();
            outputStream.close();

        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                |  BadPaddingException
                | IllegalBlockSizeException | IOException ex) {
            throw new GuardExecption("[Guard] Has run into a problem.", ex);
        } catch (InvalidKeyException e){
            JFrame wrong=new JFrame();
            JOptionPane.showMessageDialog(wrong,"Password is incorrect, double check it.","Wrong Password",JOptionPane.WARNING_MESSAGE);
            throw new GuardExecption("[Guard] Has run into a problem.", e);
        }
    }
}

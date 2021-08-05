package LZW;

import GUI.LZWGuardGUI;
import LZW.Compressor.LZW_Compressor_Decompressor;
import LZW.Guard.GuardEncrypt;
import LZW.Guard.GuardExecption;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

public class Runner {

	public static Queue<String> inputSourceQueue = new LinkedList<>();
	public static Queue<String> FileQueue = new LinkedList<>();

	public static void setActivateGuard(boolean activateGuard) {
		Runner.activateGuard = activateGuard;
	}

	public static void setPassword(String password) {
		Runner.password = padPassword(password);
	}

	static boolean activateGuard = false;
	static String password = padPassword("1010101010101010");
	static LZW_Compressor_Decompressor hed = new LZW_Compressor_Decompressor();
	static String[] IN_FILE_PATH = new String[]{"",""};
	static String[] OUT_FILE_PATH = new String[]{""};
	static String[] ENCRYPTED_FILE_PATH = new String[]{""};

	public static void RunCompress() throws GuardExecption {
		LZWGuardGUI.itemCounter = 0;
		LZWGuardGUI.loading.setVisible(true);
		File theDir = new File("..\\Protected_LZW_318503257_205580087\\CompressedFiles");
		if (!theDir.exists()){
			theDir.mkdirs();
		}
		while(!FileQueue.isEmpty()){
			String FileName = FileQueue.remove();
			if(!FileName.contains(".lzw")) {
				IN_FILE_PATH[0] = inputSourceQueue.remove();
				IN_FILE_PATH[1] = "..\\Protected_LZW_318503257_205580087\\CompressedFiles\\" + FileName + ".lzw";
				OUT_FILE_PATH[0] = "..\\Protected_LZW_318503257_205580087\\DecompressedFiles\\Decompressed" + FileName;
				if (activateGuard) {
					CompressGuarded();
				} else
					Compress();
			}else
				LZWGuardGUI.DnDField.setText("Some of the files are already compressed...");
		}
		LZWGuardGUI.loading.setText("Compressing is done!");

	}
	public static void RunDecompress() throws GuardExecption {

		LZWGuardGUI.loading.setVisible(true);
		LZWGuardGUI.itemCounter = 0;

		File theDir = new File("..\\Protected_LZW_318503257_205580087\\DecompressedFiles");
		if (!theDir.exists()){
			theDir.mkdirs();
		}

		while(!FileQueue.isEmpty()){
			String FileName = FileQueue.remove();
			if (FileName.contains(".lzw")) {
				IN_FILE_PATH[1] = inputSourceQueue.remove();

				FileName = FileName.replace(".lzw","");
				FileName = FileName.replace(".guard","");
				OUT_FILE_PATH[0] = "..\\Protected_LZW_318503257_205580087\\DecompressedFiles\\" + FileName;

				if(activateGuard){
					DecryptGuarded();
				}
				Decompress();

			}else LZWGuardGUI.DnDField.setText("Some of the files are not compressed...");
		}
		LZWGuardGUI.loading.setText("Decompressing is done!");

	}

	public static String padPassword(String io_string_password) {
		while (io_string_password.length() < 16) {
			io_string_password = "*" + io_string_password;
		}
		return io_string_password;
	}

	public static void Compress()
	{
		hed.Compress(IN_FILE_PATH, OUT_FILE_PATH);
	}

	public static void Decompress()
	{
		if(IN_FILE_PATH[1].contains(".guard")){
			LZWGuardGUI.DnDField.setText("Some of the files are encrypted...");
			return;
		}
		hed.Decompress(IN_FILE_PATH, OUT_FILE_PATH);
	}

	public static void CompressGuarded() throws GuardExecption {
		hed.Compress(IN_FILE_PATH, OUT_FILE_PATH);
		File inputFile = new File((IN_FILE_PATH[1]));
		ENCRYPTED_FILE_PATH[0] = "..\\Protected_LZW_318503257_205580087\\CompressedFiles\\" + inputFile.getName() + ".guard";
		File encryptedFile = new File(ENCRYPTED_FILE_PATH[0]);
		GuardEncrypt.encrypt(password, inputFile, encryptedFile);
	}

	public static void DecryptGuarded(){
		try {
			File inputFile = new File((IN_FILE_PATH[1]));

			ENCRYPTED_FILE_PATH[0] = "..\\Protected_LZW_318503257_205580087\\CompressedFiles\\" + inputFile.getName();

			File encryptedFile = new File(ENCRYPTED_FILE_PATH[0]);

			File decryptedFile = new File((OUT_FILE_PATH[0]));

			GuardEncrypt.decrypt(password, encryptedFile, decryptedFile);

			IN_FILE_PATH[1] = "..\\Protected_LZW_318503257_205580087\\CompressedFiles\\" + inputFile.getName().replace(".guard", "");

		}catch (GuardExecption e){ }
	}
}

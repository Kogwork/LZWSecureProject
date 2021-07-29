package LZW;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class Runner {
	static String[] IN_FILE_PATH = new String[] {"E:\\Protected_LZW_318503257_205580087\\src\\LZW\\troll.mp4", "E:\\Protected_LZW_318503257_205580087\\src\\LZW\\troll.lzw"};
	static String[] OUT_FILE_PATH = new String[] {"E:\\Protected_LZW_318503257_205580087\\src\\LZW\\Detroll.mp4"};
	public static void main(String[] args) {
		LZW_Compressor_Decompressor hed = new LZW_Compressor_Decompressor();
		hed.Compress(IN_FILE_PATH, OUT_FILE_PATH);
		hed.Decompress(IN_FILE_PATH, OUT_FILE_PATH);
	}
}

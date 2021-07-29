package LZW;

import java.io.*;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.TimerTask;

/**
 * Assignment 1
 * Submitted by:
 * Student 1. 	ID# XXXXXXXXX
 * Student 2. 	ID# XXXXXXXXX
 */

// Uncomment if you wish to use FileOutputStream and FileInputStream for file access.
//import java.io.FileOutputStream;
//import java.io.FileInputStream;

public class LZW_Compressor_Decompressor implements Compressor {

    protected int m_SizeOfDictionaryElementInBits = 18;
    private int m_DefaultDictionarySize = 256;

    public LZW_Compressor_Decompressor() {

    }

    /*
    w = NIL;
    while ( read a character k )
        {
         if wk exists in the dictionary
             w = wk;
         else
         {
            add wk to the dictionary;
            output the code for w;
            w = k;
          }
        }

     */
    @Override
    public void Compress(String[] input_names, String[] output_names)
    {
        HashMap<String, String> dictionary = new HashMap<String, String>();

        for(int i = 0; i < m_DefaultDictionarySize; i++)
        {
            dictionary.put("" + (char)i, Integer.toString(i));
        }

        BitInputStream inputBit = null;
        BitOutputStream outputBit = null;
        char char_temporaryCharacterToConcatenate;
        String String_inputValueCurrent = "";
        int nextFreeKeyInHashMap = m_DefaultDictionarySize;
        int loadingCounter = 1;

        try
        {
            inputBit = new BitInputStream(new FileInputStream(input_names[0]));
            outputBit = new BitOutputStream(new FileOutputStream(input_names[1]));

            while (true) {
                if (nextFreeKeyInHashMap == (Math.pow(2, m_SizeOfDictionaryElementInBits) / 10) * loadingCounter)
                {
                    System.out.println("[COMPRESSING] progress is " + 10*loadingCounter + "% Done");
                    loadingCounter++;
                }
                /*

                if (nextFreeKeyInHashMap == Math.pow(2,m_SizeOfDictionaryElementInBits))
                {
                    throw new DictionarySizeError("[ERROR] Dictionary size is insufficient : " + Math.pow(2,m_SizeOfDictionaryElementInBits) );
                }

                 */
                char_temporaryCharacterToConcatenate = (char)inputBit.readBits(8); //000000000101010110111
                if(char_temporaryCharacterToConcatenate=='\uFFFF')
                {
                    break;
                }
                if(dictionary.containsKey(String_inputValueCurrent + char_temporaryCharacterToConcatenate))
                {
                    String_inputValueCurrent = String_inputValueCurrent + char_temporaryCharacterToConcatenate;
                }
                else {
                    outputBit.writeBits(m_SizeOfDictionaryElementInBits,Integer.valueOf(dictionary.get(String_inputValueCurrent)));
                    dictionary.put(String_inputValueCurrent + char_temporaryCharacterToConcatenate, Integer.toString(nextFreeKeyInHashMap++));
                    String_inputValueCurrent = "" + char_temporaryCharacterToConcatenate;
                }
            }
            outputBit.writeBits(m_SizeOfDictionaryElementInBits,Integer.valueOf(dictionary.get(String_inputValueCurrent)));
            outputBit.writeBits(m_SizeOfDictionaryElementInBits,Integer.valueOf(dictionary.get(String_inputValueCurrent)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    //saves default 256 values in the dictionary
    /*
    private void InitalizeDictionaryWithDefaultValues(HashMap<String, String> dictionary) {

        for(int i = 0; i < m_DefaultDictionarySize; i++){
            dictionary.put(padBinaryValue(i), Integer.toString(i));
        }

    }


    private Char padBinaryValue(int i_BinaryIntToPad) {

        Char PaddedString = Integer.toBinaryString(i_BinaryIntToPad);

        while(PaddedString.length() < m_SizeOfDictionaryElementInBits)
        {
            PaddedString = "0" + PaddedString;
        }
        return PaddedString;
    }
    /*
        string entry;
        char ch;
        int prevcode, currcode;
        ...

        prevcode = read in a code;
        decode/output prevcode;
        while (there is still data to read)
        {
         currcode = read in a code;
         entry = translation of currcode from dictionary;
         output entry;
         ch = first char of entry;
         add ((translation of prevcode)+ch) to dictionary;
         prevcode = currcode;
    }
     */
    @Override
    public void Decompress(String[] input_names, String[] output_names) {

        HashMap<String, String> DecompressedDictionary = new HashMap<String, String>();

        for(int i = 0; i < m_DefaultDictionarySize; i++)
        {
            DecompressedDictionary.put(Integer.toString(i),"" + (char)i);
        }

        BitInputStream inputBit = null;
        BitOutputStream outputBit = null;

        String inputValueNext = "";
        String inputValueCurrent = "";
        int int_getValueFromHashMap = 0;
        int nextFreeKeyInHashMap = m_DefaultDictionarySize;

        try
        {
            inputBit = new BitInputStream(new FileInputStream(input_names[1]));
            outputBit = new BitOutputStream(new FileOutputStream(output_names[0]));


            int_getValueFromHashMap = (inputBit.readBits(m_SizeOfDictionaryElementInBits)); //000000000101010110111
            inputValueCurrent = DecompressedDictionary.get(String.valueOf(int_getValueFromHashMap));
            outputBit.writeBits(8, inputValueCurrent.charAt(0));


            while (true)
            {
                int_getValueFromHashMap = (inputBit.readBits(m_SizeOfDictionaryElementInBits));
                if(int_getValueFromHashMap==-1)
                {
                    break;
                }
                if(DecompressedDictionary.containsKey(String.valueOf(int_getValueFromHashMap)))
                {
                    inputValueNext = inputValueCurrent;
                    inputValueCurrent = DecompressedDictionary.get(String.valueOf(int_getValueFromHashMap));
                    DecompressedDictionary.put(Integer.toString(nextFreeKeyInHashMap++),inputValueNext + inputValueCurrent.charAt(0));
                }
                else
                {
                    DecompressedDictionary.put(Integer.toString(nextFreeKeyInHashMap++),inputValueCurrent + inputValueCurrent.charAt(0));
                    inputValueCurrent = DecompressedDictionary.get(String.valueOf(int_getValueFromHashMap));
                }
                for (char ch: inputValueCurrent.toCharArray()) {
                    outputBit.writeBits(8,ch);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}

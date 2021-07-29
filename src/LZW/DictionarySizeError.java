package LZW;

public class DictionarySizeError extends Exception
{
    public DictionarySizeError(String errorMessage)
    {
        super(errorMessage);
    }
}

package utils;

import java.io.*;

public class ComUtils {
    private final int CMDSIZE = 4;
    private final int CHARSIZE =1;
    private final int STRSIZE = 40;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public ComUtils(InputStream inputStream, OutputStream outputStream) throws IOException {
        dataInputStream = new DataInputStream(inputStream);
        dataOutputStream = new DataOutputStream(outputStream);
    }

    /**
     * @return Read int de 4 bytes
     * @throws IOException
     */
    public int read_int32() throws IOException {
        byte[] bytes = read_bytes(4);

        return bytesToInt32(bytes, Endianness.BIG_ENNDIAN);
    }

    /**
     * @param number int de 4 bytes
     * @throws IOException
     */
    public void write_int32(int number) throws IOException {
        byte[] bytes = int32ToBytes(number, Endianness.BIG_ENNDIAN);

        dataOutputStream.write(bytes, 0, 4);
    }

    /**
     * @param str Write string de 40 bytes
     * @throws IOException
     */
    public void write_string(String str) throws IOException {
        int numBytes, lenStr;
        byte[] bStr = new byte[STRSIZE];

        lenStr = str.length();

        if (lenStr > STRSIZE)
            numBytes = STRSIZE;
        else
            numBytes = lenStr;

        for (int i = 0; i < numBytes; i++)
            bStr[i] = (byte) str.charAt(i);

        for (int i = numBytes; i < STRSIZE; i++)
            bStr[i] = (byte) ' ';

        dataOutputStream.write(bStr, 0, STRSIZE);
    }
    /**
     * @return Read String 40 bytes
     * @throws IOException
     */
    public String read_string() throws IOException {
        String result;
        byte[] bStr = new byte[STRSIZE];
        char[] cStr = new char[STRSIZE];

        bStr = read_bytes(STRSIZE);

        for (int i = 0; i < STRSIZE; i++)
            cStr[i] = (char) bStr[i];

        result = String.valueOf(cStr);

        return result.trim();
    }

    /**
     * Pasar de int a bytes.
     *
     * @param number
     * @param endianness
     * @return
     */
    private byte[] int32ToBytes(int number, Endianness endianness) {
        byte[] bytes = new byte[4];

        if (Endianness.BIG_ENNDIAN == endianness) {
            bytes[0] = (byte) ((number >> 24) & 0xFF);
            bytes[1] = (byte) ((number >> 16) & 0xFF);
            bytes[2] = (byte) ((number >> 8) & 0xFF);
            bytes[3] = (byte) (number & 0xFF);
        } else {
            bytes[0] = (byte) (number & 0xFF);
            bytes[1] = (byte) ((number >> 8) & 0xFF);
            bytes[2] = (byte) ((number >> 16) & 0xFF);
            bytes[3] = (byte) ((number >> 24) & 0xFF);
        }
        return bytes;
    }

    /**
     * Passar de bytes a enters
     *
     * @param bytes
     * @param endianness
     * @return
     */
    private int bytesToInt32(byte[] bytes, Endianness endianness) {
        int number;

        if (Endianness.BIG_ENNDIAN == endianness) {
            number = ((bytes[0] & 0xFF) << 24) | ((bytes[1] & 0xFF) << 16) |
                    ((bytes[2] & 0xFF) << 8) | (bytes[3] & 0xFF);
        } else {
            number = (bytes[0] & 0xFF) | ((bytes[1] & 0xFF) << 8) |
                    ((bytes[2] & 0xFF) << 16) | ((bytes[3] & 0xFF) << 24);
        }
        return number;
    }

    /**
     * Ens permet llegir bytes
     *
     * @param numBytes
     * @return
     * @throws IOException
     */
    private byte[] read_bytes(int numBytes) throws IOException {
        int len = 0;
        byte[] bStr = new byte[numBytes];
        int bytesread = 0;
        do {
            bytesread = dataInputStream.read(bStr, len, numBytes - len);
            if (bytesread == -1)
                throw new IOException("Cliente desconectado");
            len += bytesread;
        } while (len < numBytes);
        return bStr;
    }



    public enum Endianness {
        BIG_ENNDIAN,
        LITTLE_ENDIAN
    }

    /**
     * Escribe un char
     * @param s
     * @throws IOException
     */
    public void write_char(char s) throws IOException{
        byte bStr[] = new byte[CHARSIZE];

        bStr[0] = (byte) s;

        dataOutputStream.write(bStr, 0, 1);
    }

    /**
     * Lectura de un char y lo devuelve en formato String
     * @return String
     * @throws IOException
     */
    public String readChar() throws IOException{
        String str;
        byte bStr[] = new byte[1];
        bStr = read_bytes(CHARSIZE);
        char c = (char) bStr[0];
        str = String.valueOf(c);
        return str;
    }

    /**
     * Lectura de un espacio
     * @return
     * @throws IOException
     */
    public String read_space() throws IOException{
        String str;
        byte bStr[] = new byte[1];
        bStr = read_bytes(CHARSIZE);
        char c = (char) bStr[0];
        str = String.valueOf(c);
        return str;
    }

    /**
     * Nos permite escribir un espacio
     * @throws IOException
     */
    public void write_space() throws IOException
    {
        String str = " ";
        int numBytes, lenStr;
        byte bStr[] = new byte[1];

        lenStr = str.length();

        if (lenStr > 1)
            numBytes = 1;
        else
            numBytes = lenStr;

        for(int i = 0; i < numBytes; i++)
            bStr[i] = (byte) str.charAt(i);

        for(int i = numBytes; i < 1; i++)
            bStr[i] = (byte) ' ';

        dataOutputStream.write(bStr, 0, 1);
    }

    /**
     * Escribe un comando según el parámetro
     * @param str
     * @throws IOException
     */
    public void write_command(String str) throws IOException {
        int numBytes, lenStr;
        byte[] bStr = new byte[CMDSIZE];

        lenStr = str.length();

        if (lenStr > CMDSIZE)
            numBytes = CMDSIZE;
        else
            numBytes = lenStr;

        for (int i = 0; i < numBytes; i++)
            bStr[i] = (byte) str.charAt(i);

        for (int i = numBytes; i < CMDSIZE; i++)
            bStr[i] = (byte) ' ';

        dataOutputStream.write(bStr, 0, CMDSIZE);
    }

    /**
     * Lee un comando
     * @return
     * @throws IOException
     */
    public String read_command() throws IOException {
        String result;
        byte[] bStr = new byte[CMDSIZE];
        char[] cStr = new char[CMDSIZE];

        bStr = read_bytes(CMDSIZE);

        for (int i = 0; i < CMDSIZE; i++)
            cStr[i] = (char) bStr[i];

        result = String.valueOf(cStr);

        return result.trim();
    }

    /**
     * Lectura de un char y lo devuelve en formato char
     * @return
     * @throws IOException
     */
    public char read_character() throws IOException {
        char result;
        byte[] bStr;
        char[] cStr = new char[1];

        bStr = read_bytes(1);
        cStr[0] = (char) bStr[0];
        result = cStr[0];
        return result;
    }

    /**
     * Llegir un string  mida variable size = nombre de bytes especifica la longitud
     *
     * @param size
     * @return
     * @throws IOException
     */
    public String read_string_variable(int size) throws IOException {
        byte[] bHeader = new byte[size];
        char[] cHeader = new char[size];
        int numBytes = 0;

        // Llegim els bytes que indiquen la mida de l'string
        bHeader = read_bytes(size);
        // La mida de l'string ve en format text, per tant creem un string i el parsejem
        for (int i = 0; i < size; i++) {
            cHeader[i] = (char) bHeader[i];
        }
        numBytes = Integer.parseInt(new String(cHeader));

        // Llegim l'string
        byte[] bStr = new byte[numBytes];
        char[] cStr = new char[numBytes];
        bStr = read_bytes(numBytes);
        for (int i = 0; i < numBytes; i++)
            cStr[i] = (char) bStr[i];
        return String.valueOf(cStr);
    }

    /**
     * Escriure un string mida variable, size = nombre de bytes especifica la longitud
     * String str = string a escriure
     *
     * @param size
     * @param str
     * @throws IOException
     */
    public void write_string_variable(int size, String str) throws IOException {

        // Creem una seqüència amb la mida
        byte[] bHeader = new byte[size];
        String strHeader;
        int numBytes = 0;

        // Creem la capçalera amb el nombre de bytes que codifiquen la mida
        numBytes = str.length();

        strHeader = String.valueOf(numBytes);
        int len;
        if ((len = strHeader.length()) < size)
            for (int i = len; i < size; i++) {
                strHeader = "0" + strHeader;
            }
        for (int i = 0; i < size; i++)
            bHeader[i] = (byte) strHeader.charAt(i);
        // Enviem la capçalera
        dataOutputStream.write(bHeader, 0, size);
        // Enviem l'string writeBytes de DataOutputStrem no envia el byte més alt dels chars.
        dataOutputStream.writeBytes(str);
    }
}
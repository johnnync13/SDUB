package utils;

import java.io.*;

public abstract class ComUtils {
    private final int CMDSIZE = 4;
    private final int CHARSIZE =1;
    private final int STRSIZE = 40;

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
    public abstract void write_int32(int number) throws IOException;

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
     * @param str Write string de 40 bytes
     * @throws IOException
     */
    public abstract void write_string(String str) throws IOException;

    /**
     * Pasar de int a bytes.
     *
     * @param number
     * @param endianness
     * @return
     */
    protected byte[] int32ToBytes(int number, byte [] bytes, Endianness endianness) {
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
    protected abstract byte [] read_bytes(int numBytes) throws IOException;


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



    public enum Endianness {
        BIG_ENNDIAN,
        LITTLE_ENDIAN
    }

    public abstract void write_char(char s) throws IOException;

    public String readChar() throws IOException{
        String str;
        byte bStr[] = new byte[1];
        bStr = read_bytes(CHARSIZE);
        char c = (char) bStr[0];
        str = String.valueOf(c);
        return str;
    }

    public String read_space() throws IOException{
        String str;
        byte bStr[] = new byte[1];
        bStr = read_bytes(CHARSIZE);
        char c = (char) bStr[0];
        str = String.valueOf(c);
        return str;
    }

    public abstract void write_space() throws IOException;

    public abstract void write_command(String str) throws IOException;


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

    public int readValue() throws  IOException {
        int value = this.read_int32();
        return value;
    }

    private String split_id(String command) {
        String[] id = command.split("\\s+");
        String result = id[1];
        return result.trim();
    }

    public char read_character() throws IOException {
        char result;
        byte[] bStr;
        char[] cStr = new char[1];

        bStr = read_bytes(1);
        cStr[0] = (char) bStr[0];
        result = cStr[0];
        return result;
    }
}
package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ComUtilsService extends ComUtils{

    private ComUtils comUtils;
    private Logger logger = Logger.getLogger(("Server" + Thread.currentThread().getName() + ".log"));
    private FileHandler fh;
    public DetectErrors detectErrors;
    private ByteBuffer buffer;


    private final SocketChannel socket;

    /**
     * Constructor
     * @param socket
     */
    public ComUtilsService(SocketChannel socket) throws IOException {
        this.socket = socket;
        //Logger
        fh = new FileHandler("Server" + Thread.currentThread().getName() + ".log");
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
        logger.setUseParentHandlers(false);
        detectErrors = new DetectErrors(comUtils);


    }

    @Override
    public String read_string() throws IOException {

        int readBytes = socket.read(buffer);

        if (readBytes == -1) throw new IOException();
        return getStringBuffer();
    }

    public String getStringBuffer() {
        return new String(buffer.array()).trim();
    }

    public Logger getLogger() {
        return logger;
    }

    public void write_int32(int number) throws IOException {
        byte [] bytes = new byte[4];

        int32ToBytes(number,bytes, Endianness.BIG_ENNDIAN);
        buffer = ByteBuffer.wrap(bytes);
        socket.write(buffer);
    }

    public void write_string(String str) throws IOException {

        buffer = ByteBuffer.wrap(str.getBytes());
        socket.write(buffer);
    }


    protected byte[] read_bytes(int numBytes) throws IOException {
        buffer = ByteBuffer.allocate(numBytes);
        if (socket.read(buffer) == -1) throw new IOException();
        return buffer.array();
    }

    public void write_char(char s) throws IOException {
        buffer = buffer.putChar(s);
        socket.write(buffer);

    }

    public void write_space() throws IOException {
        byte [] bytes = new byte[1];
        buffer = ByteBuffer.wrap(bytes);
        socket.write(buffer);

    }

    public void write_command(String str) throws IOException {
        if(str.getBytes().length == 4){
            buffer = ByteBuffer.wrap(str.getBytes());
            socket.write(buffer);
        }
    }

    /**
     * Ens permet llegir una commanda de 4 bytes
     *
     * @return
     * @throws IOException
     */
    public String read_command() {
        String cmd = null;
        try {
            cmd = comUtils.read_command();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cmd;
    }

    public CmdClient r_cmd() {
        CmdClient cmc = null;
        try {
            String cmd = comUtils.read_command();
            cmc = CmdClient.getCommand(cmd);
        } catch (IOException e) {
            System.out.println(e);
        }
        return cmc;
    }


    public int strt() {
        int id = detectErrors.rightInstruction(CmdClient.STRT.toString());
        logger.info(CmdClient.STRT + " " + id);

        if (id < 0)
            return error("15STRT INCORRECT");
        return id;
    }

    public void init(int id) {
        try {
            comUtils.write_command(CmdServer.INIT.toString());
            comUtils.write_space();
            comUtils.write_int32(id);
            logger.info(CmdServer.INIT.toString() + " " + id);

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public int read_cash() {
        int chip = 0;
        String s = "";
        try {
            s += comUtils.read_space();
            chip = comUtils.read_int32();
            logger.info(CmdClient.CASH.toString() + s + chip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chip;
    }

    public void idck(Deck d) {
        try {
            Cards carta = d.getRandomCard(0);
            Cards carta2 = d.getRandomCard(0);
            comUtils.write_command(CmdServer.IDCK.toString());
            comUtils.write_space();
            comUtils.write_char((char) carta.card);
            comUtils.write_char(carta.palo);
            comUtils.write_space();
            comUtils.write_char((char) carta2.card);
            comUtils.write_char(carta2.palo);
            logger.info(CmdServer.IDCK.toString() + " " + carta.toString() + " " + carta2.toString());
            if (d.getClientPoints() == 21)
                d.setBlackJack(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write_show(int len, Deck d, int j) {
        try {
            comUtils.write_command(CmdServer.SHOW.toString());
            comUtils.write_space();
            comUtils.write_char(Character.forDigit(len, 10));
            ArrayList<Cards> aux = d.ServerCards;
            String s = "";
            if (j == 0)
                aux = d.ClientCards;
            for (int i = 0; i < len; i++) {
                comUtils.write_space();
                comUtils.write_char((char) aux.get(i).card);
                char palo = aux.get(i).palo;
                System.out.println(palo);
                comUtils.write_char(aux.get(i).palo);
                s += aux.get(i).toString()+" ";
            }
            logger.info(CmdServer.SHOW.toString() + " " + s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void card(Deck d) {
        try {
            Cards carta = d.getRandomCard(0);
            comUtils.write_command(CmdServer.CARD.toString());
            comUtils.write_space();
            comUtils.write_char((char) carta.card);
            comUtils.write_char(carta.palo);
            logger.info(CmdServer.CARD.toString() + " " + carta.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }
    }

    public int bett(int money, int cash) {
        if (money * 2 <= cash)
            return money * 2;
        logger.info(CmdClient.BETT.toString() + " " + money + " " + cash);
        return -1;
    }

    public void wins(char i, int money) {
        try {
            comUtils.write_command(CmdServer.WINS.toString());
            comUtils.write_space();
            comUtils.write_char(i);
            //comUtils.write_char(Character.forDigit(i,10));
            comUtils.write_space();
            comUtils.write_int32(money);
            logger.info(CmdServer.WINS.toString() + " " + i + " " + money);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isStringUpperCase(String str) {

        //convert String to char array
        char[] charArray = str.toCharArray();

        for (int i = 0; i < charArray.length; i++) {

            //if any character is not in upper case, return false
            if (!Character.isUpperCase(charArray[i]))
                return false;
        }

        return true;
    }

    public int error(String s) {
        try {
            comUtils.write_command("ERRO");
            comUtils.write_space();
            comUtils.write_string(s);
            logger.info(CmdServer.ERRO.toString() + " " + s);
        } catch (IOException e) {
        }
        return -1;
    }

    public void read_space_num() {
        try {
            String s = comUtils.read_space();
            s += comUtils.read_int32();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String read_show(Deck d) {
        String s = "";
        String carta;
        String aux;
        try {
            s += comUtils.read_space();
            char maxlen = comUtils.read_character();
            int max_show = Character.getNumericValue(maxlen);
            s += maxlen;
            for (int i = 0; i < max_show; i++) {
                s += comUtils.read_space();
                carta= comUtils.readChar();//mostrar carta
                s+=carta;
                aux=comUtils.readChar();
                s+=aux;
                //d.addCardServer(carta.charAt(0),aux.charAt(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return s;
    }

}

package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ComUtilsService {

    private ComUtils comUtils;
    private Logger logger = Logger.getLogger(("Server" + Thread.currentThread().getName() + ".log"));
    private FileHandler fh;
    public DetectErrors detectErrors;

    /**
     * Constructor de la clase ComUtilsService
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    public ComUtilsService(InputStream inputStream, OutputStream outputStream) throws IOException {
        comUtils = new ComUtils(inputStream, outputStream);

        //Logger
        fh = new FileHandler("Server" + Thread.currentThread().getName() + ".log");
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
        logger.setUseParentHandlers(false);
        detectErrors = new DetectErrors(comUtils);


    }

    /**
     * Método que devuelve el logger.
     * @return Logger
     */
    public Logger getLogger() {
        return logger;
    }


    /**
     * Método que lee el comando enviado por el cliente.
     * @return CmdClient
     */
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

    /**
     * Método que comprueba el comando STRT
     * @return int
     */
    public int strt() {
        int id = detectErrors.rightInstruction(CmdClient.STRT.toString());
        logger.info(CmdClient.STRT + " " + id);

        return id;
    }

    /**
     * Método que escribe un INIT,seguido de un espacio y un número(apuesta inicial)
     * @param id
     */
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

    /**
     *  Método que lee un espacio y las fichas que quiere apostar como
     *  máximo el cliente.
     *  Devuelve la cantidad de fichas que quiere apostar el cliente.
     * @return int
     */
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

    /**
     * Método que añade 2 cartas a la mano de cliente y envía
     * el comando IDCK,seguido de espacio, seguido de dos cartas,
     * cada una compuesta de su valor y su palo.
     * Ejemplo:
     *     -IDCK Q3 X2
     * @param d
     */
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

    /**
     * Método que escribe el comando SHOW.
     * Formato: SHOW,seguido de espacio,número (cantidad de cartas a enviar),
     * y finalmente las cartas(valor+palo).
     * @param len
     * @param d
     * @param j
     */
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
                comUtils.write_char(aux.get(i).palo);
                s += aux.get(i).toString()+" ";
            }
            logger.info(CmdServer.SHOW.toString() + " " + s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que escribe el comando CARD,seguido de espacio y seguido
     * de la carta. Añade la carta enviada a la mano del cliente.
     * @param d
     */
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

    /**
     * Método para doblar la apuesta actual.
     * Devuelve -1 en caso de que no sea posible doblarla(debido al CASH)
     * @param money
     * @param cash
     * @return int
     */
    public int bett(int money, int cash) {
        if (money * 2 <= cash)
            return money * 2;
        logger.info(CmdClient.BETT.toString() + " " + money + " " + cash);
        return -1;
    }

    /**
     * Método que escribe el comando WIN,seguido de espacio,seguido de número(0,1,2)
     * ,seguido de espacio y la cantidad de dinero jugada en la partida.
     * número='0':
     *       -ganador cliente
     *  número='1':
     *      -ganador servidor
     * número='2':
     *      -empate
     * @param i
     * @param money
     */
    public void wins(char i, int money) {
        try {
            comUtils.write_command(CmdServer.WINS.toString());
            comUtils.write_space();
            comUtils.write_char(i);
            comUtils.write_space();
            comUtils.write_int32(money);
            logger.info(CmdServer.WINS.toString() + " " + i + " " + money);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para escribir el comando ERRO,seguido de espacio y seguido
     * del mensaje error.
     * @param s
     * @return int
     */
    public int error(String s) {
        try {
            comUtils.write_command("ERRO");
            comUtils.write_space();
            comUtils.write_string(s);
            //comUtils.write_string_variable(s.getBytes().length,s);
            logger.info(CmdServer.ERRO.toString() + " " + s);

        } catch (IOException e) {
        }
        return -1;
    }

    /**
     * Método para los comandos STRT y CASH que estan compuestos de más un
     * componente. Lee el espacio y el número que le sigue al comando.
     */
    public void read_space_num() {
        try {
            String s = comUtils.read_space();
            s += comUtils.read_int32();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que lee el SHOW que envia el cliente.
     * Lee el comando SHOW,seguido de espacio,seguido de un número(cantidad de cartas)
     * y finalmente de las cartas a enviar.
     * @param d
     * @return String
     */
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return s;
    }

}

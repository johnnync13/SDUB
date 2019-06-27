package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ComUtilsService {


    private ComUtils comUtils;

    public ComUtilsService(InputStream inputStream, OutputStream outputStream) throws IOException {
        comUtils = new ComUtils(inputStream, outputStream);
    }


    /**
     * Metodo para inicializar la conexion con servidor
     *
     * @param s
     * @throws IOException
     */
    public boolean strt(String s) throws IOException {
        String[] result = s.split(" ");
        if (result.length != 2)
            return false;
        if (!result[0].equals(Cmd.STRT.toString()))
            return false;
        if (Integer.parseInt(result[1]) < 0)
            return false;
        comUtils.write_command(result[0]);
        comUtils.write_space();
        comUtils.write_int32(Integer.parseInt(result[1]));
        return true;
    }

    /**
     * Metodo para cuanto dinero maximo jugar
     *
     * @param s
     * @throws IOException
     */
    public boolean cash(String s) throws IOException {

        comUtils.write_command(Cmd.CASH.toString());
        comUtils.write_space();
        comUtils.write_int32(Integer.parseInt(s));

        return true;
    }

    /**
     * Método que lee el comando INIT que envia el servidor.
     * Y devuelve el Integer que identifica la apuesta inicial.
     * @return int
     */
    public int read_init() {
        int init= 0;
        try {
            comUtils.read_command();
            comUtils.readChar();
            init= comUtils.read_int32();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return init;
    }

    /**
     * Método que según el palo que reciba (ASCII) devuelve
     * el símbolo correspondiente.
     *  Palo = '3':
     *  -Trebol
     *  Palo='4':
     *  -Picas
     *  Palo='5':
     *  -Corazón
     *  Palo='6':
     *  -Diamantes
     * @param aux
     * @return String
     */
    public String getPalo(char aux) {
        if (aux=='3' || aux==('\03'))
            return 	"\u2663";
        if (aux=='4'  || aux==('\04' ))
            return "\u2664";
        if (aux=='5'  || aux==('\05' ))
            return "\u2665";
        if (aux=='6' || aux==('\06' ))
            return "\u2666";
        return "fallo";
    }

    /**.
     * Método que lee el comando IDCK, y dos cartas.(valor+palo) y
     * retorna eso en un formato entendible para el cliente.
     * @param d
     * @return String
     */
    public String read_idck(Deck d) {
        String i = "";
        try {
            i +=comUtils.read_command();
            i += comUtils.read_space();
            String carta = comUtils.readChar();
            i +=carta;
            char aux = comUtils.read_character();
            i += getPalo(aux);
            d.addCard(carta.charAt(0),aux);
            i += comUtils.read_space();
            carta= comUtils.readChar();
            i+=carta;
            aux = comUtils.read_character();
            i += getPalo(aux);
            d.addCard(carta.charAt(0),aux);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return i;
    }


    /**
     * Método que lee el comando SHOW, un número (cantidad de cartas) y
     * las cartas enviadas. Devuelve un String con ese formato pasando los
     * palos(ASCII) a su símbolo correspondiente.
     * @param d
     * @return String
     */
    public String read_show(Deck d) {
        String s = "";
        String carta;
        String palo;
        try {
            s +=comUtils.read_command();
            s += comUtils.read_space();
            char maxlen = comUtils.read_character();
            int max_show = Character.getNumericValue(maxlen);
            s += maxlen;
            for (int i = 0; i < max_show; i++) {
                s += comUtils.read_space();
                carta= comUtils.readChar();//mostrar carta
                s+=carta;
                palo= getPalo(comUtils.read_character());
                s+=palo;
                d.addCardServer(carta.charAt(0),palo.charAt(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return s;
    }

    /**
     * Este método añade la carta enviada del Servidor a Client.
     * Y devuelve un String con CARD,seguido de espacio y la carta enviada.
     * @param d
     * @return String
     */
    public String read_card(Deck d) {
        String s = "";
        String carta;
        char aux;
        try {
            s += comUtils.read_command();
            s += comUtils.read_space();
            carta= comUtils.readChar();
            s+=carta;
            aux=comUtils.read_character();
            d.addCard(carta.charAt(0),aux);
            s += getPalo(aux);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * Método que lee y devuelve al cliente quien ha ganado.
     * Formato:"WIN"+" "+'número'+" "+Integer
     * 'número':es una char que puede ser 0,1,2.
     * -0:implica victoria del cliente
     * -1:Victoria del servidor
     * -2:empate
     * Integer:Cantidad de dinero jugada en esa partida.
     * @return String
     */
    public String read_win() {
        String s = "";
        try {
            s += comUtils.read_command();
            s += comUtils.read_space();
            s+= comUtils.read_character();
            s += comUtils.read_space();
            s += comUtils.read_int32();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * Método que lee el error con cabecera enviado por el Servidor.
     * @return String
     */
    public String read_error(){
        String s = "";
        try{
            comUtils.read_command();
            comUtils.read_space();
            String aux = comUtils.read_string();
            int v = Integer.parseInt(aux.substring(0,2));
            s+=aux.substring(2,2+v);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return s;
    }

    /**
     * Método que lee error enviado por el Servidor.
     * @return String
     */
    public String case_error(){
        String s = "";
        try{
            s += comUtils.read_space();
            s += comUtils.read_string();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return s;
    }

    /**
     * Método que comprueba que un String esté escrito en mayúsculas.
     * @param str
     * @return boolean
     */
    public  boolean isStringUpperCase(String str){

        //convert String to char array
        char[] charArray = str.toCharArray();

        for(int i=0; i < charArray.length; i++){

            //if any character is not in upper case, return false
            if( !Character.isUpperCase( charArray[i] ))
                return false;
        }

        return true;
    }
    /**
     * Metodo para pedir carta
     *
     * @throws IOException
     */
    public void hitt() throws IOException {
        comUtils.write_command(Cmd.HITT.toString());
    }

    /**
     * Metodo para doblar apuesta
     *
     * @throws IOException
     */
    public void bett() throws IOException {
        comUtils.write_command(Cmd.BETT.toString());
    }

    /**
     * Método para enviar el comando SRND y renunciar a la partida.
     * @throws IOException
     */
    public void srnd() throws IOException {
        comUtils.write_command(Cmd.SRND.toString());
    }

    /**
     * Método que envia el comando RPLY para jugar otra partida
     * @throws IOException
     */
    public void rply() throws IOException {
        comUtils.write_command(Cmd.RPLY.toString());
    }

    /**
     * Método que envia EXIT para terminar de jugar
     * @throws IOException
     */
    public void exit() throws IOException {
        comUtils.write_command(Cmd.EXIT.toString());
    }

    /**
     * Método que envia un SHOW, seguido de un número(cantidad de cartas)
     * y las cartas a enviar.
     * @param d
     * @throws IOException
     */
    public void show(Deck d) throws IOException {
        comUtils.write_command(Cmd.SHOW.toString());
        comUtils.write_space();
        comUtils.write_char(Character.forDigit(d.ClientCards.size(), 10));

        for (int i = 0; i < d.ClientCards.size(); i++) {
            comUtils.write_space();
            comUtils.write_char(d.ClientCards.get(i).card);
            comUtils.write_char((char)d.ClientCards.get(i).palo);
        }

    }

    /**
     * Método que lee el comando enviado por el Servidor.
     * @return String
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

    /**
     * Método que escribe un comando específico.
     * @param cmd
     * @throws IOException
     */
    public void write_command(String cmd) throws IOException {
        comUtils.write_command(cmd);
    }
}

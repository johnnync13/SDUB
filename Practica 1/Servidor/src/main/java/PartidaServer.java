import utils.ComUtilsService;
import utils.LogicaPartida;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class PartidaServer extends Thread{
    LogicaPartida logic;
    private Socket socket;

    /**
     * Constructor de la clase PartidaServer
     * @param s
     */
    public PartidaServer(Socket s){
        this.socket = s;
        try {
            InputStream in = s.getInputStream();
            OutputStream o = s.getOutputStream();
            ComUtilsService comUtilsS = new ComUtilsService(in,o);
            s.setSoTimeout(500*1000);
            logic = new LogicaPartida(comUtilsS);
            this.start();

        }
        catch(IOException e){
            try {
                s.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.out.println(e);
        }

    }

    /**
     * Thread de una partida.
     */
    @Override
    public void run(){

        logic.startGame();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

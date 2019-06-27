import utils.ComUtilsService;
import utils.LogicaPartida;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class PartidaServer{
    LogicaPartida logic;

    private SocketChannel socket;
    public PartidaServer(SocketChannel client){
        this.socket = client;
        try {
            ComUtilsService comUtilsS = new ComUtilsService(client);
            client.socket().setSoTimeout(500*1000);
            logic = new LogicaPartida(comUtilsS);
            logic.startGame();

        }
        catch(IOException e){
            try {
                client.socket().close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.out.println(e);
        }

    }



}

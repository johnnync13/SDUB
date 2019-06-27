import utils.ComUtilsError;
import utils.ComUtilsService;
import utils.Deck;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class PartidaClient {
    LogicaPartida logic;
    int opc;

    /**
     * Constructor de la clase PartidaClient
     * @param s
     * @param opc
     */
    public PartidaClient(Socket s, int opc) {
        try {
            this.opc = opc;
            InputStream in = s.getInputStream();
            OutputStream o = s.getOutputStream();
            ComUtilsService comUtilsS = new ComUtilsService(in, o);
            s.setSoTimeout(500 * 1000);
            Deck d = new Deck();
            logic = new LogicaPartida(comUtilsS,d);
            switchPartida(opc);

        } catch (IOException e) {
            try {
                s.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.out.println(e);
        } catch (ComUtilsError comUtilsError) {
            comUtilsError.printStackTrace();
        }
    }
    /**
     * Método para escoger el tipo de partida.
     * Opcion 0:
     * -Partida con cliente escribiendo por consola(Manual)
     * Opcion 1:
     * -Partida automatizada con cliente Random
     * Opcion 2:
     * -Partida automatizada con cliente inteligente
     * @param opc
     * @throws IOException
     * @throws ComUtilsError
     */
    private void switchPartida(int opc) throws IOException, ComUtilsError {
        switch (opc) {
            case 0:
                logic.startManualGame();
                break;
            case 1:
                logic.startRandomGame();
                break;
            case 2:
                logic.basicStrategy();
                break;
            default:
                throw new ComUtilsError("ERRO: OPCION NO VALIDA");
        }
    }
}

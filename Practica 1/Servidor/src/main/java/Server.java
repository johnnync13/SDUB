import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {

        int port = 0;
        try {
            if (args.length > 0) {
                if (args[0].equals("-p")) {
                    port = Integer.parseInt(args[1]);
                } else {
                    throw new Exception("ERROR: PORT");
                }
                ServerSocket server = new ServerSocket(port);
                System.out.println("Buscando clientes...");
                while (true) {
                    Socket s = server.accept();
                    System.out.println("Cliente conectado");
                    PartidaServer p = new PartidaServer(s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
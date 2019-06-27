
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

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
                ServerSocketChannel server = ServerSocketChannel.open();
                Selector selector = Selector.open();
                InetSocketAddress hostAdress = new InetSocketAddress(port);
                server.socket().bind(hostAdress);
                server.configureBlocking(false);
                int operations = server.validOps();
                SelectionKey serverKey = server.register(selector, operations);
                System.out.println("Buscando clientes...");
                while (true) {
                    if (selector != null && server != null) {
                        selector.select();
                    }
                    Set selectKeys = selector.selectedKeys();
                    Iterator it = selectKeys.iterator();

                    while (it.hasNext()) {

                        SelectionKey key = (SelectionKey) it.next();

                        if (key == serverKey && key.isAcceptable()) {
                            SocketChannel client;
                            try {
                                client = (SocketChannel) server.accept();
                                client.configureBlocking(false);
                                SelectionKey clientKey = client.register(selector, SelectionKey.OP_READ);
                                clientKey.attach(new Integer(0));
                                System.out.println("Nuevo Cliente");
                            }catch (IOException ex){
                                ex.printStackTrace();
                            }
                        }else if(key.isReadable()){
                            SocketChannel c = (SocketChannel) key.channel();
                            PartidaServer p = new PartidaServer(c);



                        }
                    }
                }
            }
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
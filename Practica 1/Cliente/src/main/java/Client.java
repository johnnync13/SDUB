import utils.ComUtilsError;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;


public class Client {

    public static void main(String[] args) throws ComUtilsError {

        String IP = null;
        int port = 0, opc = 0;
        Socket s = null;
        try {
            if (args.length > 0) {
                if (args[0].equals("-s")) {
                    IP = args[1];
                } else {
                    throw new ComUtilsError("ERROR: IP NOT FOUND");
                }
                if (args[2].equals("-p")) {

                    port = Integer.parseInt(args[3]);
                } else{
                    throw new ComUtilsError("ERROR: PORT NOT FOUND");
                }
                if (args[4].equals(null)) {
                    opc = 0;
                } else if (args[4].equals("-i")) {
                    opc = Integer.parseInt(args[5]);
                }else {
                    throw new ComUtilsError("ERROR: OPC NOT COMPATIBLE");

                }
                s = new Socket(IP, port);
                s.setSoTimeout(500 * 1000);
                PartidaClient p = new PartidaClient(s,opc);
            } else {
                s.close();
                throw new ComUtilsError("ERRO: ANYONE ARGUMENT FOUNDED");
            }
        } catch (ConnectException e) {
            throw new ComUtilsError(e.getMessage());
        } catch (SocketException e) {
            throw new ComUtilsError(e.getMessage());
        } catch (IOException e) {
            throw new ComUtilsError(e.getMessage());
        } catch (ComUtilsError e) {
            throw new ComUtilsError(e.getMessage());
        }catch(ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }
}
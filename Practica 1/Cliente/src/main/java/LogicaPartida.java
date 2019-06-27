import utils.*;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class LogicaPartida {

    ComUtilsService comUtilsS;
    Deck d;

    /**
     * Constructor de LogicaPartida.
     * @param comUtilsS
     * @param d
     */
    public LogicaPartida(ComUtilsService comUtilsS, Deck d) {
        this.d = d;
        this.comUtilsS = comUtilsS;
    }

    /**
     * Método para jugada manual con Cliente escribiendo por consola.
     * @throws IOException
     * @throws ComUtilsError
     */
    public void startManualGame() throws IOException, ComUtilsError {
        try {
            Cmd cmd_prev = Cmd.STRT;
            Cmd cms = null;
            String cmd;
            Scanner sc = new Scanner(System.in);
            boolean error;
            String cash = "";
            String s = sc.nextLine();
            String strt = cmd_split(s);
            int init;

            if(comUtilsS.isStringUpperCase(strt) == true) {
                comUtilsS.strt(s);
            }
            else{
                System.out.println(("ERROR DE ESCRITURA COMANDO"));
            }
            init = comUtilsS.read_init();
            System.out.println(Cmd.INIT.toString()+" "+init);
            do {
                error = false;
                cmd = sc.nextLine();
                if (cmd.getBytes().length > 4) {
                    String[] result = cmd.split(" ");
                    cash = result[1];
                    cmd = result[0];
                }
                if(comUtilsS.isStringUpperCase(cmd) == true) {
                    cms = Cmd.getCommand(cmd);
                }
                else{
                    System.out.println(("ERROR DE ESCRITURA COMANDO"));
                }
                if(cms ==null){
                    System.out.println("Erro de comando");
                }
                else {
                    switch (cms) {
                        case STRT:
                            comUtilsS.strt(Cmd.STRT.toString()+" "+cash);
                            error = true;
                            break;
                        case CASH:
                            comUtilsS.cash(cash);
                            if (cmd_prev == Cmd.STRT) {
                                System.out.println(comUtilsS.read_idck(d));
                                System.out.println(comUtilsS.read_show(d));
                                cmd_prev = Cmd.CASH;
                            } else {
                                error = true;
                            }
                            break;
                        case HITT:
                            comUtilsS.hitt();
                            if (cmd_prev == Cmd.HITT || cmd_prev == Cmd.BETT || cmd_prev == Cmd.CASH) {
                                System.out.println(comUtilsS.read_card(d));
                                cmd_prev = Cmd.HITT;
                            } else {
                                error = true;
                            }
                            break;
                        case SHOW:
                            comUtilsS.show(d);
                            if (cmd_prev == Cmd.HITT || cmd_prev == Cmd.BETT || cmd_prev == Cmd.CASH) {

                                System.out.println(comUtilsS.read_show(d));

                                System.out.println(comUtilsS.read_win());
                                cmd_prev = Cmd.SHOW;
                            } else {
                                error = true;
                            }
                            break;
                        case BETT:
                            comUtilsS.bett();
                            if (cmd_prev == Cmd.HITT || cmd_prev == Cmd.CASH ){
                                if(init*2>Integer.parseInt(cash)){
                                    error=true;
                                }
                                else {
                                    init=init*2;
                                    cmd_prev = Cmd.BETT;
                                }
                            } else {
                                error = true;
                            }
                            break;
                        case SRND:
                            comUtilsS.srnd();
                            if (cmd_prev == Cmd.BETT || cmd_prev == Cmd.HITT || cmd_prev == Cmd.CASH) {
                                System.out.println(comUtilsS.read_win());
                                cmd_prev = Cmd.SRND;
                            } else {
                                error = true;
                            }
                            break;
                        case RPLY:
                            comUtilsS.rply();
                            if (cmd_prev == Cmd.SHOW || cmd_prev == Cmd.SRND) {
                                cmd_prev = Cmd.STRT;
                                d.restart();
                                System.out.println(Cmd.INIT.toString()+" "+comUtilsS.read_init());

                            } else {
                                error = true;
                            }
                            break;
                        case EXIT:
                            comUtilsS.exit();
                            cmd_prev = Cmd.EXIT;
                            break;
                        case ERRO:
                            System.out.println(comUtilsS.case_error());
                            break;

                    }
                    if (error) {
                        System.out.println(comUtilsS.read_error());
                    }
                }
            } while (cms != Cmd.EXIT);
        } catch (IOException e) {
            throw new ComUtilsError((e.getMessage()));
        }
    }

    /**
     * Método para cuando envian un comando del formato:
     * {"COMANDO"," ",Integer}
     * @param cmd
     * @return String
     */
    private String cmd_split(String cmd) {
        String cms;
        if (cmd.getBytes().length > 4) {
            String[] result = cmd.split(" ");

            cms = result[0];
        } else {
            cms = cmd;
        }
        return cms;
    }

    /**
     * Método para jugada automática con Cliente Random.
     * Opción -i 1.
     * El cliente envia un identificador y un cash aleatorio,
     * También tiene una probabilidad de 1/3 de hacer un HITT,
     * una probabilidad de 1/3 de hacer un BETT y una probabilidad
     * de 1/3 de no hacer ni HITT ni BETT.
     * Después decide aleatoriamente si hacer un SHOW o un SRND.
     * Finalmente, con una probabilidad de 3/4 hará otra partida(RPLY)
     */
    public void startRandomGame(){
        Random r = new Random();
        int aux;
        try {
            int reply = 1;
            boolean bett;
            int cash;
            int init = 100;
            aux = r.nextInt(999);
            comUtilsS.strt("STRT " + aux);
            System.out.println("STRT "+aux);
            while (reply != 0){
                System.out.println(Cmd.INIT.toString()+" "+comUtilsS.read_init());
                cash = r.nextInt(999);
                comUtilsS.cash("" + cash);
                System.out.println("CASH "+cash);
                System.out.println(comUtilsS.read_idck(d));
                System.out.println(comUtilsS.read_show(d));
                aux = r.nextInt(3);
                bett = true;
                while(aux != 2) {
                    if (aux == 0) {
                        System.out.println("HITT");
                        comUtilsS.hitt();
                        System.out.println(comUtilsS.read_card(d));
                    } else if (aux == 1 && bett && init*2 <= cash) {
                        init += init *2;
                        comUtilsS.bett();
                        System.out.println("BETT");
                        bett = false;
                    }
                    aux = r.nextInt(3);
                }
                aux = r.nextInt(2);
                if(aux==0) {
                    srnd();
                }
                else {
                    show();
                }
                reply = r.nextInt(4);
                if(reply != 0){
                    System.out.println("RPLY");
                    d.restart();
                    comUtilsS.rply();
                }
            }
            comUtilsS.exit();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Método para jugada automática con Cliente Inteligente.
     * Opción -i 2
     * Hacemos uso del Random para cosas de menor interés,
     * como el identificador a pasar y si hacemos otra partida o no.
     * El cash es fijo de 1600.(No necesita más)
     */
    public void basicStrategy(){
        Random r = new Random();
        int aux;
        try {
            int reply = 1;
            aux = r.nextInt(999);
            comUtilsS.strt("STRT " + aux);
            System.out.println("STRT "+aux);
            boolean play = true;
            while (reply != 0){
                System.out.println(Cmd.INIT.toString()+" "+comUtilsS.read_init());
                comUtilsS.cash("" + 1600);
                System.out.println("CASH "+1600);
                System.out.println(comUtilsS.read_idck(d));
                System.out.println(comUtilsS.read_show(d));
                while(play) {
                    if(!d.clientAce){
                        if(d.clientPoints <=8){
                            hitt();
                        }
                        else if(d.clientPoints==11){
                            doubleHitt();
                        }
                        else{
                            if(d.clientPoints==9){
                                if(d.serverPoints>=3 && d.serverPoints<=6) {
                                    doubleHitt();
                                }
                                else{
                                    hitt();
                                }
                            }
                            else if(d.clientPoints==10){
                                if(d.serverPoints<=9) {
                                    doubleHitt();
                                }
                                else{
                                    hitt();
                                }
                            }
                            else if(d.clientPoints==12){
                                if(d.serverPoints<=6 && d.serverPoints>=4){
                                    show();
                                    play=false;
                                }
                                else{
                                    hitt();
                                }
                            }
                            else if(d.clientPoints == 14 || d.clientPoints==13){
                                if(d.serverPoints<=6){
                                    show();
                                    play=false;
                                }
                                else{
                                    hitt();
                                }
                            }
                            else if(d.clientPoints==15){
                                if(d.serverPoints<=6){
                                    show();
                                    play=false;
                                }
                                else if(d.serverPoints==10){
                                    srnd();
                                    play=false;
                                }
                                else{
                                    hitt();
                                }
                            }
                            else if(d.clientPoints==16){
                                if(d.serverPoints<=6){
                                    show();
                                    play=false;
                                }
                                else if(d.serverPoints==9||d.serverPoints==10||d.serverPoints==11){
                                    srnd();
                                    play=false;
                                }
                                else{
                                    hitt();
                                }
                            }
                            else{
                                show();
                                play=false;
                            }
                        }
                    }
                    else{
                        if(d.clientPoints>21){
                            d.clientAce=false;
                            d.clientPoints-=10;
                        }
                        else if(d.clientPoints==13||d.clientPoints==14){
                            if(d.serverPoints==5||d.serverPoints==6){
                                doubleHitt();
                            }
                            else{
                                hitt();
                            }
                        }
                        else if(d.clientPoints==15||d.clientPoints==16){
                            if(d.serverPoints==4||d.serverPoints==5||d.serverPoints==6){
                                doubleHitt();
                            }
                            else{
                                hitt();
                            }
                        }
                        else if(d.clientPoints==17){
                            if(d.serverPoints==3||d.serverPoints==4||d.serverPoints==5||d.serverPoints==6){
                                doubleHitt();
                            }
                            else{
                                hitt();
                            }
                        }
                        else if(d.clientPoints==18){
                            if(d.serverPoints<=6){
                                doubleStand();
                                play=false;
                            }
                            else if(d.serverPoints<=8){
                                show();
                                play=false;
                            }
                            else{
                                hitt();
                            }
                        }
                        else if(d.clientPoints==19 && d.serverPoints==6){
                            doubleStand();
                        }
                        else{
                            show();
                            play=false;
                        }

                    }
                }
                reply = r.nextInt(2);
                if(reply != 0){
                    System.out.println("RPLY");
                    d.restart();
                    comUtilsS.rply();
                    play=true;
                }
            }
            comUtilsS.exit();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Cliente llama comando HITT y espera un CARD de Servidor.
     * @throws IOException
     */
    public void hitt() throws IOException {
        System.out.println("HITT");
        comUtilsS.hitt();
        System.out.println(comUtilsS.read_card(d));
    }

    /**
     * Cliente hace BETT y HITT y espera CARD del Servidor.
     * @throws IOException
     */
    public void doubleHitt() {
        try {
            comUtilsS.bett();
            System.out.println("BETT");
            System.out.println("HITT");
            comUtilsS.hitt();
            System.out.println(comUtilsS.read_card(d));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cliente llama SHOW y espera SHOW y WIN del Servidor.
     * @throws IOException
     */
    public void show() throws IOException {
        System.out.println("SHOW");
        comUtilsS.show(d);
        System.out.println(comUtilsS.read_show(d));
        System.out.println(comUtilsS.read_win());
    }

    /**
     * Cliente llama SRND y espera WIN del Servidor.
     * @throws IOException
     */
    public void srnd() throws IOException {
        System.out.println("SRND");
        comUtilsS.srnd();
        System.out.println(comUtilsS.read_win());
    }

    /**
     * Cliente llama BETT y espera SHOW y WIN del Servidor.
     * @throws IOException
     */
    public void doubleStand() throws IOException{
        comUtilsS.bett();
        System.out.println("BETT");
        show();
    }
}

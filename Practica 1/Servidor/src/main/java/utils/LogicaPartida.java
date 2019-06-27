package utils;


import java.util.ArrayList;
import java.util.Arrays;

public class LogicaPartida {

    public int initMoney = 100;
    ComUtilsService comUtilsS;
    Deck d;
    int cash;
    int Initmoney = 100;
    int id = -1;
    int bet = 0;
    ArrayList<CmdClient> comandos = new ArrayList<CmdClient>(Arrays.asList(CmdClient.values()));

    /**
     * Constructor de la clase LogicaPartida
     * @param comUtilsS
     */
    public LogicaPartida(ComUtilsService comUtilsS) {
        this.comUtilsS = comUtilsS;
        this.d = new Deck();

    }

    /**
     * Método para comenzar una partida.(Por parte del Servidor)
     */
    public void startGame() {

        id = comUtilsS.strt();
        comUtilsS.init(Initmoney);
        bet = Initmoney;
        CmdClient st;
        String comandoPrevio = "STRT";
        do {
            st = comUtilsS.r_cmd();
            if (comandos.contains(st)) {
                switch (st) {
                    case STRT:
                        comUtilsS.read_space_num();
                        comUtilsS.error("15STRT INCORRECTO");
                        break;
                    case CASH:
                        if (comandoPrevio == "STRT") {
                            cash = comUtilsS.read_cash();
                            comUtilsS.idck(d);
                            d.getRandomCard(1);
                            d.getRandomCard(1);
                            comUtilsS.write_show(1, d, 1);
                            comandoPrevio = "CASH";
                        } else {
                            comUtilsS.read_space_num();
                            comUtilsS.error("15CASH INCORRECTO");
                        }
                        break;
                    case HITT:
                        if (comandoPrevio == "HITT" || comandoPrevio == "BETT" || comandoPrevio == "CASH") {
                            comUtilsS.card(d);
                            comandoPrevio = "HITT";
                        } else {
                            comUtilsS.error("15HITT INCORRECTO");
                        }
                        break;
                    case BETT:
                        if (comandoPrevio == "HITT" || comandoPrevio == "CASH") {
                            int aux = comUtilsS.bett(bet, cash);
                            if (aux == -1) {
                                comUtilsS.error("17CASH INSUFICIENTE");
                            } else {
                                bet = aux;
                            }
                            comandoPrevio = "BETT";
                        } else {
                            comUtilsS.error("15BETT INCORRECTO");
                        }
                        break;
                    case SRND:
                        if (comandoPrevio == "CASH" || comandoPrevio == "HITT" || comandoPrevio == "BETT") {
                            comUtilsS.wins('2', bet);
                            comandoPrevio = "SRND";
                        } else {
                            comUtilsS.error("15SRND INCORRECTO");
                        }
                        break;
                    case SHOW:
                        if (comandoPrevio == "CASH" || comandoPrevio == "HITT" || comandoPrevio == "BETT") {
                            comUtilsS.read_show(d);
                            char winner = whoWins(d);
                            comUtilsS.write_show(d.ServerCards.size(), d, 1);
                            comUtilsS.wins(winner, bet);
                            comandoPrevio = "SHOW";
                        } else {
                            comUtilsS.error("15SHOW INCORRECTO");
                        }
                        break;
                    case RPLY:
                        comUtilsS.getLogger().info(CmdClient.RPLY.toString());
                        if (comandoPrevio == "SHOW" || comandoPrevio == "SRND") {
                            d.restart();
                            d.fillDeck();
                            bet = initMoney;
                            comUtilsS.init(initMoney);
                            comandoPrevio = "STRT";
                        } else {
                            comUtilsS.error("15RPLY INCORRECTO");
                        }
                        break;
                    case EXIT:
                        comUtilsS.getLogger().info(CmdClient.EXIT.toString());
                        System.out.println(st.toString());
                        break;
                }
            } else {
                comUtilsS.error("15COMANDO ERRONEO");
            }
        } while (!st.equals(CmdClient.EXIT));
    }

    /**
     * Método que devuelve el ganador de la partida.
     * return '0':
     * -Ganador: Client
     * return '1':
     * -Ganador: Server
     * return '2':
     * -Empate
     * @param deck
     * @return char
     */
    public char whoWins(Deck deck) {
        if (deck.blackJack && deck.ClientCards.size() == 2)
            return '0';
        int client = deck.getClientPoints();
        int server = deck.getServerPoints();
        while (client > 21 && deck.clientAce > 0) {
            client -= 10;
            deck.setClientAce(deck.clientAce - 10);
        }
        while (server < 17) {
            deck.getRandomCard(1);
            server = deck.getServerPoints();
            if (server > 21 && deck.ServerAce > 0) {
                server -= 10;
                deck.setServerAce(deck.ServerAce - 10);
            } else if (server >= 17 && server < client && deck.ServerAce > 0) {
                server -= 10;
                deck.setServerAce(deck.ServerAce - 10);
            }
        }
        if (client > 21 && server > 21)
            return '2';
        if (client > 21 && server <= 21)
            return '1';
        if (client <= 21 && server > 21)
            return '0';
        if (client > server)
            return '0';
        if (server > client)
            return '1';
        return '2';
    }

}
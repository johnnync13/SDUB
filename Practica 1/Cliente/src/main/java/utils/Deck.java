package utils;

import java.util.ArrayList;
public class Deck {
    public ArrayList<Cards> ClientCards;
    public ArrayList<Cards> ServerCards;
    public boolean clientAce = false;
    public int clientPoints = 0;
    public int serverPoints = 0;

    /**
     * Constructor de la clase Deck
     */
    public Deck() {
        ClientCards = new ArrayList<Cards>();
        ServerCards = new ArrayList<Cards>();
    }

    /**
     * Añade una carta al cliente y actualiza sus puntos
     * @param c
     * @param p
     */
    public void addCard(char c,int p){
        Cards ca = new Cards(c,p);
        if(determineCardValue(ca)==11)
            clientAce=true;
        clientPoints+=determineCardValue(ca);
        ClientCards.add(ca);
    }

    /**
     * Añade una carta a Servidor y actualiza sus puntos.
     * @param c
     * @param p
     */
    public void addCardServer(char c,int p){
        Cards ca = new Cards(c,p);
        ServerCards.add(ca);
        serverPoints+=determineCardValue(ca);
    }

    /**
     * Reinicia las cartas de cliente y servidor y todas las
     * caracterísiticas de partidas anteriores.
     */
    public void restart(){
        this.ClientCards = new ArrayList<Cards>();
        this.ServerCards = new ArrayList<Cards>();
        clientPoints=0;
        serverPoints=0;
        clientAce=false;
    }

    /**
     * Método que devuelve el valor númerico de una carta.
     *      * Ejemplo:
     *      * Input     Output
     *      *   4         4
     *      *   X         10
     *      *   J         10
     *      *   Q         10
     *      *   K         10
     *      *   A         11
     *      * @param card
     *      * @return Integer
     *      * @throws NumberFormatException
     */
    public int determineCardValue(Cards card) throws NumberFormatException {
        int c = Character.getNumericValue(card.getCard());

        if (c == 10) {
            return 11;
        }  else if (c == 33) {
            return 10;
        } else if (c == 19) {
            return 10;
        } else if (c == 26) {
            return 10;
        } else if (c == 20) {
            return 10;
        }
        return c;
    }
}

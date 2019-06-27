package utils;

public class Cards {
    int card;
    char palo;

    /**
     * Constructor de la clase Cards
     * @param card
     * @param palo
     */
    public Cards(int card, char palo) {
        this.card = card;
        this.palo = palo;
    }

    /**
     * Método que retorna el valor de una carta.
     * @return int
     */
    public int getCard() {
        return card;
    }

    /**
     * Método que retorna el palo de una carta.
     * @return char
     */
    public char getPalo() {
        return palo;
    }

    /**
     * Método que retorna el símbolo correspondiente al palo.
     * Palo = '3':
     * -Trebol
     * Palo='4':
     * -Picas
     * Palo='5':
     * -Corazón
     * Palo='6':
     * -Diamantes
     * @return String
     */
    public String getPaloAscii() {
        if (getPalo()==('3') || getPalo()==('\03' ))
            return 	"\u2663";
        if (getPalo()==('4') || getPalo()==('\04'))
            return "\u2664";
        if (getPalo()==('5') || getPalo()==('\05'))
            return "\u2665";
        if (getPalo()==('6') || getPalo()==('\06'))
            return "\u2666";
        return "fallo";
    }

    /**
     * Método que devuelve el valor de una carta como char.
     * @return char
     */
    public char getCardAscii() {
        char c;
        c = (char) getCard();
        return c;
    }

    /**
     * Método que devuelve el valor de una carta y su símbolo.
     * @return String
     */
    @Override
    public String toString() {
        String s = "";
            s+=getCardAscii()+getPaloAscii();
        return s;
    }
}
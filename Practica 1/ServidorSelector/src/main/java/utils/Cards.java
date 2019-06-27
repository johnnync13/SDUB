package utils;

public class Cards {
    int card;
    char palo;
    private boolean Ace;

    public Cards(int card, char palo) {
        this.card = card;
        this.palo = palo;
    }


    public int getCard() {
        return card;
    }

    public char getPalo() {
        return palo;
    }

    public boolean isAce() {
        return Ace;
    }

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

    public char getCardAscii() {
        char c;
        c = (char) getCard();
        //Character.forDigit(c,10);
        return c;
    }

    @Override
    public String toString() {
        String s = "";
            s+=getCardAscii()+getPaloAscii();
        return s;
    }
}
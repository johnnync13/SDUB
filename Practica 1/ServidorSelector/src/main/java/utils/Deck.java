package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Deck {
    public ArrayList<Cards> deck;
    public List<Integer> cartas = Arrays.asList(50, 51, 52, 53, 54, 55, 56, 57, 88, 74, 81, 75, 65);
    public List<Integer> palos = Arrays.asList(3, 4, 5, 6);
    public ArrayList<Cards> ServerCards;
    public ArrayList<Cards> ClientCards;
    public int clientPoints = 0;
    public int ServerPoints = 0;
    public int clientAce = 0;
    public int ServerAce = 0;
    public boolean blackJack = false;

    public Deck() {
        deck = new ArrayList<Cards>();
        fillDeck();
        ServerCards = new ArrayList<Cards>();
        ClientCards = new ArrayList<Cards>();
    }

    public void fillDeck() {
        for (Integer i : cartas)
            for (Integer j : palos) {
                Cards c = new Cards(i, Character.forDigit(j, 10));
                deck.add(c);
            }
    }
    public void restart(){
        this.deck = new ArrayList<Cards>();
        fillDeck();
        this.ServerCards = new ArrayList<Cards>();
        this.ClientCards = new ArrayList<Cards>();
        clientAce = 0;
        ServerAce = 0;
        clientPoints = 0;
        ServerPoints = 0;
    }
    public Cards getRandomCard(int i) {
        Random random = new Random();
        Cards c = deck.get(random.nextInt(deck.size()));
        if (i == 0) {
            ClientCards.add(c);
            clientPoints += determineCardValue(c);
            if(determineCardValue(c)==11)
                clientAce +=10;
        } else {
            ServerCards.add(c);
            ServerPoints += determineCardValue(c);
            if(determineCardValue(c)==11)
                ServerAce +=10;
        }
        deck.remove(c);
        return c;
    }


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
    public int getClientPoints(){
        return clientPoints;
    }
    public int getServerPoints(){
        return ServerPoints;
    }
    public void setClientAce(int i){
        this.clientAce = i;
    }
    public void setServerAce(int i){
        this.ServerAce = i;
    }
    public void setBlackJack(boolean i){
        this.blackJack = i;
    }
}

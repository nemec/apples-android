package ec.nem.apples.generic;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// The Deck class holds a list of cards populated with either
// the contents of a file or a List. Those cards are then shuffled.
public class Deck {
	
	private ArrayList<Card> cards;
	private ArrayList<Card> discard;
	private Random rand;
	
	public Deck(List<Card> l){
		cards = new ArrayList<Card>(l);
		discard = new ArrayList<Card>();
	}
	
	public Deck(List<Card> l, long seed){
		this(l);
		shuffleDeck(seed);
	}

	public void shuffleDeck(){
		Collections.shuffle(cards, rand);
	}
	
	public void shuffleDeck(long seed){
		rand = new Random(seed);
		shuffleDeck();
	}
	
	// We don't want to disturb the deck itself when adding a card,
	// so place it in the discard pile.
	public void add(Card c){
		discard.add(c);
	}
	
	// Removes a card from the shuffled deck
	// Transparently transfer the discard pile to the deck if the deck runs out of cards.
	public Card remove(){
		if(cards.isEmpty()){
			if(discard.isEmpty()){
				return null;
			}
			cards = discard;
			discard = new ArrayList<Card>();
		}
		shuffleDeck();
		return cards.remove(0);
	}
	
	public boolean isEmpty(){
		return cards.isEmpty() && discard.isEmpty();
	}
	
	public String toString(){
		return cards.toString();
	}
	
}

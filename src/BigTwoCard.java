/**
 * The BigTwoCard class is a subclass of the Card class, and is used to model a card used in a
Big Two card game. It should override the compareTo() method it inherited from the Card
class to reflect the ordering of cards used in a Big Two card game. Below is a detailed
description for the BigTwoCard class.
 * @author Marco Brian
 *
 */
public class BigTwoCard extends Card {
	/**
	 * a constructor for building a card with the specified
suit and rank. suit is an integer between 0 and 3, and rank is an integer between 0 and
12.
	 * @param suit suit to be given to card
	 * @param rank rank to be given to card
	 */
	BigTwoCard(int suit, int rank){
		super(suit,rank);
	}
	
	/**
	 * a method for comparing the order of this card with the
specified card. Returns a negative integer, zero, or a positive integer as this card is less
than, equal to, or greater than the specified card.
	 */
	public int compareTo(Card card) {
		int this_rank = ( this.rank + 11 ) % 13; //shifts card rank ordering
		int card_rank =  (card.rank + 11 ) % 13 ; //shifts card rank ordering 
		if (this_rank > card_rank) {
			return 1;
		} else if (this_rank < card_rank) {
			return -1;
		} else if (this.suit > card.suit) {
			return 1;
		} else if (this.suit < card.suit) {
			return -1;
		} else {
			return 0;
		}
	}
}

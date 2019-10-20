/***
 * 
 * The Hand abstract class is a subclass of the CardList class, and is used to model a hand of cards. It
has a private instance variable for storing the player who plays this hand. It also has methods
for getting the player of this hand, checking if it is a valid hand, getting the type of this hand,
getting the top card of this hand, and checking if it beats a specified hand.
 * 
 * @author Marco Brian
 *
 */

public abstract class Hand extends CardList {
	/**
	 * the player who plays this hand.
	 */
	private CardGamePlayer player;
	
	/***
	 * 
	 * a constructor for building a hand
with the specified player and list of cards.
	 * @param player player playing this hand
	 * @param cards cards selected for this hand
	 */
	public Hand(CardGamePlayer player, CardList cards) {
		this.player = player;
		
		for(int i = 0; i < cards.size();i++) {
			this.addCard(cards.getCard(i));
		}
		sort();
	}
	/***
	 * getter method that returns a player
	 * @return player Player playing this hand
	 */
	public CardGamePlayer getPlayer() {
		return player;
	}
	/***
	 * 
	 * returns the top card of the hand after being sorted. Highest value and rank. 
	 * 
	 * @return Card top card of the sorted hand
	 */
	public Card getTopCard() {
		return this.getCard(this.size() - 1);
	}
	
	/**
	 * Check whether player's hand can beat hand on table
	 * @param hand hand on table to be compared to
	 * @return boolean true if player's hand can beat table's hand, false otherwise.
	 */
	public boolean beats(Hand hand) {
		if(this.size()==hand.size()){  //Checks if the number of cards on table's hand is the same as player's hand.
			Card this_top_card = this.getTopCard();
			Card hand_top_card = hand.getTopCard();
			if(this_top_card.compareTo(hand_top_card)==1) { //compare top of card from both hands
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	/***
	 * a method for checking if this is a valid hand.
	 * @return boolean true if valid, false otherwise.
	 */
	abstract boolean isValid();

	/***
	 * a method for returning a string specifying the type of this hand
	 * @return String hand type
	 */
	abstract String getType();

}

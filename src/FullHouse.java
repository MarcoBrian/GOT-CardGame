/***
 * 
 * This hand consists of five cards, with two having the same rank and three
having another same rank. The card in the triplet with the highest suit in a full house
is referred to as the top card of this full house. A full house always beats any straights
and flushes. A full house having a top card with a higher rank beats a full house
having a top card with a lower rank
 * @author Marco Brian
 *
 */
public class FullHouse extends Hand{
	/**
	  * constructor for FullHouse class
	 * 
	 * @param player player having this hand
	 * @param cards cards to be made into a hand
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player,cards);
	}
	
	public String getType() {
		return "FullHouse";
	}
	/***
	 * Representation
	 * 00111 -Three cards ranking higher two cards
	 * 00011 - Three cards ranking lower than two cards
	 *
	 * For a full house there are two types of representation.
	 * Where the three cards can be ranked higher or lower than the two cards.
	 * We need to check for these two cases
	 * 
	 */
	public boolean isValid() {
		if(size()==5) {
			//case 00011
			if(getCard(0).getRank()==getCard(2).getRank()) {
				if(getCard(0).getRank()==getCard(1).getRank() && getCard(3).getRank()==getCard(4).getRank() &&
						getCard(0).getRank()!=getCard(4).getRank()) {
					return true;
				}
				else {
					return false;
				}
			}
			//case 00111
			else if(getCard(0).getRank()==getCard(1).getRank()&&
					getCard(2).getRank()==getCard(3).getRank() &&
					getCard(3).getRank()==getCard(4).getRank()){

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
	
	/**
	 * Since the top card will be the highest rank based on the three cards.
	 * We need to override the getTopCard() meethod.
	 * Thus also according to the two cases:
	 * 00111 - get card index 4
	 * and 
	 * 00011 - get card index 2
	 * 
	 * 
	 * 
	 */
	public Card getTopCard() {
		//Case 00011
		if(getCard(0).getRank()==getCard(2).getRank()) {
			return getCard(2);
		}
		//Case 00111
		else {
			return getCard(4);
		}
	}
	/**
	 * If the other hand is also a fullhouse, we compare the highest rank of the three cards.
	 * FullHouse will beat all straights and flushes.
	 * For other types of hands, return false.
	 */
	public boolean beats(Hand hand) {
		String hand_type=hand.getType();
		if(this.getType()==hand_type) {
			if(this.getTopCard().compareTo(hand.getTopCard())==1) {
				return true;
			}
			else {
				return false;
			}	
		}
		else if (hand_type=="Straight" || hand_type=="Flush") {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	
}

/**
 * This hand consists of five cards with the same suit. The card with the highest
rank in a flush is referred to as the top card of this flush. A flush always beats any
straights. A flush with a higher suit beats a flush with a lower suit. For flushes with
the same suit, the one having a top card with a higher rank beats the one having a top
card with a lower rank
 * @author Marco Brian
 *
 */
public class Flush extends Hand{
	/**
	 * Flush constructor 
	 * @param player player having this hand
	 * @param cards cards to be made into a hand
	 */
	public Flush(CardGamePlayer player, CardList cards) {
		super(player,cards);
	}
	public String getType() {
		return "Flush";
	}
	
	/**
	 * We just have to check if all the cards in the hand have the same suit and that the size of the hand is 5
	 */
	public boolean isValid() {
		if(this.size()==5) {
			if(getCard(0).suit==getCard(1).suit && 
					getCard(1).suit==getCard(2).suit &&
					getCard(2).suit==getCard(3).suit &&
					getCard(3).suit==getCard(4).suit) {
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
	 * First check if the Hand argument is also a Flush. If yes then we compare the top cards.
	 * If Hand argument is a straight then we have to always return true
	 * Else it will be false
	 */
	public boolean beats(Hand hand)
	{	
		String hand_type = hand.getType();
		String player_type = this.getType();
		if(player_type==hand_type) {
			if(this.getTopCard().compareTo(hand.getTopCard())==1) {
				return true;
			}
			return false;
		}
		else if(hand_type=="Straight") {
			return true;
		}
		else {
			return false;
		}

	}
}

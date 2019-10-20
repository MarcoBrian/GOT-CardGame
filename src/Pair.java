/**
 * This hand consists of two cards with the same rank. The card with a higher suit
in a pair is referred to as the top card of this pair. A pair with a higher rank beats a
pair with a lower rank. For pairs with the same rank, the one containing the highest
suit beats the other.
 * @author Marco Brian
 *
 */
public class Pair extends Hand {
	/**
	 * Constructs the Pair object
	 * @param player player having this hand
	 * @param cards cards to be made into a hand
	 */
	public Pair(CardGamePlayer player, CardList cards) {
		super(player,cards);
	}
	
	/**
	 * 
	 */
	public String getType() {
		return "Pair";
	}
	
/**
 *  Check if hand is a pair
 */
	public boolean isValid() {
		if(this.size()==2) {
			if(getCard(0).rank==getCard(1).rank) {
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
}

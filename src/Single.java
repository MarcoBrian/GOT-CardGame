/***
 * This hand consists of only one single card. The only card in a single is
referred to as the top card of this single. A single with a higher rank beats a single
with a lower rank. For singles with the same rank, the one with a higher suit beats the
one with a lower suit.
 * 
 * @author Marco Brian
 *
 */
public class Single extends Hand {
	/***
	 * constructor for Single class
	 * 
	 * @param player player having this hand
	 * @param cards cards to be made into a hand
	 */
	public Single(CardGamePlayer player, CardList cards) {
		super(player,cards);
	}
	public String getType() {
		return "Single";
	}
	/**
	 * Checks if hand has one card 
	 */
	public boolean isValid() {
		if(this.size()==1) {
			return true;
		}
		else {
			return false;
		}
	}
	
}

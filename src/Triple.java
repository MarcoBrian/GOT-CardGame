/**
 * This hand consists of three cards with the same rank. The card with the
highest suit in a triple is referred to as the top card of this triple. A triple with a higher
rank beats a triple with a lower rank.
 * @author Marco Brian
 *
 */
public class Triple extends Hand{
	/**
	 * Constructs the Triple object
	 * @param player player having this hand
	 * @param cards cards to be made into a hand
	 */
	public Triple(CardGamePlayer player, CardList cards) {
		super(player,cards);
	}
	
	public String getType() {
		return "Triple";
	}
/**
 * Check if hand is a triple, check if has 3 cards and same cards.
 */
	public boolean isValid() {
		if(this.size()==3) {
			if(getCard(0).rank==getCard(1).rank && getCard(1).rank==getCard(2).rank) {
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

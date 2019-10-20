/**
 * This hand consists of five cards with consecutive ranks and the same
suit. For the sake of simplicity, 2 and A can only form a straight flush with K but not
with 3. The card with the highest rank in a straight flush is referred to as the top card
of this straight flush. A straight flush always beats any straights, flushes, full houses
and quads. A straight flush having a top card with a higher rank beats a straight flush
having a top card with a lower rank. For straight flushes having top cards with the
same rank, the one having a top card with a higher suit beats one having a top card
with a lower suit.
 * @author Marco Brian
 *
 */
public class StraightFlush extends Hand {
	/**
	 * Constructs the StraightFlush object
	 * @param player player having this hand
	 * @param cards cards to be made into a hand
	 */
	public StraightFlush(CardGamePlayer player, CardList cards) {
		super(player,cards);
	}
	
	public String getType() {
		return "StraightFlush";
	}
	/**
	 * Using the same logic when building a Straight class and a Flush Class.
	 * 
	 * We check whether is it is a Flush and if also a Straight.
	 * Return true only if it is a flush and a straight,
	 * if we find that either one is false we can directly return false.
	 * 
	 */
	public boolean isValid() {
		boolean is_flush=false;
		boolean is_straight=false;

		if(this.size()==5) {
			//Check if hand is a flush
			if(getCard(0).suit==getCard(1).suit && 
					getCard(1).suit==getCard(2).suit &&
					getCard(2).suit==getCard(3).suit &&
					getCard(3).suit==getCard(4).suit) {
				is_flush=true;
			}
			else {
				return false;
			}
			
			// Check if hand is a straight

			if( getCard(0).getRank() == 9 && getCard(1).getRank() == 10 && 
					getCard(2).getRank() == 11 && getCard(3).getRank() == 12 && 
					getCard(4).getRank() == 0)
			{
				is_straight=true;
			}
			else if( getCard(0).getRank() == 10 && getCard(1).getRank() == 11 &&
					getCard(2).getRank() == 12 && getCard(3).getRank() == 0 && 
					getCard(4).getRank() == 1 )
			{
				is_straight=true;
			}
			else if(getCard(0).getRank() == 0 && getCard(1).getRank() == 1 &&
					getCard(2).getRank() == 2 && getCard(3).getRank() == 3 && 
					getCard(4).getRank() == 4) {
				return false;
			}
			else if (getCard(0).getRank() == 1 && getCard(1).getRank() == 2 &&
					getCard(2).getRank() == 3 && getCard(3).getRank() == 4 && 
					getCard(4).getRank() == 5) {
				return false;
			}
			else
			{

				for(int i=0;i<4;i++) {
					if(getCard(i).getRank()+1 != getCard(i+1).getRank()) {
						return false;
					}
				}
				is_straight=true;	
			}
			return (is_straight && is_flush);

		}
		else {
			return false;
		}
	}
	
	/**
	 * If hand argument is also a straight flush then we have to compare the top card of each hand.Else
	 * if the hands are of other size 5 cards, we return true because StraightFlush is the highest rank among
	 * the size 5 cards. Else we return false.
	 */
	
	public boolean beats(Hand hand) {
		String hand_type= hand.getType();
		if(getType()==hand_type) {
			if(this.getTopCard().compareTo(hand.getTopCard()) == 1) {
				return true;
			}
			else 
			{
				return false;
			}	
		}
		else if(hand_type=="Straight" || hand_type=="Flush" || hand_type=="FullHouse" || hand_type=="Quad") {
			return true;
		}
		else {
			return false;
		}
	}

}


/**
 * This hand consists of five cards with consecutive ranks. For the sake of
simplicity, 2 and A can only form a straight with K but not with 3. The card with the
highest rank in a straight is referred to as the top card of this straight. A straight
having a top card with a higher rank beats a straight having a top card with a lower
rank. For straights having top cards with the same rank, the one having a top card
with a higher suit beats the one having a top card with a lower suit.
 * @author Marco Brian
 *
 */
public class Straight extends Hand{
	/**
	 * Constructs the Straight object
	 * @param player player having this hand
	 * @param cards cards to be made into a hand
	 */
	public Straight(CardGamePlayer player, CardList cards) {
		super(player,cards);
	}
	
	public String getType(){
		return "Straight";
	}
	/**
	 * We check if the hand is a valid straight
	 *To do so there are a few special cases we have to consider, the fact that
	 * 2 and A can only form a straight with K we need to consider these cases.
	 * (10 J Q K A) - valid
	 * (J Q K A 2) - valid
	 * (A 2 3 4 5) - invalid
	 * (2 3 4 5 6) - invalid
	 * The rest of the cards we can just check whether or not the cards are increasing
	 * incrementally
	 *
	 */
	public boolean isValid()
	{

		if(size()==5)
		{
			if( getCard(0).getRank() == 9 && getCard(1).getRank() == 10 && 
				getCard(2).getRank() == 11 && getCard(3).getRank() == 12 && 
				getCard(4).getRank() == 0) //(10 J Q K A) - valid
			{
				return true;
			}
			else if( getCard(0).getRank() == 10 && getCard(1).getRank() == 11 &&
					getCard(2).getRank() == 12 && getCard(3).getRank() == 0 && 
					getCard(4).getRank() == 1 ) //(J Q K A 2) - valid
			{
				return true;
			}
			else if(getCard(0).getRank() == 0 && getCard(1).getRank() == 1 &&
					getCard(2).getRank() == 2 && getCard(3).getRank() == 3 && 
					getCard(4).getRank() == 4) { //(A 2 3 4 5) - invalid
				return false;
			}
		  else if (getCard(0).getRank() == 1 && getCard(1).getRank() == 2 &&
					getCard(2).getRank() == 3 && getCard(3).getRank() == 4 && 
					getCard(4).getRank() == 5) { //(2 3 4 5 6) - invalid
			  return false;
			}
			else
			{
				//Check if it is a normal straight. Cards are increasing value
				for(int i=0;i<4;i++) {
					if(getCard(i).getRank()+1 != getCard(i+1).getRank()) {
						return false;
					}
				}
				return true;	
			}
		}
		
		return false;
	}
	
	public boolean beats(Hand hand) {
		if(hand.size()==5) {
			if(hand.getType()==this.getType()) {
				Card hand_top_card = hand.getTopCard();
				Card player_top_card = this.getTopCard();
				if(player_top_card.compareTo(hand_top_card)==1) {
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
		else {
			return false;
		}
	}
	
}

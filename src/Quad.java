/**
 * 
 * This hand consists of five cards, with four having the same rank. The card in
the quadruplet with the highest suit in a quad is referred to as the top card of this quad.
A quad always beats any straights, flushes and full houses. A quad having a top card
with a higher rank beats a quad having a top card with a lower rank.
 * @author Marco Brian
 *
 */
public class Quad extends Hand{
	public Quad(CardGamePlayer player, CardList cards) {
		super(player,cards);
	}
	public String getType() {
		return "Quad";
	}
	/**
	 * Visual Representation
	 * 00001 - 4 cards ranking lower than the 1 card
	 * 01111 - 4 cards ranking higher than the 1 card
	 * 
	 * for this one we also do a similar comparison as with a full house. Because here
	 * we also have two different representation for the Quad hand.
	 * 
	 */
	public boolean isValid() {
		if(size()==5) {
			//Case 00001
			if(getCard(0).getRank()==getCard(1).getRank()) {

				if(getCard(1).getRank()==getCard(2).getRank()&&
						getCard(2).getRank()==getCard(3).getRank() &&
						getCard(0).getRank()!=getCard(4).getRank()){
					return true;
				}
			}
			//Case 01111
			else if (getCard(1).getRank()==getCard(2).getRank()
					&& getCard(2).getRank()==getCard(3).getRank()
					&& getCard(3).getRank()==getCard(4).getRank()) {
				return true;
			}

			return false;
		}

		else {
			return false;
		}

	}
	/**
	 * If we have Hand argument to be a Quad, we compare the highest ranking of the 4 cards.
	 * Quad hand will beat all straight , flushes, and full houses.
	 * Otherwise the function returns false.
	 */
	public boolean beats(Hand hand) {

		String hand_type=hand.getType();

		if( getType()==hand_type){

			if(getTopCard().compareTo(hand.getTopCard())==1){
				return true;
			}

			else{
				return false;
			}

		}

		else if(hand_type=="Straight" || hand_type=="FullHouse" || hand_type=="Flush" ){
			return true;
		}

		else
		{
			return false;
		}

	}



	
	/**
	 * Similar to full houses, we also have to consider the getTopCard for the Quad hand
	 * because it has two different representations. 
	 * 
	 * For Case 01111:
	 * we return the card of index 4
	 * For Case 00001:
	 * we return card of index 3
	 */
	public Card getTopCard(){

		if(getCard(0).getRank()!=getCard(1).getRank()){
			return getCard(4);
		}
		else
		{
			return getCard(3);
		}

	}

}

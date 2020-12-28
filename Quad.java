/**
 * 
 * A subclass of Hand that is one of the 8 legal combinations that can be played.
 * A Quad is a set of five cards, with 4 cards having the same rank and an additional card that can be any rank or suit.
 * A Quad beats any five card combination except the Straight Flush. Therefore its getOrder() method will return a value
 * higher than the previous three combinations.
 * @author Hong Ming, Wong
 *
 */
public class Quad extends Hand{

	/**
	 * The only public constructor that builds a hand with the specified player and cards.
	 * @param player the specified player
	 * @param cards the specified list of cards
	 */
	public Quad(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	
	/**
	 * Checks whether then hand is a Quad
	 * @return returns true if it is a Quad. Else, returns false.
	 */
	public boolean isValid(){
		
		if(size() == 5) {
			sort();
			
			if (getCard(0).getRank() == getCard(1).getRank()) {
				if (getCard(1).getRank() == getCard(2).getRank()) {
					if (getCard(2).getRank() == getCard(3).getRank()) {
						return true;
					}
				}
			}
			
			if (getCard(1).getRank() == getCard(2).getRank()) {
				if (getCard(2).getRank() == getCard(3).getRank()) {
					if (getCard(3).getRank() == getCard(4).getRank()) {
						return true;
					}
				}
			}
			
			return false;
		}
		return false;
	}
	
	/** 
	 * the getTopCard() method is overridden here to reflect that the top card is the highest suit among the quadruplet.
	 * @return Assuming the Quad is valid, the top card of the Quad Hand
	 * 
	 */
	public Card getTopCard() {
		if (getCard(3).getRank() == getCard(4).getRank()) return getCard(4);
		
		return getCard(3);
		
		
	/**
	 * Retrieves the type of hand
	 * @return A string - "Quad"
	 */
	}
	public String getType(){
		return "Quad";
	}
	
	
	/**
	 * Retrieves the order of a Quad among the 8 legal combinations
	 * @return The order of a Quad - 7
	 */
	public int getOrder() {
		return 7;
	}
}

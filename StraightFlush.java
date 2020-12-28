/**
 * 
 * A subclass of Straight that is one of the 8 legal combinations that can be played. 
 * A Straight Flush is a set of five cards that are of consecutive ranks and similar suits.
 * A few methods inherited from the Straight are overridden.
 * This class is a subclass of straight so that the super.isValid() can be used, since a Straight Flush is a subset of a Straight.  
 * A straight flush beats any five card combination, therefore the getOrder() method returns the highest value among the five card combinations.
 * @author Hong Ming, Wong
 *
 */
public class StraightFlush extends Straight{

	
	/**
	 * The only public constructor that builds a hand with the specified player and cards.
	 * @param player the specified player
	 * @param cards the specified list of cards
	 */
	public StraightFlush(CardGamePlayer player, CardList cards) {
		super(player, cards);
		
	}
	
	/**
	 * Checks whether the hand is a Straight Flush
	 * @return returns true if it is a Straight Flush. Else, returns false.
	 */
	public boolean isValid() {
		if (size() != 5) return false;
		
		sort();
		
		
		//we check if it is a straight first
		if (super.isValid() == false) return false;
		
		
		//if it is a straight, we check if the suits are the same
		int standard = getCard(0).getSuit();
		int frequency = 1;
		for (int i = 1; i < 5; i++) {
			if (getCard(i).getSuit() == standard) {
				frequency++;
			}
		}
		if (frequency == 5) return true;
	
		return false;
	}
	
	/**
	 * Retrieves the type of hand
	 * @return A string - "StraightFlush"
	 */
	public String getType() {
		return "StraightFlush";
	}
	
	
	/**
	 * Retrieves the order of a Straight Flush among the 8 legal combinations
	 * @return The order of a Straight Flush - 8
	 */
	public int getOrder() {
		return 8;
	}
}

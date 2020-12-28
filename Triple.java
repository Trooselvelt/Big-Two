/**
 * 
 * A subclass of Hand that is one of the 8 legal combinations that can be played.
 * A triple is a set of three cards that have the same rank.
 * @author Hong Ming, Wong
 *
 */
public class Triple extends Hand {

	/**
	 * The only public constructor that builds a hand with the specified player and cards.
		 * @param player the specified player
	 * @param cards the specified list of cards
	 */
	public Triple(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	
	/**
	 * Checks whether the hand is a Triple
	 * @return returns true if it is a Triple. Else, returns false.
	 */
	public boolean isValid() {
		if (size() == 3) {
			int first = getCard(0).getRank();
			
			for (int i = 1; i < 3; i++) {
				if (getCard(i).getRank() != first) {
					return false;
				}
			}
			
			return true;
		}
	
		return false;
	}
	
	/**
	 * Retrieves the type of hand
	 * @return A string - "Triple"
	 */
	public String getType() {
		return "Triple";
	}
	
	/**
	 * Retrieves the order of a Triple among the 8 legal combinations
	 * @return The order of a Triple - 3
	 */
	public int getOrder() {
		return 3;
	}
	
}

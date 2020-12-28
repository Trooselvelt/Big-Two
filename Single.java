/**
 * 
 * A subclass of Hand that is one of the 8 legal combinations that can be played.
 * A single is simply a hand with a size of one. 
 * @author Hong Ming, Wong
 *
 */
public class Single extends Hand {
	
	/**
	 * The only public constructor that builds a hand with the specified player and cards.
	 * @param player the specified player
	 * @param cards the specified list of cards
	 */
	public Single(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	
	/**
	 * Checks whether the hand is a Single
	 * @return returns true if it is a Single. Else, returns false.
	 */
	public boolean isValid() {
		if (size() == 1) return true;
		return false;
	}
	
	/**
	 * Retrieves the type of hand
	 * @return A string - "Single"
	 */
	public String getType() {
		return "Single";
	}
	
	/**
	 * Retrieves the order of a Quad among the 8 legal combinations
	 * @return The order of a Quad - 1
	 */
	public int getOrder() {
		return 1;
	}
}

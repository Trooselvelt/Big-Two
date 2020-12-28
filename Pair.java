/**
 * 
 * A subclass of Hand that is one of the 8 legal combinations that can be played.
 * A pair is a set of two cards that have the same rank. 
 * @author Hong Ming, Wong
 *
 */
public class Pair extends Hand {
	
	/**
	 * The only public constructor that creates a hand with the specified player and cards.
	* @param player the specified player
	 * @param cards the specified list of cards
	 */
	public Pair(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * Checks whether the Hand is a Pair or not.
	 * @return If the hand is a pair, returns true. Else, returns false.
	 */
	public boolean isValid() {		
		if (size() == 2) {
			Card firstcard = getCard(0);
			Card secondcard = getCard(1);
			if (firstcard.getRank() == secondcard.getRank()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retrieves the type of Hand
	 * @return A String - "Pair"
	 */
	public String getType() {
		if (isValid()) return "Pair";
		return null;
	}
	
	/**
	 * Retrieve the ordering of Pair among the 8 legal combinations.
	 * @return the order of a Pair -  2
	 */
	public int getOrder() {
		return 2;
	}
	
}


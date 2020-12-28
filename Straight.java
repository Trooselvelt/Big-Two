/**
 * 
 * A subclass of Hand that is one of the 8 legal combinations that can be played.
 * A straight are a set of five cards that have consecutive ranks. For simplicity, A and 2 cannot form a straight with
 * 3 but can form a straight with a K. 
 * A straight is the lowest among the five card combinations.
 * @author Hong Ming, Wong
 *
 */
public class Straight extends Hand {

	/**
	 * The only public constructor that builds a hand with the specified player and cards.
	 * @param player the specified player
	 * @param cards the specified list of cards
	 */
	public Straight(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	
	/**
	 * Checks whether the hand is a Straight
	 * @return returns true if it is a Straight. Else, returns false.
	 */
	public boolean isValid() {
		if (size() != 5) return false;
		
		sort();
		
		
		int highestcardposition = 4; //0, 1, 2, 3, 4
		int highestrank = getCard(highestcardposition).getRank();
		
		if (highestrank == 1) {//after sorting, if the highest rank is 2
			if (getCard(3).getRank() == 0) { // if the second Highest rank is A
				if(getCard(2).getRank() == 12) { // 3rd highest must be K
					if(getCard(1).getRank() == 11) { //4th must be Q
						if (getCard(0).getRank() == 10) { //Final must be J
							return true;
						}
					}
				}
			}
		}
		
		else if (highestrank == 0) { //if the highest rank is A instead
			if (getCard(3).getRank() == 12) { // if the second Highest rank is K
				if(getCard(2).getRank() == 11) { //third is a Q
					if(getCard(1).getRank() == 10) { //fourth is a J
						if (getCard(0).getRank() == 9) { //final must be 10
							return true;
						}
					}
				}
			}
		}
	
	
		else { // if top card isn't a 2 or an A
			int frequency = 1; 
			for (int i = highestcardposition - 1; i >= 0; i--) { 
				highestrank = highestrank - 1;
				if (highestrank == getCard(i).getRank()) {
					frequency++;
				}
			}
			if (frequency == 5) return true;
		}
		
		return false;
	}
	
	
	/**
	 * Retrieves the type of hand
	 * @return A string - "Straight"
	 */
	public String getType() {
		return "Straight";
		
	}
	
	/**
	 * Retrieves the order of a Straight among the 8 legal combinations
	 * @return The order of a Straight - 4
	 */
	public int getOrder() {
		return 4;
	}

}

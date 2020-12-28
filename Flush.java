/**
 * 
 * A subclass of Hand that is one of the 8 legal combinations that can be played.
 * A valid Flush is a set of five cards that all have the same suit.
 * A Flush is higher than a straight, therefore the getOrder() method will return a higher value than straight. 
 * @author Hong Ming, Wong
 *
 */
public class Flush extends Hand{
	
	/**
	 * The only public constructor for the Flush class. It creates a Flush object that with the given player and cards.
	 * @param player the specified player
	 * @param cards the specified list of cards
	 */
	public Flush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * Checks whether the hand is a Flush or not. 
	 * @return if the hand is a flush, returns true. Else, returns false.
	 */
	public boolean isValid(){
	
		if(size() == 5) {
			sort();
			int standard = getCard(0).getSuit();
			int frequency = 1;
			for (int i = 1; i < 5; i++) {
				if (getCard(i).getSuit() == standard) {
					frequency++;
				}
			}
			if (frequency == 5) return true;
			
		}	
				
		return false;
	}
	
	/**
	 * The method beats is overridden to reflect that the suit is compared first rather than the rank.
	 * @param hand the specified hand
	 */
	public boolean beats(Hand hand){
		if (hand.getOrder() == 5) {
			int current = this.getTopCard().getSuit();
			int other = hand.getTopCard().getSuit();
			
			if (current > other){
			//we compare the suits first, if this suit is larger then the hand suit then true
				return true;
			}
			
			else if (current == other) {
			//if the suits are equal, we compare rank
				if (this.getTopCard().getRank() > hand.getTopCard().getRank()) {
					return true;
				}
				return false;
			}
			
			//if this suit is smaller than the hand suit 
			return false;
		}
		
		//if hand is not Flush, we simply call the superclass beats method
		else return super.beats(hand);
	}
	
	/**
	 * Retrieves the hand type
	 * @return A string  - "Flush"
	 */
	public String getType(){
		return "Flush";
	}
	
	/**
	 * Retrieve the ordering of Flush among the 8 legal combinations.
	 * @return the order of Flush -  5
	 */
	public int getOrder() {
		return 5;
	}
}

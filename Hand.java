/**
 * The Hand class is a subclass of the CardList class, and is used to model a hand of cards. Besides storing a list of cards,
 * the player holding this hand is also stored in this class.
 * @author Hong Ming, Wong
 *
 */
public abstract class Hand extends CardList {
	private CardGamePlayer player; //the player
	
	
	/**
	 * A constructor set to the protected access modifier so that only its subclasses can call this constructor.
	 * It creates a hand with the given set of cards and assigns the hand to the given player.
	 * @param player The specified player
	 * @param cards The specified List of Cards
	 */
	protected Hand(CardGamePlayer player, CardList cards){
		this.player = player;
		for (int i = 0; i < cards.size(); i++) {
			addCard(cards.getCard(i));
		}
		sort();
	}
	
	
	/**
	 * Retrieves the player who owns this set of hands.
	 * @return the player who owns this set of hands.
	 */
	public CardGamePlayer getPlayer() {
		return player;

	}
	
	/**
	 * This method retrieves that Top Card of the hand. It returns the card with the highest rank. If there are two top cards with the same rank,
	 * it returns the card with higher suit.
	 * @return the top card of the hand.
	 */
	public Card getTopCard() {
		sort();
		if(!isEmpty()) return getCard(this.size() - 1);
		return null;
	}
	
	/**
	 * checks whether the hand beats a given hand. It compares the rank first, then the suit.
	 * @param hand the given hand
	 * @return True if given hand has the same number of cards and is has a lower top card, or the given card is null. 
	 * False if the given hand does not have the same size, or the given hand has a higher top card than the current hand. 
	 */
	public boolean beats(Hand hand) {
		//we assume the current hand and the given hand are both valid
		if (hand == null) return true;
		if (hand.getPlayer() == this.player) return true;
	
		if (this.size() == hand.size()) {
		
			if (this.getType() != hand.getType()) {
			//if they have the same number of cards but different type, it must be a five-card combination
				if (this.getOrder() > hand.getOrder()){
				//we compare the order of the five-card combination
					return true;
				}
				return false;
				
			}
				
			else if (this.getType() == hand.getType()) {
			//if they have the same type we simply compare their top cards
			//hand is a subclass of CardList which stores an arraylist of Card object instead of BigTwoCard
			//we need convert Card Object to BigTwoCard Object to utilized the overridden compareTo() method
				BigTwoCard first = new BigTwoCard(this.getTopCard());
				BigTwoCard second = new BigTwoCard(hand.getTopCard());
			
				if(first.compareTo(second) > 0) return true;
				return false;
			}
		}
		return false;
	}
	
	/**
	 * An abstract class to check whether the hand is a valid combination of the 8 legal combinations. 
	 * 
	 * @return True if the hand adheres the specified legal combination. Else, false.
	 */
	public abstract boolean isValid();
	
	/**
	 * An abstract class to return the type of hand.
	 * @return A string of the type. 
	 * 
	 */
	public abstract String getType();
	
	/**
	 * An abstract class to return the order of the hand among the 8 legal combinations.
	 * It allows for comparison between hands with the same number of cards but are not of the same type.
	 * Single - 1 |
	 * Pair - 2 |
	 * Triple - 3 |
	 * Straight - 4 |
	 * Flush - 5 |
	 * Full House - 6 |
	 * Quad - 7 |
	 * Straight Flush - 8 |
	 * 
	 * Not a legal combination - 0
	 * 
	 * @return the order of the hand
	 */
	public abstract int getOrder();
}

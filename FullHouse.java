/**
 * 
 * A subclass of Hand that is one of the 8 legal combinations that can be played.
 * A Full House is a set of five cards that contain 2 cards that of one type of rank and the other 3 cards of another type of rank.
 * Example: 2 of spades, 2 of clubs, 3 of hearts, 3 of diamonds, 3 of clubs.
 * A Full House is higher than a straight and a Flush. Therefore, the getOrder() method will return a higher value than a straight and a flush.
 * @author Hong Ming, Wong
 *
 */
public class FullHouse extends Hand{
	
	/**
	 * The only public constructor of FullHouse. Creates a hand with the specified player and cards.
	 * @param player the specified player
	 * @param cards the specified list of cards
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player, cards);
		
	}
	
	/**
	 * Checks whether the given hand is a FullHouse or not.
	 * @return If the hand is a valid full house, returns true. If it is not, returns false.
	 */
	public boolean isValid() {
		if (size() != 5) return false;
		
		sort();
		
		
		//after sorting, the two types of ranks will lie on the first and the last card respectively
		int first_rank_type  = getCard(0).getRank(); 
		int second_rank_type = getCard(4).getRank();
		int count = 0;
		
		// if the second card has the same rank as the first
		if (first_rank_type == getCard(1).getRank()) count++;
		//if the fourth card has the same rank as the last
		if (second_rank_type == getCard(3).getRank()) count++;
		//the third card can be either way
		if (getCard(2).getRank() == first_rank_type || getCard(2).getRank() == second_rank_type){
			count++;
		}
		//if all 3 comparisons pass, this is a full house
		if (count == 3) return true;
		return false;
	}
	
	/**
	 * The getTopCard() method is overridden here to obtain the card with the highest suit of the triplets
	 * @return the top card of the Hand
	 */
	public Card getTopCard() {
		//we assume that the card is valid
		//sort by rank and suit first
		sort();
		int thirdCard = getCard(2).getRank();
		int fifthCard = getCard(4).getRank();
		
		
		//if third card and the fifth card have the same, then the triples must be 3, 4, 5
		if (thirdCard == fifthCard) {
			return getCard(4); // we return the last card
		}
		
		//if third card and fifth card are different, then the triples are 1, 2, 3
		return getCard(2);
	}
	
	

	
	/**
	 * Retrieves the type of hand
	 * @return A string - "FullHouse"
	 */
	public String getType() {
		return "FullHouse";
	}
	
	/**
	 * Retrieve the ordering of a Full House among the 8 legal combinations.
	 * @return the order of a Full House -  6
	 */
	public int getOrder() {
		return 6;
	}
		
}

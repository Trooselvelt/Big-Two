/**
 * BigTwoCard is a subclass of Card.
 * It overrides the compareTo() method based on the rules of BigTwo.
 * It has also two public constructors to allow for easier construction of a BigTwoCard object.
 * @author Hong Ming, Wong
 *
 */
public class BigTwoCard extends Card{
	
	/**
	 * A public constructor that takes a specified suit and rank and creates a card from it.
	 * @param suit The specified suit
	 * @param rank The specified rank
	 */
	public BigTwoCard(int suit, int rank){
		super(suit, rank);
		
	}
	
	/**
	 * A public constructor that takes a card and creates a BigTwoCard copy of it.
	 * @param card the specified card
	 */
	public BigTwoCard(Card card) {
		super(card.suit, card.rank);
	}
	
	
	/**
	 * Overrides the superclass compareTo(). A method for comparing the order of this card with the 
specified card according to the rules of BigTwo. 
@param card the card intended to be compared to.
@return Returns a negative integer, zero, or a positive integer as this card is less 
than, equal to, or greater than the specified card.
	 */
	public int compareTo(Card card) {	
		if (this.rank == card.rank) {
		//if they have the same rank, we simply compare their suits
			if (this.suit > card.suit) {
				return 1;
			}
			
			else if (this.suit < card.suit) {
				return -1;
			}
		}
		
		if (this.rank != card.rank) {
		
			if (this.rank == 1) return 1;
			//if this is 2 then this wins
			
			else if (card.rank == 1) return -1;
			//if the other card is 2 then the other wins
			
			else if (this.rank == 0) return 1;
			//if none of the cards are 2 but this is A then this wins
			
			else if (card.rank == 0) return -1;
			//vice versa
			
		
			else if (this.rank > card.rank) return 1;
			//beside the two exception, the rest will perform normally
		
			else if (card.rank > this.rank) return -1;
		
		}
		
		return 0;
	}
	
}

/**
 * The BigTwoDeck class is a subclass of the Deck class, and is used to model a deck of cards 
used in a Big Two card game. It overrides the initialize() method it inherits from the 
Deck  class  to  create  a  deck  of  BigTwo cards instead of the usual cards.
 * @author Hong Ming, Wong
 *
 */
public class BigTwoDeck extends Deck {
	
	/**
	 * The only public constructor for BigTwoDeck creates a Deck of BigTwoCards.
	 * It then shuffles it immediately. 
	 */
	public BigTwoDeck(){
		initialize();
		shuffle();
	}
	
	/**
	 * The initialize() method creates a new Deck of cards that are in order.
	 */
	public void initialize() {
		removeAllCards();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				BigTwoCard card = new BigTwoCard(i, j);
				addCard(card);
			}
		}
	}

}

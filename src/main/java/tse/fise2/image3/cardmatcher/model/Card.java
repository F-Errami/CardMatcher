package tse.fise2.image3.cardmatcher.model;

/**
* The Card class represents a card with a name.
*/

public class Card {

	String name;

	/**
	* Creates a new card with no name.
	*/
	
	public Card() {
	}
	
	/**
	* Creates a new card with the given name.
	* @param name the name of the card
	*/
	
	public Card(String name) {
		this.name = name;
	}
	/**
	* Sets the name of the card.
	* @param name the new name of the card
	*/
	
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Returns the name of the card.
	 * @return the name of the card
	 */
	
	public String getName() {
		return name;
	}
}

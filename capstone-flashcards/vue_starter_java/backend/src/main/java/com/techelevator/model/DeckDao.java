package com.techelevator.model;

import java.util.List;

public interface DeckDao {

	public List<Deck> getSubjectDecks(long subjectId);
	
	public List<Deck> searchByDeckByDeckName(long subjectId, String search);
	
	// editDeck works but I think it should be void. 
	 	// Not sure how to make it work as a void	
	public Deck editDeck(Deck deck);
	
	public void deleteDeck(long deckId);
	
	public Deck createDeck(Deck deck);

	//admins can access all decks
	public List<Deck> getAllDecks();
	

	public List<Deck> getDecksByCardContents(String search);

	public List<Deck> getAllUserDecks();

	

}

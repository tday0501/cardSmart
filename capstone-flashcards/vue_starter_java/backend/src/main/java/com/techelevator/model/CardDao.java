package com.techelevator.model;

import java.util.List;

public interface CardDao {
	
	public Card getOneUserCard(long cardId);
	
	public List<Card> searchByText(long userId, String search);
	
	public List<Card> getAllCardsFromDeck(long deckId);	
	
	
	// editCard works but I think it should be void. 
		// Not sure how to make it work as a void
	public Card editCard(Card card);
	
	public void deleteCard(Long cardId);
	
	public Card createCard(Card card);
		
	// admins can access all cards
	public List<Card> getAllCards(); 
	
	
	

}

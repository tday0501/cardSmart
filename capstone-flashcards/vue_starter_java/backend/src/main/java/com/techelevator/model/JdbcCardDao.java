package com.techelevator.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcCardDao implements CardDao {
	
	 private JdbcTemplate jdbcTemplate;
	 
	 @Autowired
	 public JdbcCardDao (BasicDataSource dataSource) {
		 jdbcTemplate = new JdbcTemplate (dataSource);
	 }
	 
	@Override
	public Card createCard(Card card) {
		String sql = "INSERT INTO card (deck_id, question, answer, image_path) "
				+ "VALUES (?, ?, ?, ?) RETURNING card_id;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, card.getDeckId(), card.getQuestion(), card.getAnswer(), card.getImagePath());
		if (results.next()) {
			card.setCardId(results.getLong("card_id"));
		}
		return card;
		
	}

	// gets one card created by one user

	// currently not being used - when user selects a deck, all deck cards are being called 
	
	// using method getAllCardsFromDeck
	@Override
	public Card getOneUserCard(long cardId) { 
		Card card = new Card ();
		String sqlSelectUserCard = "SELECT card.card_id, card.deck_id, card.question, card.answer, card.image_path, deck.category from card "
				+ "WHERE card_id = ?;";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSelectUserCard, cardId);
		if (results.next()) {
			card = mapRowToCard(results);
		}
		return card;     
		
		
	}
	
	// users can view all cards in one deck 
	
	// is currently being used
	@Override
	public List<Card> getAllCardsFromDeck(long deckId) {
		List<Card> AllDeckCards = new ArrayList<Card>();
		String sqlSelectUserCards = "SELECT card_id, deck_id, question, answer, image_path, deck_id "
				+ "FROM card "				
				+ "WHERE deck_id=?;";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSelectUserCards, deckId);
		while (results.next()) {
			AllDeckCards.add(mapRowToCard(results));
		}
		return AllDeckCards;    
		
	}

	// editCard works but I think it should be void. 
	// Not sure how to make it work as a void
	
	@Override
	public Card editCard(Card card) {		
		
		String sql = "UPDATE card SET question = ?, answer = ?, image_path = ? "
				+ "WHERE card_id = ? RETURNING card_id;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, card.getQuestion(), card.getAnswer(), card.getImagePath(), card.getCardId());
		if (results.next()) {
			card.setCardId(results.getLong("card_id"));
		}
		return card;
	}

	@Override
	public void deleteCard(Long cardId) {		
		jdbcTemplate.update("DELETE FROM card WHERE card_id = ?;", cardId);
		
	}

	// gets all cards for all users
	// admins can access all user cards
	@Override
	public List<Card> getAllCards() { 
		List<Card> allCards = new ArrayList<Card>();
		String sqlAllCards = "SELECT card.card_id, card.deck_id, card.question, card.answer, card.image_path, deck.category "
				+ "FROM card join deck on card.deck_id = deck.deck_id "
				+ "JOIN subjects ON deck.subject_id = subjects.subject_id join users ON subjects.user_id = users.id "
				+ "WHERE users.role = 'user';";      
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlAllCards);
		while (results.next()) {
			allCards.add(mapRowToCard(results));
		}
		return allCards;    
		
	}
	
	
	// searches all cards for one user by question
	// case insensitive 
	
	@Override
	public List<Card> searchByText(long userId, String search) {
		List<Card> searchedCards = new ArrayList<Card>();		
		String sqlSearchedByQuestion = "SELECT card.card_id, card.deck_id, card.question, card.answer, card.image_path, deck.category from card "
				+ "JOIN deck on card.deck_id = deck.deck_id "
				+ "JOIN subjects ON deck.subject_id = subjects.subject_id "
				+ "WHERE subjects.user_id = ? AND card.question ILIKE ? OR card.answer ILIKE ? "
				+ "ORDER BY subjects.subject_name AND card.question;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSearchedByQuestion, userId, "%" + search + "%", "%" + search + "%");
		while (results.next()) {
			searchedCards.add(mapRowToCard(results));
		}
		return searchedCards;
	}

	
	private Card mapRowToCard(SqlRowSet results) {
		Card card = new Card ();
		card.setCardId(results.getLong("card_id"));
		card.setDeckId(results.getLong("deck_id"));
		card.setQuestion(results.getString("question"));
		card.setAnswer(results.getString("answer"));
		card.setImagePath(results.getString("image_path"));		
		return card;
	}

}

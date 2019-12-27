package com.techelevator.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.authentication.AuthProvider;

@Component
public class JdbcDeckDao implements DeckDao {
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
    private AuthProvider authProvider;
	
	@Autowired
	public JdbcDeckDao(BasicDataSource dataSource) {
		jdbcTemplate = new JdbcTemplate (dataSource);
	}
	
	//Users can view all decks for one subject
	@Override
	public List<Deck> getSubjectDecks(long subjectId) {
		List<Deck> userDecks = new ArrayList <Deck>();
		String sqlSelectUserDecks = "SELECT deck_id, subject_id, deck_name, is_public FROM deck WHERE subject_id = ?;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSelectUserDecks, subjectId);
		while (results.next()) {
			userDecks.add(mapRowToDeck(results));
		}
		return userDecks;
	}


	// Admins can view all decks from all users
	@Override
	public List<Deck> getAllDecks() {
		List<Deck> allDecks = new ArrayList <Deck>();
		String sqlSelectUserDecks = "SELECT deck_id, subject_id, deck_name, is_public "
				+ "FROM deck;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSelectUserDecks);
		while (results.next()) {
			allDecks.add(mapRowToDeck(results));
		}
		return allDecks;
	}
	@Override
	public List<Deck> searchByDeckByDeckName(long subjectId, String search) {
		List<Deck> searchedDecks = new ArrayList <Deck> ();
		String sqlSearchedDecks = "SELECT deck_id, subject_id, deck_name, is_public "
				+ "FROM deck "
				+ "WHERE subject_id = ? AND name = ?;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSearchedDecks, subjectId, "%" + search + "%");
		while (results.next()) {
			searchedDecks.add(mapRowToDeck(results));
		}		
		return searchedDecks;
	}

	
	// editDeck works but I think it should be void. 
	 	// Not sure how to make it work as a void
	@Override
	public Deck editDeck(Deck deck) {
		String sql = "UPDATE deck SET subject_id = ?, deck_name = ?, is_public = ? "
				+ "WHERE deck_id = ? RETURNING deck_id;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, deck.getSubjectId(), deck.getDeckName(), deck.isPublic(), deck.getDeckId());     
		if (results.next()) {
			deck.setDeckId(results.getLong("deck_id"));
		}
		return deck;
	}

	@Override
	public void deleteDeck(long deckId) {
		jdbcTemplate.update("DELETE FROM deck WHERE deck_id = ?;", deckId);
		
	}

	@Override
	public Deck createDeck(Deck deck) {
		String sql = "INSERT INTO deck (subject_id, deck_name, is_public) "
				+ "VALUES (?, ?, ?) RETURNING deck_id;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, deck.getSubjectId(), deck.getDeckName(), deck.isPublic());
		if (results.next()) {
			deck.setDeckId(results.getLong("deck_id"));
		}
		return deck;
	}
	
	@Override
	public List<Deck> getDecksByCardContents(String search){
		List<Deck> searchDecksByCards = new ArrayList<Deck>();
		String sqlString = "SELECT deck.deck_id, subject_id, deck_name, is_public "
				+ "FROM deck "
				+ "JOIN card on deck.deck_id = card.deck_id "
				+ "WHERE card.question ILIKE ? OR card.answer ILIKE ? "
				+ "GROUP BY deck.deck_id;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlString, "%" + search + "%", "%" + search + "%");
		while (results.next()) {
			searchDecksByCards.add(mapRowToDeck(results));
		} return searchDecksByCards;
	}
	

	private Deck mapRowToDeck(SqlRowSet results) {
		Deck deck = new Deck ();
		deck.setDeckId(results.getLong("deck_id"));
		deck.setSubjectId(results.getLong("subject_id"));
		deck.setDeckName(results.getString("deck_name"));
		deck.setPublic(results.getBoolean("is_public"));
		
		return deck;
	}

	@Override
	public List<Deck> getAllUserDecks() {
		List<Deck> allUserDecks = new ArrayList<Deck>();
		String sql = "select deck_id, subject_id, deck_name, is_public from deck where subject_id in (select subject_id from subjects where user_id = ?);";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, authProvider.getCurrentUser().getId());
		while (results.next()) {
			Deck deck = new Deck ();
			deck.setDeckId(results.getLong("deck_id"));
			deck.setSubjectId(results.getLong("subject_id"));
			deck.setDeckName(results.getString("deck_name"));
			deck.setPublic(results.getBoolean("is_public"));
			allUserDecks.add(deck);
		}
		return allUserDecks;
	}

	

}

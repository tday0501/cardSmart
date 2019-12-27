package com.techelevator.authentication;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.techelevator.model.Card;

public class CardTest {
	
	
	@Before
	public void before () {
		
	}
	
	
	@Test
	public void testCreateCard() {
		Card card = new Card ();
		
		card.setCardId(0);
		card.setDeckId(0);
		card.setQuestion("Test Question");
		card.setAnswer("Test Answer");
		card.setImagePath("Test Path");
		
	}
	
	@After
	public void after () {
		
	}

}

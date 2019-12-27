package com.techelevator.model;

public class Card {
	
	private long cardId;
	private long deckId;
	private String question;
	private String answer;
	private String imagePath;
	
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public long getCardId() {
		return cardId;
	}
	public void setCardId(long cardId) {
		this.cardId = cardId;
	}
	public long getDeckId() {
		return deckId;
	}
	public void setDeckId(long deckId) {
		this.deckId=deckId;
	}
	

}

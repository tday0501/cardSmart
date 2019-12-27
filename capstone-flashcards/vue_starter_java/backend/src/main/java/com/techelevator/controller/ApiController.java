package com.techelevator.controller;

import com.techelevator.authentication.AuthProvider;
import com.techelevator.authentication.UnauthorizedException;

import com.techelevator.model.Card;
import com.techelevator.model.CardDao;
import com.techelevator.model.Deck;
import com.techelevator.model.DeckDao;
import com.techelevator.model.Subject;
import com.techelevator.model.SubjectDao;
import com.techelevator.model.User;
import com.techelevator.model.UserDao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ApiController
 */
@CrossOrigin
@RestController
@RequestMapping("/api")
public class ApiController {

	@Autowired
	private AuthProvider authProvider;

	@Autowired
	private CardDao cardDao;

	@Autowired
	private DeckDao deckDao;

	@Autowired
	private SubjectDao subjectDao;

	@Autowired
	private UserDao userDao;

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public String authorizedOnly() throws UnauthorizedException {
		/*
		 * You can lock down which roles are allowed by checking if the current user has
		 * a role.
		 * 
		 * In this example, if the user does not have the admin role we send back an
		 * unauthorized error.
		 */
		if (!authProvider.userHasRole(new String[] { "admin" })) {
			throw new UnauthorizedException();
		}
		return "Success";
	}

	@GetMapping("/user")
	public User getUserId(@RequestParam String username) {
		return userDao.getUserByUsername(username);
	}

	// for admins only
	// admins can view all cards from all users
	@RequestMapping(path = "/allCards", method = RequestMethod.GET)
	public List<Card> getAllCards() {
		return cardDao.getAllCards();
	}

	// for users
	// when user selects one deck, returns all cards in deck
	@GetMapping("/cards")
	public List<Card> getDeckCards(@RequestParam long deckId) {
		List<Card> allDeckCards = new ArrayList<Card>();
		User currentUser = authProvider.getCurrentUser();
		if (currentUser != null) {
			allDeckCards = cardDao.getAllCardsFromDeck(deckId);
		}
		return allDeckCards;
	}

	@PostMapping("/cards")
	public Card addNewCard(@RequestBody Card card) {
		Card results = null;
		User currentUser = authProvider.getCurrentUser();
		if (currentUser != null) {
			results = cardDao.createCard(card);
		}

		return results;
	}

	// editCard works but I think it should be void.
	// Not sure how to make it work as a void
	@PostMapping("/updateCard")
	public Card editCard(@RequestBody Card card) {
		Card results = null;
		User currentUser = authProvider.getCurrentUser();
		if (currentUser != null) {
			results = cardDao.editCard(card);
		}

		return results;
	}
	

	// for admins only
	// admins can view all decks from all users
	@RequestMapping(path = "/allDecks", method = RequestMethod.GET)
	public List<Deck> getAllDecks() {
		return deckDao.getAllDecks();
	}

	// for users
	// when user selects one subject, returns all decks in subject
	@GetMapping("/decks")
	public List<Deck> getSubjectDecks(@RequestParam(required = false) Long subjectId,
			@RequestParam(required = false) String search) {
		List<Deck> displayedDecks = new ArrayList<Deck>();
		User currentUser = authProvider.getCurrentUser();
		if (currentUser != null) {
			if (search == null) {
				displayedDecks = deckDao.getSubjectDecks(subjectId);
			} else {
				displayedDecks = deckDao.getDecksByCardContents(search);
			}
		}
		return displayedDecks;
	}

	// for users
	// when user selects quiz, pulls up all user decks from all subject
	@GetMapping("/allUserDecks")
	public List<Deck> getAllUserDecks() {
		List<Deck> allUserDecks = new ArrayList<Deck>();
		User currentUser = authProvider.getCurrentUser();
		if (currentUser != null) {
			allUserDecks = deckDao.getAllUserDecks();
		}
		return allUserDecks;
	}

	
   // for users
   // when user selects decks for quiz, pulls up the decks the user has selected
   @GetMapping("/quizUserCards")
   public List <Card> getQuizUserCards (@RequestParam List<Integer> quizDecks) {
	   List <Card> tempList = new ArrayList <Card>();
	   List <Card> quizUserCards = new ArrayList <Card>();
	   User currentUser = authProvider.getCurrentUser();
	   if(currentUser != null) {
		   for (long deckId : quizDecks) {
			   tempList = cardDao.getAllCardsFromDeck(deckId);
			   for(Card card : tempList) {
				   quizUserCards.add(card);
			   }
		   }
	   }
	   return quizUserCards;	   
   }
	

	@PostMapping("/decks")
	public Deck addNewDeck(@RequestBody Deck deck) {
		Deck results = null;
		User currentUser = authProvider.getCurrentUser();
		if (currentUser != null) {
			results = deckDao.createDeck(deck);
		}
		return results;
	}

// editDeck works but I think it should be void. 
	// Not sure how to make it work as a void
	@PostMapping("/editDeck")
	public Deck editDeck(@RequestBody Deck deck) {
		Deck results = null;
		User currentUser = authProvider.getCurrentUser();
		if (currentUser != null) {
			results = deckDao.editDeck(deck);
		}
		return results;
	}

	@PostMapping("/deleteDeck")
	public void deleteDeck(@RequestBody Deck deck) {
		User currentUser = authProvider.getCurrentUser();
		if (currentUser != null) {
			deckDao.deleteDeck(deck.getDeckId());
		}
	}

	// for admins only
	// admins can view all subjects for all users
	@GetMapping(path = "/allSubjects")
	public List<Subject> getAllSubjects() {
		return subjectDao.getAllSubjects();
	}

	// for users
	// returns all subjects for one user
	@GetMapping("/subjects")
	public List<Subject> getUserSubjects() {
		List<Subject> allUserSubjects = new ArrayList<Subject>();

		User currentUser = authProvider.getCurrentUser();

		currentUser.getId();
		if (currentUser != null) {
			allUserSubjects = subjectDao.getUserSubjects();
		}
		return allUserSubjects;
	}
	
	@PostMapping("/deleteCard")
	public void deleteCard (@RequestBody Card card) {
		User currentUser = authProvider.getCurrentUser();
		System.out.println("****" + card.getCardId());
		if (currentUser != null) {
			cardDao.deleteCard(card.getCardId());
		}
	}

	// users can create a new subject
	@PostMapping("/subjects")
	public Subject addNewSubject(@RequestBody Subject subject) {
		Subject results = null;
		User currentUser = authProvider.getCurrentUser();
		if (currentUser != null) {
			results = subjectDao.createSubject(subject);
		}
		return results;
	}

	@PostMapping("/editSubject")
	public Subject editSubject(@RequestBody Subject subject) {
		Subject results = null;
		User currentUser = authProvider.getCurrentUser();
		if (currentUser != null) {
			results = subjectDao.updateSubject(subject);
		}
		return results;
	}

	@PostMapping("/deleteSubject")
	public void deleteSubject(@RequestBody Subject subject) {
		User currentUser = authProvider.getCurrentUser();
		if (currentUser != null) {
			subjectDao.deleteSubject(subject.getSubjectId());
		}
	}

}
/**
 * Baroness.java
 * 
 * @author Kodey Converse (krconverse@wpi.edu)
 */
package krconverse;

import krconverse.baroness.controller.DeckController;
import krconverse.baroness.controller.FoundationController;
import krconverse.baroness.move.DealCardsMove;
import krconverse.baroness.move.MoveCardToEmptyColumnMove;
import krconverse.baroness.move.PlayKingMove;
import krconverse.baroness.move.PlayPairMove;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import krconverse.baroness.controller.ColumnController;
import ks.common.controller.SolitaireMouseMotionAdapter;
import ks.common.games.Solitaire;
import ks.common.games.SolitaireUndoAdapter;
import ks.common.games.SolvableSolitaire;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.common.view.CardImages;
import ks.common.view.ColumnView;
import ks.common.view.DeckView;
import ks.common.view.IntegerView;
import ks.common.view.PileView;
import ks.launcher.Main;

/**
 * An implementation for the Baroness variant of Solitaire, also known as
 * Thirteens.
 * <p>
 * Layout: There is a deck, five columns and a foundation pile. All of the cards
 * start face down in the deck.
 * <p>
 * Objective: To move all cards into the foundation pile by pairing the top
 * cards on the columns which add to thirteen or which are Kings.
 * <p>
 * Play: Deal five cards from the deck onto the top of the five columns. Pairs
 * of cards adding up to thirteen or single Kings can be moved to the foundation
 * pile after all five cards have been dealt. When there are no more moves to be
 * made, five more cards can be dealt and the game play continues like this
 * until the deck has run out (there are only two cards in the last deal) and
 * there are no more moves to be made.
 * <p>
 * Score: A player's score is determined by how many cards are left in play.
 * This means that the goal of the game is to achieve a low or zero score.
 */
public class Baroness extends Solitaire implements SolvableSolitaire {

	Deck deck; // deck which cards are dealt from
	Column[] columns = new Column[5]; // columns which cards are dealt to and played from
	Pile foundation; // pile which pairs are moved to after being played

	DeckView deckView; // view for the deck
	ColumnView[] columnViews = new ColumnView[5]; // views for the columns
	PileView foundationView; // view for the foundation

	IntegerView scoreView; // view to show the score
	IntegerView cardsLeftView; // view to show the cards left counter

	/**
	 * Creates a new Baroness Solitaire game.
	 */
	public Baroness() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ks.common.games.Solitaire#initialize()
	 */
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		initializeModel(getSeed());
		initializeView();
		initializeControllers();
	}

	/**
	 * Initializes the model for the plugin.
	 * 
	 * @param seed
	 *            The seed to start the model with for random generation.
	 */
	private void initializeModel(int seed) {
		// add the deck to the model
		deck = new Deck("deck");
		deck.create(seed);
		model.addElement(deck);
		
		// add the columns to the model
		for (int i = 0; i < 5; i++) {
			columns[i] = new Column("col" + (i + 1));
			model.addElement(columns[i]);
		}

		// add the foundation to the model
		foundation = new Pile("foundation");
		model.addElement(foundation);
		
		// update the score and cards left
		this.updateScore(52);
		this.updateNumberCardsLeft(52);
	}

	/**
	 * Initializes the view for the plugin.
	 */
	private void initializeView() {
		CardImages ci = getCardImages(); // the images which the cards will be
											// represented with

		int cardSpacing = 20;
		int extraTableauSpacing = 20;

		// show the deck on the screen
		deckView = new DeckView(deck);
		deckView.setBounds(cardSpacing, cardSpacing, ci.getWidth(), ci.getHeight());
		addViewWidget(deckView);

		// show the columns on the screen
		int maximumColumnHeight = 13 * ci.getOverlap() + ci.getHeight(); // maximum
																			// height
																			// in
																			// Baroness
		for (int i = 0; i < 5; i++) {
			columnViews[i] = new ColumnView(columns[i]);
			columnViews[i].setBounds(extraTableauSpacing + cardSpacing + (i + 1) * (cardSpacing + ci.getWidth()), // x
					cardSpacing, // y
					ci.getWidth(), // width
					maximumColumnHeight // height
			);
			addViewWidget(columnViews[i]);
		}

		// show the foundation pile on the screen
		foundationView = new PileView(foundation);
		foundationView.setBounds(extraTableauSpacing * 2 + cardSpacing + 6 * (cardSpacing + ci.getWidth()), // x
				cardSpacing, // y
				ci.getWidth(), // width
				ci.getHeight()); // height
		addViewWidget(foundationView);

		// show the score on screen
		scoreView = new IntegerView(getScore());
		scoreView.setBounds(extraTableauSpacing * 2 + cardSpacing + 6 * (cardSpacing + ci.getWidth()),
				cardSpacing * 2 + ci.getHeight(), ci.getWidth(), 60);
		addViewWidget(scoreView);

		cardsLeftView = new IntegerView(getNumLeft());
		cardsLeftView.setBounds(cardSpacing, cardSpacing * 2 + ci.getHeight(), ci.getWidth(), 60);
		addViewWidget(cardsLeftView);
	}

	/**
	 * Initializes the controllers for the plugin.
	 */
	private void initializeControllers() {
		// set up the deck controllers
		deckView.setMouseAdapter(new DeckController(this, model));
		deckView.setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
		deckView.setUndoAdapter(new SolitaireUndoAdapter(this));
		
		// set up the column controllers
		for (int i = 0; i < 5; i++) {
			columnViews[i].setMouseAdapter(new ColumnController(this, model, columnViews[i]));
			columnViews[i].setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
			columnViews[i].setUndoAdapter(new SolitaireUndoAdapter(this));
		}
		
		// and lastly the foundation controllers
		foundationView.setMouseAdapter(new FoundationController(this, model));
		foundationView.setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
		foundationView.setUndoAdapter(new SolitaireUndoAdapter(this));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ks.common.games.Solitaire#getName()
	 */
	@Override
	public String getName() {
		return "Baroness";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ks.common.games.Solitaire#getVersion()
	 */
	@Override
	public String getVersion() {
		return "1.0";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ks.common.games.Solitaire#hasWon()
	 */
	@Override
	public boolean hasWon() {
		return getScoreValue() == 0;
	}

	/** 
	 * Launches the Baroness Solitaire variation.
	 */
	public static void main (String []args) {
		Main.generateWindow(new Baroness(), (int) System.currentTimeMillis());
	}

	/* (non-Javadoc)
	 * @see ks.common.games.SolvableSolitaire#availableMoves()
	 */
	@Override
	public Enumeration<Move> availableMoves() {
		ArrayList<Move> moves = new ArrayList<Move>();

		// try to play any Kings on the board
		for (int i = 0; i < 5; i++) {
			PlayKingMove move = new PlayKingMove(columns[i], foundation);
			if (move.valid(this)) {
				moves.add(move);
			}
		}

		// try to play any available pairs
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				PlayPairMove move = new PlayPairMove(columns[i], columns[j], foundation);
				if (move.valid(this)) {
					moves.add(move);
				}
			}
		}

		// try to move cards to empty columns
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				MoveCardToEmptyColumnMove move = new MoveCardToEmptyColumnMove(columns[i], columns[j]);
				if (move.valid(this)) {
					moves.add(move);
				}
			}
		}
		
		// try to deal cards
		DealCardsMove move = new DealCardsMove(deck, columns);
		if (move.valid(this)) {
			moves.add(move);
		}

		return Collections.enumeration(moves);
	}
}

/**
 * DealMove.java
 * 
 * @author Kodey Converse (kronverse@wpi.edu)
 */
package krconverse.baroness.move;

import ks.common.games.Solitaire;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.model.Move;

/**
 * The move which deals cards from the deck to the columns.
 */
public class DealCardsMove extends Move {
	
	Deck deck;
	Column[] columns;
	int cardsMoved;
	
	/**
	 * Creates a new move to deal cards from the deck to the columns.
	 * @param deck The deck to deal from.
	 * @param columns The columns to deal to, ordered from left to right.
	 */
	public DealCardsMove(Deck deck, Column[] columns) {
		this.deck = deck;
		this.columns = columns;
		this.cardsMoved = Math.min(columns.length, deck.count());
	}

	@Override
	public boolean doMove(Solitaire game) {
		for (int i = 0; i < cardsMoved; i++) {
			columns[columns.length - 1 - i].add(deck.get());
		}
		game.updateNumberCardsLeft(-1 * cardsMoved);
		return cardsMoved > 0;
	}

	@Override
	public boolean undo(Solitaire game) {
		for (int i = 0; i < cardsMoved; i++) {
			deck.add(columns[columns.length - cardsMoved + i].get());
		}
		game.updateNumberCardsLeft(cardsMoved);
		return true;
	}

	@Override
	public boolean valid(Solitaire game) {
		return !deck.empty();
	}
}

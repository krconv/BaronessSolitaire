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
	boolean isValid;
	
	/**
	 * Creates a new move to deal cards from the deck to the columns.
	 * @param deck The deck to deal from.
	 * @param columns The columns to deal to, ordered from left to right.
	 */
	public DealCardsMove(Deck deck, Column[] columns) {
		this.deck = deck;
		this.columns = columns;
		if (deck == null || deck.empty() || columns == null) {
			isValid = false;
		} else {
			this.cardsMoved = Math.min(columns.length, deck.count());
			isValid = true;
		}
	}

	@Override
	public boolean doMove(Solitaire game) {
		if (isValid) {
			for (int i = 0; i < cardsMoved; i++) {
				columns[columns.length - 1 - i].add(deck.get());
			}
			game.updateNumberCardsLeft(-1 * cardsMoved);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean undo(Solitaire game) {
		if (isValid) {
			for (int i = 0; i < cardsMoved; i++) {
				deck.add(columns[columns.length - cardsMoved + i].get());
			}
			game.updateNumberCardsLeft(cardsMoved);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean valid(Solitaire game) {
		return isValid;
	}
}

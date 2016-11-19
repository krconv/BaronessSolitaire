/**
 * PlayPairMove.java
 * 
 * @author Kodey Converse (krconverse@wpi.edu)
 */
package krconverse.baroness.move;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.model.Pile;

/**
 * A move which plays two cards which add up to thirteen.
 */
public class PlayPairMove extends Move {
	Column sourceColumn;
	Column targetColumn;
	Card cardBeingDragged;
	Pile foundation;
	boolean isValid;
	
	/**
	 * Creates a move which plays two cards which add up to thirteen.
	 * @param sourceColumn The column that the card is being dragged from.
	 * @param targetColumn The column that the card is being dragged to.
	 * @param cardBeingDragged The card that is being dragged.
	 * @param foundation The foundation to move the played pair to.
	 */
	public PlayPairMove(Column sourceColumn, Column targetColumn, Card cardBeingDragged, Pile foundation) {
		this.sourceColumn = sourceColumn;
		this.targetColumn = targetColumn;
		this.cardBeingDragged = cardBeingDragged;
		this.foundation = foundation;
		if (targetColumn == null || cardBeingDragged == null || targetColumn.empty()) { // no card to pair it with
			isValid = false;
		} else {
			isValid = targetColumn.peek().getRank() + cardBeingDragged.getRank() == 13;
		}
	}
	
	/**
	 * Creates a move which plays two cards which add up to thirteen.
	 * @param sourceColumn The column with the first card being played.
	 * @param targetColumn The column with the second card being played.
	 * @param foundation The foundation to move the played pair to.
	 */
	public PlayPairMove(Column sourceColumn, Column targetColumn, Pile foundation) {
		this.sourceColumn = sourceColumn;
		this.targetColumn = targetColumn;
		this.foundation = foundation;
		if (sourceColumn == null || sourceColumn.empty() || targetColumn == null || targetColumn.empty()) { // no cards to pair
			isValid = false;
		} else {
			isValid = sourceColumn.peek().getRank() + targetColumn.peek().getRank() == 13;
		}
	}

	/* (non-Javadoc)
	 * @see ks.common.model.Move#doMove(ks.common.games.Solitaire)
	 */
	@Override
	public boolean doMove(Solitaire game) {
		if (isValid) {
			foundation.add(targetColumn.get());
			foundation.add(cardBeingDragged == null ? sourceColumn.get() : cardBeingDragged);
			game.updateScore(-2);
			return true;
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see ks.common.model.Move#undo(ks.common.games.Solitaire)
	 */
	@Override
	public boolean undo(Solitaire game) {
		if (isValid) {
			sourceColumn.add(foundation.get());
			targetColumn.add(foundation.get());
			game.updateScore(2);
			return true;
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see ks.common.model.Move#valid(ks.common.games.Solitaire)
	 */
	@Override
	public boolean valid(Solitaire game) {
		return isValid;
	}
}

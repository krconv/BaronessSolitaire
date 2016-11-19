/**
 * MoveToEmptyColumnMove.java
 * 
 * @author Kodey Converse (kodey@krconv.com)
 */
package krconverse.baroness.move;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;

/**
 * A move which moves a card to an empty column.
 */
public class MoveCardToEmptyColumnMove extends Move {
	Column sourceColumn;
	Column targetColumn;
	Card cardBeingDragged;
	boolean isValid;
	
	/**
	 * Creates a move which moves a card to an empty column.
	 * @param sourceColumn The column that the card is being dragged from.
	 * @param targetColumn The column that the card is being dragged to.
	 * @param cardBeingDragged The card that is being dragged.
	 */
	public MoveCardToEmptyColumnMove(Column sourceColumn, Column targetColumn, Card cardBeingDragged) {
		this.sourceColumn = sourceColumn;
		this.targetColumn = targetColumn;
		this.cardBeingDragged = cardBeingDragged;
		if (sourceColumn == null || targetColumn == null || cardBeingDragged == null) {
			isValid = false;
		} else {
			isValid = targetColumn.empty() && !sourceColumn.empty();
		}
	}

	/**
	 * Creates a move which moves a card to an empty column.
	 * @param sourceColumn The column with the card being moved.
	 * @param targetColumn The column that the card is being moved to.
	 */
	public MoveCardToEmptyColumnMove(Column sourceColumn, Column targetColumn) {
		this.sourceColumn = sourceColumn;
		this.targetColumn = targetColumn;
		if (sourceColumn == null || targetColumn == null) {
			isValid = false;
		} else {
			isValid = targetColumn.empty() && sourceColumn.count() > 1;
		}
	}
	
	/* (non-Javadoc)
	 * @see ks.common.model.Move#doMove(ks.common.games.Solitaire)
	 */
	@Override
	public boolean doMove(Solitaire game) {
		if (isValid) {
			targetColumn.add(cardBeingDragged == null ? sourceColumn.get() : cardBeingDragged);
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
			sourceColumn.add(targetColumn.get());
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

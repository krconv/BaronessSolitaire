/**
 * PlayKingMove.java
 * 
 * @author Kodey Converse (kodey@krconv.com)
 */
package krconverse.baroness.move;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.model.Pile;

/**
 * A move which plays a King card.
 */
public class PlayKingMove extends Move {
	Column sourceColumn;
	Card card;
	Pile foundation;
	boolean isValid;
	
	/**
	 * Creates a move which plays a King to the foundation.
	 * @param sourceColumn The column which contains the King.
	 * @param foundation The foundation that the King is being moved to.
	 */
	public PlayKingMove(Column sourceColumn, Pile foundation) {
		this.sourceColumn = sourceColumn;
		this.foundation = foundation;
		
		if (sourceColumn == null || sourceColumn.empty()) { // no card to play
			isValid = false;
		} else {
			isValid = sourceColumn.peek().getRank() == 13;
		}
	}
	/**
	 * Creates a move which plays a King to the foundation.
	 * @param sourceColumn The column which contains the King.
	 * @param card The card being played.
	 * @param foundation The foundation that the King is being moved to.
	 */
	public PlayKingMove(Column sourceColumn, Card card, Pile foundation) {
		this.sourceColumn = sourceColumn;
		this.card = card;
		this.foundation = foundation;
		if (card == null) { // no card to play
			isValid = false;
		} else {
			isValid = card.getRank() == 13;
		}
	}

	/* (non-Javadoc)
	 * @see ks.common.model.Move#doMove(ks.common.games.Solitaire)
	 */
	@Override
	public boolean doMove(Solitaire game) {
		if (isValid) {
			foundation.add(card == null ? sourceColumn.get() : card);
			game.updateScore(-1);
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
			game.updateScore(1);
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

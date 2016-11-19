/**
 * PlayKingMove.java
 * 
 * @author Kodey Converse (kodey@krconv.com)
 */
package krconverse.baroness.move;

import ks.common.games.Solitaire;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.model.Pile;

/**
 * A move which plays a King card.
 */
public class PlayKingMove extends Move {
	Column sourceColumn;
	Pile foundation;
	boolean isValid;
	
	/**
	 * 
	 */
	public PlayKingMove(Column sourceColumn, Pile foundation) {
		this.sourceColumn = sourceColumn;
		this.foundation = foundation;
		
		if (sourceColumn.count() == 0) { // no card to play
			isValid = false;
		} else {
			isValid = sourceColumn.peek().getRank() == 13;
		}
	}

	/* (non-Javadoc)
	 * @see ks.common.model.Move#doMove(ks.common.games.Solitaire)
	 */
	@Override
	public boolean doMove(Solitaire game) {
		if (isValid) {
			foundation.add(sourceColumn.get());
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

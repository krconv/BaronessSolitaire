/**
 * FoundationController.java
 * 
 * @author Kodey Converse (krconverse@wpi.edu)
 */

package krconverse.baroness.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import krconverse.Baroness;
import krconverse.baroness.move.PlayKingMove;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Pile;
import ks.common.view.ColumnView;
import ks.common.view.Container;
import ks.common.view.Widget;

/**
 * Controller to handle the player's interactions with the foundation pile.
 */
public class FoundationController extends MouseAdapter {
	Baroness game;
	
	/**
	 * Creates a new controller to handle the player's interactions with the foundation pile.
	 * @param game The Baroness game.
	 */
	public FoundationController(Baroness game) {
		this.game = game;
	}


	/* (non-Javadoc)
	 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent event) {
		super.mouseReleased(event);

		// the container of the game
		Container container = game.getContainer();
		
		// see if there is a card being dropped here
		Widget draggedWidget = container.getActiveDraggingObject();
		if (draggedWidget == Container.getNothingBeingDragged()) {
			container.releaseDraggingObject();
			return;
		}
	
		// a card is being dropped here
		ColumnView sourceColumnView = (ColumnView) container.getDragSource();
		Column sourceColumn = (Column) sourceColumnView.getModelElement();
		Card cardBeingDragged = (Card) draggedWidget.getModelElement();
		
		// the user is playing a card
		PlayKingMove move = new PlayKingMove(sourceColumn, cardBeingDragged, (Pile) game.getModelElement("foundation"));
		// make the move
		if (move.doMove (game)) {
			game.pushMove(move);
		} else {
			// couldn't do the move
			sourceColumn.add(cardBeingDragged);
		}
		
		container.releaseDraggingObject();
		container.repaint();
	}

}

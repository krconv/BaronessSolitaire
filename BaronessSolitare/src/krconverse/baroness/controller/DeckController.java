/**
 * DeckController.java
 * 
 * @author Kodey Converse (krconverse@wpi.edu)
 */
package krconverse.baroness.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import krconverse.Baroness;
import krconverse.baroness.move.DealCardsMove;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.model.Move;
import ks.common.view.ColumnView;
import ks.common.view.Container;
import ks.common.view.Widget;

/**
 * Controller to handle the player's interactions with the deck.
 */
public class DeckController extends MouseAdapter {
	Baroness game;

	/**
	 * Creates a new controller to handle the player's interactions with the deck.
	 * @param game The game to act upon.
	 */
	public DeckController(Baroness game) {
		this.game = game;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent event) {
		super.mouseClicked(event);
		Deck deck = (Deck) game.getModelElement("deck");
		
		Column[] columns = new Column[5];
		for (int i = 0; i < 5; i++) {
			columns[i] = (Column) game.getModelElement("col" + (i + 1));
		}

		Move move = new DealCardsMove(deck, columns);
		if (move.doMove(game)) {
			// move was successful
			game.pushMove(move);
			game.refreshWidgets();
		}	
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
		
		// just cancel the drag
		sourceColumn.add(cardBeingDragged);
		
		container.releaseDraggingObject();
		container.repaint();
	}
}

package krconverse.baroness.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import krconverse.Baroness;
import krconverse.baroness.move.MoveCardToEmptyColumnMove;
import krconverse.baroness.move.PlayKingMove;
import krconverse.baroness.move.PlayPairMove;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.common.view.CardView;
import ks.common.view.ColumnView;
import ks.common.view.Container;
import ks.common.view.Widget;

/**
 * The controller to handle two cards being played.
 */
public class ColumnController extends MouseAdapter {
	Baroness game;
	ColumnView view;
	
	/**
	 * Creates a new controller to play a pair of cards.
	 * @param game The Baroness game.
	 * @param view The column view that this controller is registered for.
	 */
	public ColumnController(Baroness game, ColumnView view) {
		this.game = game;
		this.view = view;
	}

	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent event) {
		super.mousePressed(event);
		// the container of the game
		Container container = game.getContainer();
		Column column = (Column) view.getModelElement();
		
		if (column.empty()) {
			// no cards can be played
			return;
		}

		// pick up the card that the player is dragging
		CardView dragged = view.getCardViewForTopCard(event);
		if (dragged != null) {
			container.setActiveDraggingObject(dragged, event);
			container.setDragSource(view);
		}

		// redraw the column
		view.redraw();
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent event) {
		super.mouseReleased(event);
		// the container of the game
		Container container = game.getContainer();
		Column column = (Column) view.getModelElement();
		
		// see if there is a card being dropped here
		Widget draggedWidget = container.getActiveDraggingObject();
		if (draggedWidget == Container.getNothingBeingDragged()) {
			container.releaseDraggingObject();
			return;
		}
		
		// a card is being dropped here
		Widget sourceWidget = container.getDragSource();
		
		if (sourceWidget == view) {
			// the source and the destination are both on this column, so just move it back
			column.add((Card) draggedWidget.getModelElement());
		} else {
			// the card being dragged came from another column
			ColumnView sourceColumnView = (ColumnView) sourceWidget;
			Column sourceColumn = (Column) sourceColumnView.getModelElement();
			Card cardBeingDragged = (Card) draggedWidget.getModelElement();
			
			Move move;
			if (column.empty()) {
				// moving a card to the empty column
				move = new MoveCardToEmptyColumnMove(sourceColumn, column, cardBeingDragged);
			} else {
				// play the pair
				move = new PlayPairMove(sourceColumn, column, cardBeingDragged, (Pile) game.getModelElement("foundation"));
			}
			// make the move
			if (move.doMove (game)) {
				game.pushMove(move);
			} else {
				// couldn't do the move
				sourceColumn.add(cardBeingDragged);
			}
		}

		container.releaseDraggingObject();
		container.repaint();
	}


	/* (non-Javadoc)
	 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent event) {
		super.mouseClicked(event);
		// playing a King
		Column column = (Column) view.getModelElement();
		PlayKingMove move = new PlayKingMove(column, (Pile) game.getModelElement("foundation"));
		if (move.doMove (game)) {
			// played the pair successfully
			game.pushMove(move);
		}
	}
}

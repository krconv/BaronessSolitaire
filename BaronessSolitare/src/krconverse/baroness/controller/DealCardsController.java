/**
 * DealFiveCardsController.java
 * 
 * @author Kodey Converse (krconverse@wpi.edu)
 */
package krconverse.baroness.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import krconverse.Baroness;
import krconverse.baroness.move.DealCardsMove;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.model.Model;
import ks.common.model.Move;

/**
 * Controller to control the process of dealing five cards.
 */
public class DealCardsController extends MouseAdapter {
	Baroness game;
	Model model;
	
	/**
	 * Creates a new controller to deal five cards.
	 */
	public DealCardsController(Baroness game, Model model) {
		this.game = game;
		this.model = model;
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		super.mouseClicked(event);
		Deck deck = (Deck) model.getElement("deck");
		
		Column[] columns = new Column[5];
		for (int i = 0; i < 5; i++) {
			columns[i] = (Column) model.getElement("col" + (i + 1));
		}

		Move move = new DealCardsMove(deck, columns);
		if (move.doMove(game)) {
			// move was successful
			game.pushMove(move);
			game.refreshWidgets();
		}	
	}
	
	

}

/**
 * PlayPairMoveTest.java
 * 
 * @author Kodey Converse (krconverse@wpi.edu)
 */
package krconverse.baroness.move;

import static org.junit.Assert.*;

import org.junit.Test;

import krconverse.Baroness;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.model.Pile;
import ks.launcher.Main;

/**
 * Tests that the PlayPairMove is working properly.
 */
public class PlayPairMoveTest {

	@Test
	public void test() {
		// initialize the game
		Baroness game = new Baroness();
		Main.generateWindow(game, Deck.OrderBySuit);
		Deck deck = (Deck) game.getModelElement("deck");
		Pile foundation = (Pile) game.getModelElement("foundation");
		Column[] columns = new Column[5];
		for (int i = 0; i < 5; i++) {
			columns[i] = (Column) game.getModelElement("col" + (i + 1));
		}
		
		// make sure that trying to play pairs on an empty board is invalid
		PlayPairMove move = new PlayPairMove(columns[0], columns[1], deck.peek(), foundation);
		assertFalse(move.valid(game));
		assertFalse(move.doMove(game));
		assertFalse(move.undo(game));
		
		// need to deal cards so that we can pair them
		DealCardsMove[] dealMoves = new DealCardsMove[2];
		for (int i = 0; i < 2; i++) {
			dealMoves[i] = new DealCardsMove(deck, columns);
			dealMoves[i].doMove(game);
		}
		
		// now play the 5 and the 8 cards, a valid move
		move = new PlayPairMove(columns[1], columns[4], columns[1].get(), foundation);
		assertTrue(move.valid(game));
		assertTrue(move.doMove(game));
		// make sure that the two cards got moved to the foundation successfully
		assertEquals(50, game.getScoreValue());
		assertEquals(2, foundation.count());
		assertEquals(5, foundation.peek().getRank());
		assertEquals(8, foundation.peek(0).getRank());
		// make sure the tableau is correct
		assertEquals(2, columns[0].count());
		assertEquals(4, columns[0].peek().getRank());
		assertEquals(1, columns[1].count());
		assertEquals(10, columns[1].peek().getRank());
		assertEquals(2, columns[2].count());
		assertEquals(6, columns[2].peek().getRank());
		assertEquals(2, columns[3].count());
		assertEquals(7, columns[3].peek().getRank());
		assertEquals(1, columns[4].count());
		assertEquals(13, columns[4].peek().getRank());
		// make sure the undo works
		assertTrue(move.undo(game));
		assertEquals(52, game.getScoreValue());
		assertEquals(0, foundation.count());
		// make sure the tableau is correct
		assertEquals(2, columns[0].count());
		assertEquals(4, columns[0].peek().getRank());
		assertEquals(2, columns[1].count());
		assertEquals(5, columns[1].peek().getRank());
		assertEquals(2, columns[2].count());
		assertEquals(6, columns[2].peek().getRank());
		assertEquals(2, columns[3].count());
		assertEquals(7, columns[3].peek().getRank());
		assertEquals(2, columns[4].count());
		assertEquals(8, columns[4].peek().getRank());

		// try pairing 4 and 5, and invalid move
		move = new PlayPairMove(columns[0], columns[1], columns[0].peek(), foundation);
		assertFalse(move.valid(game));
		assertFalse(move.doMove(game));
		// make sure the move didn't do anything
		assertEquals(52, game.getScoreValue());
		assertEquals(0, foundation.count());
		assertEquals(2, columns[0].count());
		assertEquals(4, columns[0].peek().getRank());
		assertEquals(2, columns[1].count());
		assertEquals(5, columns[1].peek().getRank());
		assertEquals(2, columns[2].count());
		assertEquals(6, columns[2].peek().getRank());
		assertEquals(2, columns[3].count());
		assertEquals(7, columns[3].peek().getRank());
		assertEquals(2, columns[4].count());
		assertEquals(8, columns[4].peek().getRank());
		// make sure undo does nothing
		assertFalse(move.undo(game));
		assertEquals(52, game.getScoreValue());
		assertEquals(0, foundation.count());
		assertEquals(2, columns[0].count());
		assertEquals(4, columns[0].peek().getRank());
		assertEquals(2, columns[1].count());
		assertEquals(5, columns[1].peek().getRank());
		assertEquals(2, columns[2].count());
		assertEquals(6, columns[2].peek().getRank());
		assertEquals(2, columns[3].count());
		assertEquals(7, columns[3].peek().getRank());
		assertEquals(2, columns[4].count());
		assertEquals(8, columns[4].peek().getRank());
	}
}

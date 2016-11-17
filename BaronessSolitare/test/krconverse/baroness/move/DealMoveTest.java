/**
 * DealMoveTest.java
 * 
 * @author Kodey Converse (kodey@krconv.com)
 */
package krconverse.baroness.move;

import static org.junit.Assert.*;

import org.junit.Test;

import krconverse.Baroness;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.launcher.Main;
/**
 * 
 */
public class DealMoveTest {

	@Test
	public void test() {
		Baroness game = new Baroness();
		Main.generateWindow(game, Deck.OrderBySuit);
		Deck deck = (Deck) game.getModelElement("deck");
		
		Column[] columns = new Column[5];
		for (int i = 0; i < 5; i++) {
			columns[i] = (Column) game.getModelElement("col" + (i + 1));
		}
		
		DealMove move = new DealMove(deck, columns);
		// make sure that the move hasn't done anything yet
		assertEquals(52, deck.count());
		for (int i = 0; i < 5; i++) {
			assertTrue(columns[i].empty());
		}
		// the move should be valid
		assertTrue(move.valid(game));
		
		// test making the move
		move.doMove(game);
		assertEquals(47, deck.count());
		assertEquals(47, game.getNumLeft().getValue());
		for (int i = 0; i < 5; i++) { // make sure the cards were dealt right to left
			assertEquals(13 - 4 + i, columns[i].peek().getRank());
		}
		
		// test undoing the move
		move.undo(game);
		assertEquals(52, deck.count());
		assertEquals(52, game.getNumLeft().getValue());
		for (int i = 0; i < 5; i++) {
			assertTrue(columns[i].empty());
		}
		
		DealMove[] moves = new DealMove[11]; // 11 deals to deal the whole deck
		for (int i = 0; i < 11; i++) {
			moves[i] = new DealMove(deck, columns);
			moves[i].doMove(game);
		}
		
		// make sure the last two were dealt from right to left
		assertTrue(deck.empty());
		assertEquals(0, game.getNumLeft().getValue());
		assertEquals(10, columns[0].count());
		assertEquals(3, columns[0].peek().getRank());
		assertEquals(10, columns[1].count());
		assertEquals(4, columns[1].peek().getRank());
		assertEquals(10, columns[2].count());
		assertEquals(5, columns[2].peek().getRank());
		assertEquals(11, columns[3].count());
		assertEquals(1, columns[3].peek().getRank());
		assertEquals(11, columns[4].count());
		assertEquals(2, columns[4].peek().getRank());
		
		// a new move on the deck would be invalid
		move = new DealMove(deck, columns);
		assertFalse(move.valid(game));
		assertFalse(move.doMove(game));
		assertTrue(deck.empty());
		assertEquals(0, game.getNumLeft().getValue());
		assertEquals(10, columns[0].count());
		assertEquals(3, columns[0].peek().getRank());
		assertEquals(10, columns[1].count());
		assertEquals(4, columns[1].peek().getRank());
		assertEquals(10, columns[2].count());
		assertEquals(5, columns[2].peek().getRank());
		assertEquals(11, columns[3].count());
		assertEquals(1, columns[3].peek().getRank());
		assertEquals(11, columns[4].count());
		assertEquals(2, columns[4].peek().getRank());
		
		
		// make sure undoing the last move removes the last two cards dealt
		moves[10].undo(game);
		assertEquals(2, deck.count());
		assertEquals(2, game.getNumLeft().getValue());
		assertEquals(10, columns[0].count());
		assertEquals(3, columns[0].peek().getRank());
		assertEquals(10, columns[1].count());
		assertEquals(4, columns[1].peek().getRank());
		assertEquals(10, columns[2].count());
		assertEquals(5, columns[2].peek().getRank());
		assertEquals(10, columns[3].count());
		assertEquals(6, columns[3].peek().getRank());
		assertEquals(10, columns[4].count());
		assertEquals(7, columns[4].peek().getRank());
		
	}

}

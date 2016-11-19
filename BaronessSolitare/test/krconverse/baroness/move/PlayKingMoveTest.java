/**
 * PlayKingMoveTest.java
 * 
 * @author Kodey Converse (krconverse@wpi.edu)
 */
package krconverse.baroness.move;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import krconverse.Baroness;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.model.Pile;
import ks.launcher.Main;

/**
 * Test class for {@link krconverse.baroness.move.PlayKingMove}
 */
public class PlayKingMoveTest extends TestCase {
	Baroness game;
	Deck deck;
	Column[] columns;
	Pile foundation;
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp() throws Exception {
		game = new Baroness();
		Main.generateWindow(game, Deck.OrderBySuit);
		deck = (Deck) game.getModelElement("deck");
		
		columns = new Column[5];
		for (int i = 0; i < 5; i++) {
			columns[i] = (Column) game.getModelElement("col" + (i + 1));
		}
		
		foundation = (Pile) game.getModelElement("foundation");
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@After
	public void tearDown() throws Exception {
		// check that the deck is correct
		assertEquals(52, deck.count());
		for (int i = 0; i < 52; i++) {
			assertEquals(i % 13 + 1, deck.peek(i).getRank());
		}
		// check that the columns are empty
		for (int i = 0; i < 5; i++) {
			assertTrue(columns[i].empty());
		}
		// check that the foundation is empty
		assertTrue(foundation.empty());
		
		// and check the score and cards left counters
		assertEquals(52, game.getScoreValue());
		assertEquals(52, game.getNumLeft().getValue());
		game.dispose();
	}

	/**
	 * Test method for {@link krconverse.baroness.move.PlayKingMove#doMove(ks.common.games.Solitaire)}.
	 */
	@Test
	public void testDoMove() {
		DealCardsMove dealMove = new DealCardsMove(deck, columns);
		
		// test that playing an empty column doesn't do anything
		PlayKingMove move = new PlayKingMove(columns[0], foundation);
		assertFalse(move.doMove(game));
		assertTrue(columns[0].empty());
		assertTrue(foundation.empty());
		
		// test that doing a move on a non-king does nothing
		dealMove.doMove(game);
		move = new PlayKingMove(columns[0], foundation);
		assertFalse(move.doMove(game));
		assertEquals(1, columns[0].count());
		assertEquals(9, columns[0].peek().getRank());
		assertTrue(foundation.empty());
		assertEquals(52, game.getScoreValue());
		
		// test that making a valid move moves the king to foundation
		move = new PlayKingMove(columns[4], foundation);
		assertTrue(move.doMove(game));
		assertTrue(columns[4].empty());
		assertEquals(13, foundation.peek().getRank());
		assertEquals(51, game.getScoreValue());
		
		// undo everything
		move.undo(game);
		dealMove.undo(game);
	}

	/**
	 * Test method for {@link krconverse.baroness.move.PlayKingMove#undo(ks.common.games.Solitaire)}.
	 */
	@Test
	public void testUndo() {
		DealCardsMove dealMove = new DealCardsMove(deck, columns);
		
		// try undoing it if the move is invalid
		PlayKingMove move = new PlayKingMove(columns[0], foundation);
		assertFalse(move.undo(game));
		assertTrue(columns[0].empty());
		assertEquals(52, game.getScoreValue());
		
		// try undoing it if the move has been made and is valid
		dealMove.doMove(game);
		move = new PlayKingMove(columns[4], foundation);
		move.doMove(game);
		assertTrue(move.undo(game));
		assertEquals(13, columns[4].peek().getRank());
		assertEquals(1, columns[4].count());
		assertTrue(foundation.empty());
		assertEquals(52, game.getScoreValue());
		
		// try undoing it if another move has already been made, but the new one is invalid
		move.doMove(game);
		PlayKingMove move2 = new PlayKingMove(columns[0], foundation);
		assertFalse(move2.undo(game));
		assertEquals(9, columns[0].peek().getRank());
		assertEquals(1, columns[0].count());
		assertTrue(columns[4].empty());
		assertEquals(13, foundation.peek().getRank());
		assertEquals(51, game.getScoreValue());
		
		// revert moves
		move.undo(game);
		dealMove.undo(game);
	}

	/**
	 * Test method for {@link krconverse.baroness.move.PlayKingMove#valid(ks.common.games.Solitaire)}.
	 */
	@Test
	public void testValid() {
		DealCardsMove dealMove = new DealCardsMove(deck, columns);
		
		// make sure the move isn't valid on an empty column
		PlayKingMove move = new PlayKingMove(columns[0], foundation);
		assertFalse(move.valid(game));
		
		// and with a column that isn't a king
		dealMove.doMove(game);
		move = new PlayKingMove(columns[0], foundation);
		assertFalse(move.valid(game));
		
		// make sure it is valid with a king
		move = new PlayKingMove(columns[4], foundation);
		assertTrue(move.valid(game));
		
		dealMove.undo(game);
	}

	/**
	 * Test method for {@link krconverse.baroness.move.PlayKingMove#PlayKingMove(ks.common.model.Column, ks.common.model.Pile)}
	 */
	@Test
	public void testPlayKingMoveColumnPile() {
		DealCardsMove dealMove = new DealCardsMove(deck, columns);
		
		// test with normal input, with and without empty columns
		new PlayKingMove(columns[0], foundation);
		dealMove.doMove(game);
		new PlayKingMove(columns[0], foundation);
		dealMove.undo(game);
		
		// test with bad input
		new PlayKingMove(null, foundation);
		new PlayKingMove(columns[0], null);
		new PlayKingMove(null, null);
	}

	/**
	 * Test method for {@link krconverse.baroness.move.PlayKingMove#PlayKingMove(ks.common.model.Column, ks.common.model.Card, ks.common.model.Pile)}
	 */
	@Test
	public void testPlayKingMoveColumnCardPile() {
		DealCardsMove dealMove = new DealCardsMove(deck, columns);
		
		// test with normal input, with and without empty columns
		new PlayKingMove(columns[0], columns[0].peek(), foundation);
		dealMove.doMove(game);
		new PlayKingMove(columns[0], columns[0].peek(), foundation);
		dealMove.undo(game);
		
		// test with bad input
		new PlayKingMove(null, columns[0].peek(), foundation);
		new PlayKingMove(columns[0], null, foundation);
		new PlayKingMove(columns[0], columns[0].peek(), null);
		new PlayKingMove(null, null, foundation);
		new PlayKingMove(null, columns[0].peek(), null);
		new PlayKingMove(columns[0], null, null);
		new PlayKingMove(null, null, null);
	}
}

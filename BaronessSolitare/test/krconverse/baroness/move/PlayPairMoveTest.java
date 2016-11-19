/**
 * PlayPairMoveTest.java
 * 
 * @author Kodey Converse (krconverse@wpi.edu)
 */
package krconverse.baroness.move;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
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
	Baroness game;
	Deck deck;
	Column[] columns;
	Pile foundation;
	

	/**
	 * @throws Exception
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

	/**
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		game.dispose();
	}

	/**
	 * Test method for {@link krconverse.baroness.move.PlayPairMove#doMove(ks.common.games.Solitaire)}
	 */
	@Test
	public void testDoMove() {
		DealCardsMove dealMove = new DealCardsMove(deck, columns);
		
		// test that playing on empty columns doesn't do anything
		PlayPairMove move = new PlayPairMove(columns[0], columns[1], foundation);
		assertFalse(move.doMove(game));
		assertTrue(columns[0].empty());
		assertTrue(columns[1].empty());
		assertTrue(foundation.empty());
		assertEquals(52, game.getScoreValue());
		
		dealMove.doMove(game);
		PlayKingMove kingMove = new PlayKingMove(columns[4], foundation); 
		kingMove.doMove(game);
		
		move = new PlayPairMove(columns[0], columns[4], foundation);
		assertFalse(move.doMove(game));
		assertEquals(1, columns[0].count());
		assertTrue(columns[4].empty());
		assertEquals(1, foundation.count());
		assertEquals(13, foundation.peek().getRank());
		assertEquals(51, game.getScoreValue());
		
		move = new PlayPairMove(columns[4], columns[0], foundation);
		assertFalse(move.doMove(game));
		assertEquals(1, columns[0].count());
		assertTrue(columns[4].empty());
		assertEquals(1, foundation.count());
		assertEquals(13, foundation.peek().getRank());
		assertEquals(51, game.getScoreValue());
		
		kingMove.undo(game);
		

		DealCardsMove dealMove2 = new DealCardsMove(deck, columns);
		dealMove2.doMove(game);
		
		// test that doing a move on a non-playable pair does nothing
		move = new PlayPairMove(columns[1], columns[3], foundation);
		assertFalse(move.doMove(game));
		assertEquals(2, columns[1].count());
		assertEquals(5, columns[1].peek().getRank());
		assertEquals(2, columns[3].count());
		assertEquals(7, columns[3].peek().getRank());
		assertEquals(0, foundation.count());
		assertEquals(52, game.getScoreValue());
				
		// test that making a valid move moves the pair to foundation
		move = new PlayPairMove(columns[1], columns[4], foundation);
		assertTrue(move.doMove(game));
		assertEquals(1, columns[1].count());
		assertEquals(10, columns[1].peek().getRank());
		assertEquals(1, columns[4].count());
		assertEquals(13, columns[4].peek().getRank());
		assertEquals(2, foundation.count());
		assertEquals(50, game.getScoreValue());
		assertEquals(5, foundation.peek().getRank());
		assertEquals(8, foundation.peek(0).getRank());
		
		// undo everything
		move.undo(game);
		dealMove2.undo(game);
		dealMove.undo(game);
	}

	/**
	 * Test method for {@link krconverse.baroness.move.PlayPairMove#undo(ks.common.games.Solitaire)}
	 */
	@Test
	public void testUndo() {
		DealCardsMove dealMove = new DealCardsMove(deck, columns);
		
		// test that undoing an invalid move does nothing
		PlayPairMove move = new PlayPairMove(columns[0], columns[1], foundation);
		assertFalse(move.undo(game));
		assertTrue(columns[0].empty());
		assertTrue(columns[1].empty());
		assertTrue(foundation.empty());
		assertEquals(52, game.getScoreValue());
		
		dealMove.doMove(game);
		PlayKingMove kingMove = new PlayKingMove(columns[4], foundation); 
		kingMove.doMove(game);
		
		move = new PlayPairMove(columns[0], columns[4], foundation);
		assertFalse(move.undo(game));
		assertEquals(1, columns[0].count());
		assertTrue(columns[4].empty());
		assertEquals(1, foundation.count());
		assertEquals(13, foundation.peek().getRank());
		assertEquals(51, game.getScoreValue());
		
		move = new PlayPairMove(columns[4], columns[0], foundation);
		assertFalse(move.undo(game));
		assertEquals(1, columns[0].count());
		assertTrue(columns[4].empty());
		assertEquals(1, foundation.count());
		assertEquals(13, foundation.peek().getRank());
		assertEquals(51, game.getScoreValue());
		
		kingMove.undo(game);

		DealCardsMove dealMove2 = new DealCardsMove(deck, columns);
		dealMove2.doMove(game);
		
		// test that undoing a move on a non-playable pair does nothing
		move = new PlayPairMove(columns[1], columns[3], foundation);
		assertFalse(move.undo(game));
		assertEquals(2, columns[1].count());
		assertEquals(5, columns[1].peek().getRank());
		assertEquals(2, columns[3].count());
		assertEquals(7, columns[3].peek().getRank());
		assertEquals(0, foundation.count());
		assertEquals(52, game.getScoreValue());
				
		// test that undoing is successful for a valid move
		move = new PlayPairMove(columns[1], columns[4], foundation);
		move.doMove(game);
		assertTrue(move.undo(game));
		assertEquals(2, columns[1].count());
		assertEquals(5, columns[1].peek().getRank());
		assertEquals(2, columns[4].count());
		assertEquals(8, columns[4].peek().getRank());
		assertEquals(0, foundation.count());
		assertEquals(52, game.getScoreValue());
		
		// undo everything
		dealMove2.undo(game);
		dealMove.undo(game);
	}

	/**
	 * Test method for {@link krconverse.baroness.move.PlayPairMove#valid(ks.common.games.Solitaire)}
	 */
	@Test
	public void testValid() {
		DealCardsMove dealMove = new DealCardsMove(deck, columns);
		
		// test that the move is invalid on empty columns
		PlayPairMove move = new PlayPairMove(columns[0], columns[1], foundation);
		assertFalse(move.valid(game));
		
		dealMove.doMove(game);
		PlayKingMove kingMove = new PlayKingMove(columns[4], foundation); 
		kingMove.doMove(game);
		
		move = new PlayPairMove(columns[0], columns[4], foundation);
		assertFalse(move.valid(game));
		
		move = new PlayPairMove(columns[4], columns[0], foundation);
		assertFalse(move.valid(game));
		
		kingMove.undo(game);

		DealCardsMove dealMove2 = new DealCardsMove(deck, columns);
		dealMove2.doMove(game);
		
		// test that doing a move on a non-playable pair does nothing
		move = new PlayPairMove(columns[1], columns[3], foundation);
		assertFalse(move.valid(game));
						
		// test that making a valid move moves the pair to foundation
		move = new PlayPairMove(columns[1], columns[4], foundation);
		assertTrue(move.valid(game));
		
		// undo everything
		dealMove2.undo(game);
		dealMove.undo(game);
	}

	/**
	 * Test method for {@link krconverse.baroness.move.PlayPairMove#PlayPairMove(ks.common.model.Column, ks.common.model.Column, ks.common.model.Pile)}
	 */
	@Test
	public void testPlayPairMoveColumnColumnPile() {
		DealCardsMove dealMove = new DealCardsMove(deck, columns);
		
		// test with normal input, with and without empty columns
		new PlayPairMove(columns[0], columns[1], foundation);
		dealMove.doMove(game);
		new PlayPairMove(columns[0], columns[1], foundation);
		dealMove.undo(game);
		
		// test with bad input
		new PlayPairMove(null, columns[1], foundation);
		new PlayPairMove(columns[0], null, foundation);
		new PlayPairMove(columns[0], columns[1], null);
		new PlayPairMove(null, null, foundation);
		new PlayPairMove(null, columns[1], null);
		new PlayPairMove(columns[0], null, null);
		new PlayPairMove(null, null, null);
	}

	/**
	 * Test method for {@link krconverse.baroness.move.PlayPairMove#PlayPairMove(ks.common.model.Column, ks.common.model.Column, ks.common.model.Card, ks.common.model.Pile)}
	 */
	@Test
	public void testPlayKingMoveColumnColumnCardPile() {
		DealCardsMove dealMove = new DealCardsMove(deck, columns);
		
		// test with normal input, with and without empty columns
		new PlayPairMove(columns[0], columns[1], columns[0].peek(), foundation);
		dealMove.doMove(game);
		new PlayPairMove(columns[0], columns[1], columns[0].peek(), foundation);
		dealMove.undo(game);
		
		// test with bad input
		new PlayPairMove(null, columns[1], columns[0].peek(), foundation);
		new PlayPairMove(columns[0], null, columns[0].peek(), foundation);
		new PlayPairMove(columns[0], columns[1], null, foundation);
		new PlayPairMove(columns[0], columns[1], columns[0].peek(), null);
		new PlayPairMove(null, null, columns[0].peek(), foundation);
		new PlayPairMove(null, columns[1], null, foundation);
		new PlayPairMove(null, columns[1], columns[0].peek(), null);
		new PlayPairMove(columns[0], null, null, foundation);
		new PlayPairMove(columns[0], null, columns[0].peek(), null);
		new PlayPairMove(columns[0], columns[1], null, null);
		new PlayPairMove(null, null, null, null);
	}
}

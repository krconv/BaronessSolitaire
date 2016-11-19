/**
 * MoveCardToEmptyColumnMoveTest.java
 * 
 * @author Kodey Converse (kodey@krconv.com)
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
 * Test class for {@link krconverse.baroness.move.MoveCardToEmptyColumnMove}
 */
public class MoveCardToEmptyColumnMoveTest {
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
	 * Test method for {@link krconverse.baroness.move.MoveCardToEmptyColumnMove#doMove(ks.common.games.Solitaire)}.
	 */
	@Test
	public void testDoMove() {
		DealCardsMove dealMove = new DealCardsMove(deck, columns);
		
		// test moving from an empty column to an empty column
		MoveCardToEmptyColumnMove move = new MoveCardToEmptyColumnMove(columns[0], columns[1]);
		assertFalse(move.doMove(game));
		
		// test moving from a non-empty column to a non-empty column
		dealMove.doMove(game);
		move = new MoveCardToEmptyColumnMove(columns[0], columns[1]);
		assertFalse(move.doMove(game));
		assertEquals(1, columns[0].count());
		assertEquals(9, columns[0].peek().getRank());
		assertEquals(1, columns[4].count());
		assertEquals(13, columns[4].peek().getRank());
		
		// test moving from a column of 1 card to an empty one
		PlayKingMove kingMove = new PlayKingMove(columns[4], foundation);
		kingMove.doMove(game);
		move = new MoveCardToEmptyColumnMove(columns[0], columns[4]);
		assertFalse(move.doMove(game));
		assertEquals(1, columns[0].count());
		assertEquals(9, columns[0].peek().getRank());
		assertTrue(columns[4].empty());
		
		// test moving a stacked column to an empty one
		DealCardsMove dealMove2 = new DealCardsMove(deck, columns);
		dealMove2.doMove(game);
		PlayPairMove pairMove = new PlayPairMove(columns[2], columns[3], foundation);
		pairMove.doMove(game);
		PlayPairMove pairMove2 = new PlayPairMove(columns[1], columns[4], foundation);
		pairMove2.doMove(game);
		// now column 4 is empty and 0 has two cards
		move = new MoveCardToEmptyColumnMove(columns[0], columns[4]);
		assertTrue(move.doMove(game));
		assertEquals(1, columns[0].count());
		assertEquals(9, columns[0].peek().getRank());
		assertEquals(1, columns[4].count());
		assertEquals(4, columns[4].peek().getRank());
		
		// undo everything
		move.undo(game);
		pairMove2.undo(game);
		pairMove.undo(game);
		dealMove2.undo(game);
		kingMove.undo(game);
		dealMove.undo(game);
	}

	/**
	 * Test method for {@link krconverse.baroness.move.MoveCardToEmptyColumnMove#undo(ks.common.games.Solitaire)}.
	 */
	@Test
	public void testUndo() {
		DealCardsMove dealMove = new DealCardsMove(deck, columns);
		
		// test undoing from an empty column to an empty column
		MoveCardToEmptyColumnMove move = new MoveCardToEmptyColumnMove(columns[0], columns[1]);
		assertFalse(move.undo(game));
		
		// test undoing from a non-empty column to a non-empty column
		dealMove.doMove(game);
		move = new MoveCardToEmptyColumnMove(columns[0], columns[1]);
		assertFalse(move.undo(game));
		assertEquals(1, columns[0].count());
		assertEquals(9, columns[0].peek().getRank());
		assertEquals(1, columns[4].count());
		assertEquals(13, columns[4].peek().getRank());
		
		// test undoing from a column of 1 card to an empty one
		PlayKingMove kingMove = new PlayKingMove(columns[4], foundation);
		kingMove.doMove(game);
		move = new MoveCardToEmptyColumnMove(columns[0], columns[4]);
		assertFalse(move.undo(game));
		assertEquals(1, columns[0].count());
		assertEquals(9, columns[0].peek().getRank());
		assertTrue(columns[4].empty());
		
		// test undoing a valid move
		DealCardsMove dealMove2 = new DealCardsMove(deck, columns);
		dealMove2.doMove(game);
		PlayPairMove pairMove = new PlayPairMove(columns[2], columns[3], foundation);
		pairMove.doMove(game);
		PlayPairMove pairMove2 = new PlayPairMove(columns[1], columns[4], foundation);
		pairMove2.doMove(game);
		// now column 4 is empty and 0 has two cards
		move = new MoveCardToEmptyColumnMove(columns[0], columns[4]);
		move.doMove(game);
		assertTrue(move.undo(game));
		assertEquals(2, columns[0].count());
		assertEquals(4, columns[0].peek().getRank());
		assertTrue(columns[4].empty());
		
		// undo everything
		pairMove2.undo(game);
		pairMove.undo(game);
		dealMove2.undo(game);
		kingMove.undo(game);
		dealMove.undo(game);
	}

	/**
	 * Test method for {@link krconverse.baroness.move.MoveCardToEmptyColumnMove#valid(ks.common.games.Solitaire)}.
	 */
	@Test
	public void testValid() {
		DealCardsMove dealMove = new DealCardsMove(deck, columns);
		
		// test invalid moves
		MoveCardToEmptyColumnMove move = new MoveCardToEmptyColumnMove(columns[0], columns[1]);
		assertFalse(move.valid(game));
		
		// test a move from a non-empty column to a non-empty column
		dealMove.doMove(game);
		move = new MoveCardToEmptyColumnMove(columns[0], columns[1]);
		assertFalse(move.valid(game));
		
		// test a move from a column of 1 card to an empty one
		PlayKingMove kingMove = new PlayKingMove(columns[4], foundation);
		kingMove.doMove(game);
		move = new MoveCardToEmptyColumnMove(columns[0], columns[4]);
		assertFalse(move.valid(game));
		
		// test a valid move
		DealCardsMove dealMove2 = new DealCardsMove(deck, columns);
		dealMove2.doMove(game);
		PlayPairMove pairMove = new PlayPairMove(columns[2], columns[3], foundation);
		pairMove.doMove(game);
		PlayPairMove pairMove2 = new PlayPairMove(columns[1], columns[4], foundation);
		pairMove2.doMove(game);
		// now column 4 is empty and 0 has two cards
		move = new MoveCardToEmptyColumnMove(columns[0], columns[4]);
		assertTrue(move.valid(game));
		
		// undo everything
		pairMove2.undo(game);
		pairMove.undo(game);
		dealMove2.undo(game);
		kingMove.undo(game);
		dealMove.undo(game);
	}

	/**
	 * Test method for {@link krconverse.baroness.move.MoveCardToEmptyColumnMove#MoveCardToEmptyColumnMove(ks.common.model.Column, ks.common.model.Column, ks.common.model.Card)}.
	 */
	@Test
	public void testMoveCardToEmptyColumnMoveColumnColumnCard() {
		DealCardsMove dealMove = new DealCardsMove(deck, columns);
		
		// test with empty columns
		new MoveCardToEmptyColumnMove(columns[0], columns[1], columns[0].peek());
		// and with columns with cards
		dealMove.doMove(game);
		new MoveCardToEmptyColumnMove(columns[0], columns[1], columns[0].peek());
		dealMove.undo(game);
		
		// test with invalid input
		new MoveCardToEmptyColumnMove(null, columns[1], columns[0].peek());
		new MoveCardToEmptyColumnMove(columns[0], null, columns[0].peek());
		new MoveCardToEmptyColumnMove(columns[0], columns[1], null);
		new MoveCardToEmptyColumnMove(null, null, columns[0].peek());
		new MoveCardToEmptyColumnMove(null, columns[1], null);
		new MoveCardToEmptyColumnMove(columns[0], null, null);
		new MoveCardToEmptyColumnMove(null, null, null);
	}

	/**
	 * Test method for {@link krconverse.baroness.move.MoveCardToEmptyColumnMove#MoveCardToEmptyColumnMove(ks.common.model.Column, ks.common.model.Column)}.
	 */
	@Test
	public void testMoveCardToEmptyColumnMoveColumnColumn() {
		DealCardsMove dealMove = new DealCardsMove(deck, columns);
		
		// test with empty columns
		new MoveCardToEmptyColumnMove(columns[0], columns[1]);
		// and with columns with cards
		dealMove.doMove(game);
		new MoveCardToEmptyColumnMove(columns[0], columns[1]);
		dealMove.undo(game);
		
		// test with invalid input
		new MoveCardToEmptyColumnMove(null, columns[1]);
		new MoveCardToEmptyColumnMove(columns[0], null);
		new MoveCardToEmptyColumnMove(null, null);
	}

}

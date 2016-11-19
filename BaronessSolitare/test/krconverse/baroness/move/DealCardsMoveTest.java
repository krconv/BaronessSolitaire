/**
 * DealMoveTest.java
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
 * Test class for {@link krconverse.baroness.move.DealCardsMove}
 */
public class DealCardsMoveTest {
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
	 * Test method for {@link krconverse.baroness.move.DealCardsMove#doMove(ks.common.games.Solitaire)}.
	 */
	@Test
	public void testDoMove() {
		// test that cards can be dealt out
		DealCardsMove move = new DealCardsMove(deck, columns);
		assertTrue(move.doMove(game));
		assertEquals(1, columns[0].count());
		assertEquals(9, columns[0].peek().getRank());
		assertEquals(1, columns[1].count());
		assertEquals(10, columns[1].peek().getRank());
		assertEquals(1, columns[2].count());
		assertEquals(11, columns[2].peek().getRank());
		assertEquals(1, columns[3].count());
		assertEquals(12, columns[3].peek().getRank());
		assertEquals(1, columns[4].count());
		assertEquals(13, columns[4].peek().getRank());
		assertTrue(foundation.empty());
		
		assertEquals(47, deck.count());
		assertEquals(47, game.getNumLeft().getValue());
		move.undo(game);
		
		// test dealing out all of the cards
		DealCardsMove[] moves = new DealCardsMove[11]; // 11 deals to deal the whole deck
		for (int i = 0; i < 11; i++) {
			moves[i] = new DealCardsMove(deck, columns);
			assertTrue(moves[i].doMove(game));
			assertEquals(deck.count(), game.getNumLeft().getValue());
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
		move = new DealCardsMove(deck, columns);
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

		for (int i = 0; i < 11; i++) {
			moves[10 - i].undo(game);
		}
	}

	/**
	 * Test method for {@link krconverse.baroness.move.DealCardsMove#undo(ks.common.games.Solitaire)}.
	 */
	@Test
	public void testUndo() {
		// test that dealt cards can be undone
		DealCardsMove move = new DealCardsMove(deck, columns);
		move.doMove(game);
		assertTrue(move.undo(game));
		assertTrue(columns[0].empty());
		assertTrue(columns[1].empty());
		assertTrue(columns[2].empty());
		assertTrue(columns[3].empty());
		assertTrue(columns[4].empty());
		assertTrue(foundation.empty());
		assertEquals(52, deck.count());
		assertEquals(52, game.getNumLeft().getValue());
				
		// test dealing out all of the cards
		DealCardsMove[] moves = new DealCardsMove[11]; // 11 deals to deal the whole deck
		for (int i = 0; i < 11; i++) {
			moves[i] = new DealCardsMove(deck, columns);
			moves[i].doMove(game);
		}

		
		// test that undoing an invalid move does nothing
		move = new DealCardsMove(deck, columns);
		assertFalse(move.undo(game));
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
		
		// test that all of the valid moves can be undone
		for (int i = 0; i < 11; i++) {
			assertTrue(moves[10 - i].undo(game));
		}
	}

	/**
	 * Test method for {@link krconverse.baroness.move.DealCardsMove#valid(ks.common.games.Solitaire)}.
	 */
	@Test
	public void testValid() {
		
		// test that a deck move is valid with a full deck
		DealCardsMove move = new DealCardsMove(deck, columns);
		assertTrue(move.valid(game));
		
		// test dealing out all of the cards
		DealCardsMove[] moves = new DealCardsMove[11]; // 11 deals to deal the whole deck
		for (int i = 0; i < 11; i++) {
			moves[i] = new DealCardsMove(deck, columns);
			assertTrue(moves[i].valid(game));
			moves[i].doMove(game);
		}
		
		// a new move on the deck would be invalid
		move = new DealCardsMove(deck, columns);
		assertFalse(move.valid(game));

		for (int i = 0; i < 11; i++) {
			moves[10 - i].undo(game);
		}
	}

	/**
	 * Test method for {@link krconverse.baroness.move.DealCardsMove#DealCardsMove(ks.common.model.Deck, ks.common.model.Column[])}
	 */
	@Test
	public void testDealCardsMoveDeckColumn() {		
		// test with normal input, with and without empty deck
		new DealCardsMove(deck, columns);

		DealCardsMove[] moves = new DealCardsMove[11]; // 11 deals to deal the whole deck
		for (int i = 0; i < 11; i++) {
			moves[i] = new DealCardsMove(deck, columns);
			moves[i].doMove(game);
		}
		new DealCardsMove(deck, columns);
		for (int i = 0; i < 11; i++) {
			moves[10 - i].undo(game);
		}
		
		// test with bad input
		new DealCardsMove(null, columns);
		new DealCardsMove(deck, null);
		new DealCardsMove(null, null);
	}
}

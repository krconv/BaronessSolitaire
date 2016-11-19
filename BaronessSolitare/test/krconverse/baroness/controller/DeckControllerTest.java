/**
 * DeckControllerTest.java
 * 
 * @author Kodey Converse (kodey@krconv.com)
 */
package krconverse.baroness.controller;

import java.util.Enumeration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import krconverse.Baroness;
import krconverse.baroness.move.DealCardsMove;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.launcher.Main;
import ks.tests.KSTestCase;

/**
 * Test class for {@link krconverse.baroness.controller.DeckController}.
 */
public class DeckControllerTest extends KSTestCase {
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
	 * Test that clicking on the deck deals cards. 
	 */
	@Test
	public void testDealCards() {
		DeckController controller = new DeckController(game);
		controller.mouseClicked(createClicked(game, game.getDeckView(), 0, 0));
		
		// make sure that the move was done
		Enumeration<Move> moves = game.getMoves();
		assertTrue(moves.hasMoreElements());
		Move move = moves.nextElement();
		assertTrue(move instanceof DealCardsMove);
		assertTrue(game.undoMove());
		
		// test dealing all of the cards
		for (int i = 0; i < 11; i++) {
			controller.mouseClicked(createClicked(game, game.getDeckView(), 0, 0));
		}
		assertEquals(11, countEnumeration(game.getMoves()));
		
		// clicking on the deck again shouldn't do anything
		controller.mouseClicked(createClicked(game, game.getDeckView(), 0, 0));
		assertEquals(11, countEnumeration(game.getMoves()));
		
		// undo the valid deals
		for (int i = 0; i < 11; i++) {
			game.undoMove();
		}
	}
	
	/**
	 * Counts and returns the number of elements in the given enumeration.
	 * @param enumeration The enumeration to count.
	 * @return The number of elements.
	 */
	private int countEnumeration(Enumeration<?> enumeration) {
		int moveCount = 0;
		while (enumeration.hasMoreElements()) {
			enumeration.nextElement();
			moveCount++;
		}
		return moveCount;
	}

	/**
	 * Test method for {@link krconverse.baroness.controller.DeckController#DeckController(krconverse.Baroness, ks.common.model.Model)}.
	 */
	@Test
	public void testDeckController() {
		// test creating the controller
		new DeckController(game);
	}

}

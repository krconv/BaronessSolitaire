/**
 * FoundationControllerTest.java
 * 
 * @author Kodey Converse (krconverse@wpi.edu)
 */
package krconverse.baroness.controller;

import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import krconverse.Baroness;
import krconverse.baroness.move.PlayKingMove;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.launcher.Main;
import ks.tests.KSTestCase;

/**
 * Test class for {@link krconverse.baroness.controller.FoundationController}.
 */
public class FoundationControllerTest extends KSTestCase {
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
	 * Test that a king can be dropped on the foundation to play it. 
	 */
	@Test
	public void testPlayKing() {
		DeckController deckController = new DeckController(game);
		ColumnController[] columnControllers = new ColumnController[5];
		for (int i = 0; i < 5; i++) {
			columnControllers[i] = new ColumnController(game, game.getColumnViews()[i]);
		}
		FoundationController controller = new FoundationController(game);
		
		// test that an invalid move doesn't get tracked
		columnControllers[4].mousePressed(createPressed(game, game.getColumnViews()[4], 0, 0));
		controller.mouseReleased(createReleased(game, game.getFoundationView(), 0, 0));
		assertTrue(Collections.list(game.getMoves()).isEmpty());
		// with a non-king card
		deckController.mouseClicked(createClicked(game, game.getDeckView(), 0, 0));
		columnControllers[1].mousePressed(createPressed(game, game.getColumnViews()[1], 0, 0));
		controller.mouseReleased(createReleased(game, game.getFoundationView(), 0, 0));
		assertEquals(1, Collections.list(game.getMoves()).size());
				
		// test that a valid move does get tracked
		columnControllers[4].mousePressed(createPressed(game, game.getColumnViews()[4], 0, 0));
		controller.mouseReleased(createReleased(game, game.getFoundationView(), 0, 0));
		List<Move> moves = Collections.list(game.getMoves());
		assertEquals(2, Collections.list(game.getMoves()).size());
		Move move = moves.get(1);
		assertTrue(move instanceof PlayKingMove);
		assertTrue(game.undoMove());
		
		game.undoMove();
	}
	
	/**
	 * Test method for {@link krconverse.baroness.controller.FoundationController#FoundationController(krconverse.Baroness, ks.common.model.Model)}.
	 */
	@Test
	public void testFoundationController() {
		// test creating a foundation controller with valid input
		new FoundationController(game);
	}

}

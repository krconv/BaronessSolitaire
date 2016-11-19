/**
 * ColumnControllerTest.java
 * 
 * @author Kodey Converse (kodey@krconv.com)
 */
package krconverse.baroness.controller;

import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import krconverse.Baroness;
import krconverse.baroness.move.MoveCardToEmptyColumnMove;
import krconverse.baroness.move.PlayKingMove;
import krconverse.baroness.move.PlayPairMove;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.model.Move;
import ks.common.model.Pile;
import ks.launcher.Main;
import ks.tests.KSTestCase;

/**
 * Test class for {@link krconverse.baroness.controller.FoundationController}
 */
public class ColumnControllerTest extends KSTestCase {
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
	 * Test that dragging two cards onto each other attempts to play a pair.
	 */
	@Test
	public void testPlayPair() {
		DeckController deckController = new DeckController(game);
		ColumnController sourceController = new ColumnController(game, game.getColumnViews()[1]);
		ColumnController targetController = new ColumnController(game, game.getColumnViews()[4]);
		int cardOffset = game.getCardImages().getOverlap();
		
		// test that an invalid move doesn't get tracked
		sourceController.mousePressed(createPressed(game, game.getColumnViews()[1], 0, 0));
		targetController.mouseReleased(createReleased(game, game.getColumnViews()[4], 0, 0));
		assertTrue(Collections.list(game.getMoves()).isEmpty());
		
		// test that a valid move does get tracked
		deckController.mouseClicked(createClicked(game, game.getDeckView(), 0, 0));
		deckController.mouseClicked(createClicked(game, game.getDeckView(), 0, 0));
		sourceController.mousePressed(createPressed(game, game.getColumnViews()[1], 0, cardOffset));
		targetController.mouseReleased(createReleased(game, game.getColumnViews()[4], 0, cardOffset));
		List<Move> moves = Collections.list(game.getMoves());
		assertEquals(3, Collections.list(game.getMoves()).size());
		Move move = moves.get(2);
		assertTrue(move instanceof PlayPairMove);
		assertTrue(game.undoMove());
		
		game.undoMove();
		game.undoMove();
	}
	
	/**
	 * Test that clicking on a king plays the king.
	 */
	@Test
	public void testPlayKing() {
		DeckController deckController = new DeckController(game);
		ColumnController controller = new ColumnController(game, game.getColumnViews()[4]);
		
		// test that an invalid move doesn't get tracked
		controller.mouseClicked(createClicked(game, game.getColumnViews()[4], 0, 0));
		assertTrue(Collections.list(game.getMoves()).isEmpty());
		
		// test that a valid move does get tracked
		deckController.mouseClicked(createClicked(game, game.getDeckView(), 0, 0));
		controller.mouseClicked(createClicked(game, game.getColumnViews()[4], 0, 0));
		List<Move> moves = Collections.list(game.getMoves());
		assertEquals(2, Collections.list(game.getMoves()).size());
		Move move = moves.get(1);
		assertTrue(move instanceof PlayKingMove);
		assertTrue(game.undoMove());
		
		game.undoMove();
	}


	/**
	 * Test that dragging a card to an empty column will move it.
	 */
	@Test
	public void testMoveToEmpty() {
		DeckController deckController = new DeckController(game);
		ColumnController[] controllers = new ColumnController[5];
		for (int i = 0; i < 5; i++) {
			controllers[i] = new ColumnController(game, game.getColumnViews()[i]);
		}
		int cardOffset = game.getCardImages().getOverlap();
		
		// test that an invalid move doesn't get tracked
		controllers[1].mousePressed(createPressed(game, game.getColumnViews()[1], 0, 0));
		controllers[4].mouseReleased(createReleased(game, game.getColumnViews()[4], 0, 0));
		assertTrue(Collections.list(game.getMoves()).isEmpty());
		
		// test that a valid move does get tracked
		deckController.mouseClicked(createClicked(game, game.getDeckView(), 0, 0));
		controllers[4].mouseClicked(createClicked(game, game.getColumnViews()[4], 0, 0));
		deckController.mouseClicked(createClicked(game, game.getDeckView(), 0, 0));
		controllers[2].mousePressed(createPressed(game, game.getColumnViews()[2], 0, cardOffset));
		controllers[3].mouseReleased(createReleased(game, game.getColumnViews()[3], 0, cardOffset));
		controllers[1].mousePressed(createPressed(game, game.getColumnViews()[1], 0, cardOffset));
		controllers[4].mouseReleased(createReleased(game, game.getColumnViews()[4], 0, 0));
		// the move
		controllers[0].mousePressed(createPressed(game, game.getColumnViews()[0], 0, cardOffset));
		controllers[4].mouseReleased(createReleased(game, game.getColumnViews()[4], 0, 0));
		List<Move> moves = Collections.list(game.getMoves());
		assertEquals(6, moves.size());
		Move move = moves.get(5);
		assertTrue(move instanceof MoveCardToEmptyColumnMove);
		assertTrue(game.undoMove());
		
		game.undoMove();
		game.undoMove();
		game.undoMove();
		game.undoMove();
		game.undoMove();
	}
	
	/**
	 * Test method for {@link krconverse.baroness.controller.ColumnController#ColumnController(krconverse.Baroness, ks.common.model.Model, ks.common.view.ColumnView)}.
	 */
	@Test
	public void testColumnController() {
		// test creating the controller with valid input
		new ColumnController(game, game.getColumnViews()[0]);
	}

}

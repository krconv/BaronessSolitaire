/**
 * BaronessTest.java
 * 
 * @author Kodey Converse (krconverse@wpi.edu)
 */
package krconverse;

import org.junit.Test;

import junit.framework.TestCase;
import ks.common.games.SolitaireSolver;
import ks.launcher.Main;

/**
 * Test class for {@link krconverse.Baroness}.
 */
public class BaronessTest extends TestCase {
	
	/**
	 * Test that the auto solver will can win a game. 
	 */
	@Test
	public void testAutoSolver() {
		Baroness game = new Baroness();
		Main.generateWindow(game, 2115290114);

		SolitaireSolver solver = new SolitaireSolver(game);
		solver.run();
		
		// HACK! we know if the game was won by seeing of the seed was increased.
		assertEquals(2115290115, game.getSeed());
		
		game.dispose();
	}

	/**
	 * Test method for {@link krconverse.Baroness#Baroness()}.
	 */
	@Test
	public void testBaroness() {
		// try creating a new game
		new Baroness();
	}

}

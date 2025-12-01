//Lin Khit Lu
package lab12;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract class Rollable{
	protected int sides;
	
	public Rollable (int sides) {
		this.sides = sides;
	}
	
	// concrete method to return the number of sides
	public int getSides() {
		return sides;
	}
	
	// abstract method to be implemented by subclasses to define specific rolling behavior
	public abstract int roll(); // each dice defines this differently
}

// Behavior: rolls a number between 1 and 6 with equal probability
// Purpose: a neutral, fair option for players who prefer predictability
class StandardDice extends Rollable{
		private Random rand;
		
		public StandardDice() {
			super(6); // super refers to the parent class (Rollable) //Calls the parent’s constructor with 10 as the number of sides
			rand = new Random();
		}

		@Override
		public int roll() { // implement the roll() method to generate a random number between 1 and 6
			return rand.nextInt(sides)+1;
		}
}

// Behavior: Rolls a number between 1 and 6, favoring rolling a 6 more frequently (e.g.,60% chance)
// Purpose: Appeals to players who value higher scores with some predictability
class LoadedDice extends Rollable{
	private Random rand;
	
	public LoadedDice() {
		super(6);
		rand = new Random();
	}
	
	@Override
	public int roll() {
		int chance = rand.nextInt(10);
		// Use conditional logic with a random number to skew the probabilities, ensuring a higher chance of rolling a 6
		if (chance < 6) { //60% chance to roll a 6
			return 6; 
		} else {
			return rand.nextInt(5) + 1; //1-5
		}
	}
}

// Behavior: Rolls numbers between 6 and 10, guaranteeing high results
// Purpose: Ideal for players who want consistent, high scores
class HighRollDice extends Rollable{
	private Random rand;
	
	public HighRollDice() {
		super(10);
		rand = new Random();
	}
	
	@Override
	public int roll() {
		return rand.nextInt(5)+6; // 6 to 10
	}
}

// Behavior: Randomly mimics the behavior of another dice type for each roll
// Purpose: Adds unpredictability, appealing to players who enjoy surprises
class RandomDice extends Rollable{
	private Random rand;
	
	public RandomDice() {
		super(0); // doesn't really have its own side; will change depending on what we roll
		rand = new Random();
	}
	
	@Override
	public int roll() {
		Rollable dice;
		/* Use a random number to select another dice type
		 * (e.g. StandardDice, LoadedDice, or HighRollDice) and delegate the rolling logic
		 */
		
		int pick = rand.nextInt(4); // 0 to 3
		
		if (pick == 0) {
			dice = new StandardDice();	
		} else if (pick == 1) {
			dice = new LoadedDice();
		} else if (pick == 2) {
		    dice = new HighRollDice();
		} else {
		    dice = new NegativeDice();
		}
		return dice.roll();
	}
}

// Behavior: Rolls numbers between -3 and 3, potentially subtracting points from the player's score
// Purpose: Introduction a risk-reward element for players who enjoy taking chances
class NegativeDice extends Rollable {
	private Random rand;
	
	public NegativeDice() {
		super(6); // even if it's negative, it's still 6 sides
		rand = new Random();
	}
	
	public int roll() {
		return rand.nextInt(7) - 3; // -3 to +3
		//return -(rand.nextInt(sides)+1); // -1 to -6
	}
}

class Player implements Comparable <Player> { // the player class represents each participant in the game
	public static final int SORT_BY_SCORE = 1;
	public static final int SORT_BY_NAME = 2;
	public static int comparisonMode = SORT_BY_SCORE; // Static field to control/ toggle between comparing by score or name
	
	private String name; // the player's name
	private int score; // the player's current score
	
	public Player (String name) {
		this.name = name;
		score = 0;
	}
	
	public String getName() { // accessors for player attributes
		return name;
	}
	
	public int getScore() { // accessors for player attributes
		return score;
	}
	
	public void updateScore (int points) { // updates the score by adding or subtracting points
		score += points; // if "points" is negative, it subtracts
	}
	
	// introduce encapsulation by grouping player-related data and methods into a single class
	public void playTurn (Rollable dice) { 
		int rollResult = dice.roll(); // simulates a turn by rolling the dice
		// ensure the player can choose which dice to roll during their turn
		System.out.println(name + " rolled a " + rollResult + " using " + dice.getClass().getSimpleName() + "!");
		// added what kind of dice players use as an extra from the sample output because it helps me understand more
		updateScore(rollResult); // update the score
		System.out.println (name + "'s Current Score: " + score);
		
	}
	@Override
	public String toString() {
	    return name + ": " + score + " points";
	}
	
	@Override
	public int compareTo (Player other) { 
		if (comparisonMode == SORT_BY_NAME) {
			return this.name.compareTo(other.name); // Alphabetical order
		} else {
			return Integer.compare(other.score,  this.score); // Descending score
		}
	}
}

class Leaderboard {
	private List<Player> players = new ArrayList<>();
	
	public void addPlayer (Player player) {
		players.add(player); // adds a player to the leader-board
	}
	
	public void displayLeaderboard() { // displays players, sorted by scores or names
		Collections.sort(players);
		System.out.println("=== Leaderboard ===");
		int rank = 1;
		for (Player p: players) {
			System.out.printf("%d. %s - %d points%n", rank++, p.getName(), p.getScore());
		}
	}
	
	public Player getWinner() {
		Collections.sort(players);
		return players.get(0);
	}
}
	
public class Task1 {
		private static Scanner scanner = new Scanner(System.in);

		public static void main(String[] args) {
			
		    // Game setup
		    System.out.print("Enter the number of players: "); // Prompt the user to enter the number of players and their names
		    int numPlayers = getPositiveIntInput(); 
		    
		    List<Player> players = new ArrayList<>();
		    for (int i = 0; i < numPlayers; i++) {
		        System.out.print("Enter name for Player " + (i+1) + ": ");
		        String name = scanner.nextLine();
		        players.add(new Player(name));
		    }
		    
		    System.out.print("Enter the number of turns to play: ");
		    int numTurns = getPositiveIntInput(); // Allow the user to specify the number of turns for the game
		    
		    Leaderboard leaderboard = new Leaderboard(); // Initialize a Leaderboard object to track players
		    for (Player p : players) {
		        leaderboard.addPlayer(p);
		    }
		    
		    // Create dice options
		    Rollable[] diceOptions = {
		        new StandardDice(),
		        new LoadedDice(),
		        new HighRollDice(),
		        new NegativeDice(),
		        new RandomDice()
		    };
		    String[] diceNames = {
		    	    "Standard Dice (6-sided)",
		    	    "Loaded Dice (6-sided, higher chance of rolling 6)",
		    	    "High Roll Dice (10-sided, always rolls 6 or higher)",
		    	    "Random Dice (Mimics behavior of any dice type)",
		    	    "Negative Dice (Rolls between -3 and +3)"
		    	};
		    
		    System.out.println("Welcome to Dice Adventure!");
		    
		    // Gameplay loop
		    for (int turn = 0; turn < numTurns; turn++) { // Loop through the specified number of turns
		        System.out.println("\n=== Turn " + (turn+1) + " of " + numTurns + " ===");
		        
		        // During each turn:
		        for (Player player : players) { // Loop through all players
		            System.out.println("\nIt's " + player.getName() + "'s turn!" + "(current score: " + player.getScore() + ")");	
		            // added current score as an extra from the sample output because it helps me understand more
		            
		            System.out.println("Choose a dice to roll:");
		            for (int i = 0; i < diceNames.length; i++) {
		                System.out.println((i+1) + ". " + diceNames[i]);
		            }
		           
		            System.out.print("Enter your choice (1/2/3/4/5): ");
		            int choice;
		            if (scanner.hasNextInt()) {
		                choice = scanner.nextInt();
		                if (choice < 1 || choice > diceNames.length) {
		                    System.out.println("Invalid choice, using Standard Dice by default.");
		                    choice = 1; // Default to Standard Dice
		                }
		            } else {
		                scanner.next(); // Clear non-integer input
		                System.out.println("Invalid choice, using Standard Dice by default.");
		                choice = 1;
		            }
		            scanner.nextLine(); // Consume newline
		            Rollable selectedDice = diceOptions[choice - 1];
		            
		            // Roll the selected dice, update the player's score, and display the result
		            player.playTurn(selectedDice);
		        }
		    }
		    
		    // Game End
		    System.out.println("\n=== GAME OVER ===");
		    leaderboard.displayLeaderboard(); // Display a sorted leader-board based on scores or names
		    
		    Player winner = leaderboard.getWinner(); // Announce the winner
		    System.out.println("=====================");
		    System.out.println("\nThe winner is " + winner.getName() + " with a score of " + winner.getScore());
		    System.out.println("Thanks for playing Dice Adventure!");
		}

		// Helper method to get positive integer input
		private static int getPositiveIntInput() {
		    while (true) {
		        if (scanner.hasNextInt()) {
		            int input = scanner.nextInt();
		            scanner.nextLine(); // Consume newline
		            if (input > 0) {
		                return input;
		            }
		        } else {
		            scanner.nextLine(); // Clear invalid input
		        }
		        System.out.print("Please enter a positive number: ");
		    }
		}
}
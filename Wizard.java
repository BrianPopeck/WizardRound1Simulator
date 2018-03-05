import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.io.*;

public class Wizard 
{
	public static String[] suits = {"Spade", "Club", "Heart", "Diamond"};
	//public static String[] cardValues = {"Ace", "King", "Queen", "Jack", "10", "9", "8", "7", "6", "5", "4", "3", "2"};
	public enum CARD_VALUES {
		WIZARD, ACE, KING, QUEEN, JACK, TEN, NINE, EIGHT, SEVEN, SIX, FIVE, FOUR, THREE, TWO, JESTER
	};

	public static final int NUM_OF_PLAYERS = 4;
	public static final int NUM_OF_GAMES = 1000000;

	public static ArrayList<Player> players = new ArrayList<Player>();
	public static ArrayList<Card> deck = new ArrayList<Card>();

	public static Random rand = new Random();
	public static String filename = "Wizard_Data.csv";

	public static String trump;
	public static String ledSuit;
	public static ArrayList<Card> playingBoard = new ArrayList<Card>();

	public static PrintWriter outputWriter;

	public static void main(String[] args) throws IOException {
		
		final long startTime = System.nanoTime();

		outputWriter = new PrintWriter(filename);
		for (int i=1; i<=NUM_OF_PLAYERS; i++) {
			outputWriter.print("Player "+i+" Place, Player "+i+" Card Value, Player "+i+" Trump, Player " +i+ " Won Trick, ");
		}
		outputWriter.print("\n");
		//outputWriter.println("Player 1, Player 1 Card Value, Player 1 Trump, Player ");

		for (int i=0; i<NUM_OF_GAMES; i++) {
			buildDeck();

			dealHand();

			playHand();
		}

		outputWriter.close();

		final long duration = System.nanoTime() - startTime;

		System.out.println("Running time: " + (duration/1000000000) + " seconds");
		
	}

	public static void buildDeck() {
		for (int i=0; i<suits.length; i++) {
			for (int j=1; j<CARD_VALUES.values().length-1; j++) {
				deck.add(new Card(suits[i], CARD_VALUES.values()[j]));
			}
		}

		for (int i=0; i<4; i++) {
			deck.add(new Card(CARD_VALUES.WIZARD));
		}

		for (int i=0; i<4; i++) {
			deck.add(new Card(CARD_VALUES.JESTER));
		}

		/*
		//debug
		for (Card card : deck) {
			System.out.println(card.getValue() + " of " + card.getSuit() + "s");
		}
		*/
	}

	public static void dealHand() {
		
		//repopulate players
		for (int i=0; i<NUM_OF_PLAYERS; i++) {
			players.add(new Player());
		}

		//Deal cards
		int randomCardIndex;
		for (Player player: players) {
			randomCardIndex = rand.nextInt(deck.size()); 
			player.dealCard(deck.get(randomCardIndex));
			deck.remove(randomCardIndex);
			//System.out.println("Player " + (players.indexOf(player) + 1) +  " has the " + player.getHand().get(0).getValue() + " of " + player.getHand().get(0).getSuit() + "s");
		}

		//turn over trump
		randomCardIndex = rand.nextInt(deck.size()); 
		Card trumpCard = deck.get(randomCardIndex);
		deck.remove(randomCardIndex);
		if (trumpCard.getSuit() != null || trumpCard.getValue().equals(CARD_VALUES.JESTER)) {
			trump = trumpCard.getSuit();
		} else { //it must be a wizard
			if (players.get(NUM_OF_PLAYERS-1).getHand().get(0).getSuit() != null) {
				trump = players.get(NUM_OF_PLAYERS-1).getHand().get(0).getSuit();
			} else {
				trump = suits[rand.nextInt(NUM_OF_PLAYERS-1)];
			}
		}
		

		//debug
		//System.out.println("The card turned over for trump is the " + trumpCard.getValue() + " of " + trumpCard.getSuit() + "s");


		
	}

	public static void playHand() {
		
		

		for (int i=0; i<NUM_OF_PLAYERS; i++) {
			ledSuit = players.get(i).getHand().get(0).getSuit();
			if (ledSuit!=null) {
				break;
			}
		}

		//update cards based on trump and ledSuit
		for (Player player : players) {
			Card card = player.getHand().get(0).copy();
			Card aWizard = new Card(CARD_VALUES.WIZARD);

			try
			{
				if (card.getSuit().equals(trump)) {
					card.setIsTrump(true);
				} 

				if (card.getSuit().equals(ledSuit)) {
					card.setMatchesLedSuit(true);
				}
			}
			catch (NullPointerException e) {
				//This means there is a wizard or jester...don't need to do anything
			}
			finally {
				if (card.equals(aWizard)) {
					card.setIsTrump(true);
					//System.out.println("Wizards are considered trump!");
				}
			}
			

			player.getHand().set(0, card);
			playingBoard.add(player.getHand().get(0));
		}

		Collections.sort(playingBoard);

		/*
		//Old way to export data
		for (Player player : players) {
			if (playingBoard.get(0).equals(player.getHand().get(0))) {
				//System.out.println("Player " + (players.indexOf(player)+1) + " won the trick with the " + playingBoard.get(0).getValue() + " of " + playingBoard.get(0).getSuit() + "s.");
				outputWriter.println((players.indexOf(player)+1) + ", " + playingBoard.get(0).getValue() + ", " + playingBoard.get(0).getIsTrump());
				break;
			}
		}
		*/

		for (Player player : players) {
			Card card = player.getHand().get(0);
			boolean wonTrick;

			if (playingBoard.indexOf(card)==0) {
				wonTrick = true;
			} else {
				wonTrick = false;
			}
			outputWriter.print((playingBoard.indexOf(card)+1) + ", " + card.getValue() + ", " + card.getIsTrump() + ", " + wonTrick + ", ");
		}

		outputWriter.print("\n");
		



		
		/*
		//debugging
		for (int i=0; i<playingBoard.size(); i++) {
			System.out.println(i+1+". " + playingBoard.get(i).getValue() + " of " + playingBoard.get(i).getSuit() + "s");
			System.out.println("This card is trump: "+playingBoard.get(i).getIsTrump() + "; This card matches the led suit: " +playingBoard.get(i).getMatchesLedSuit());
		}
		*/

		//clear player hands
		for (Player player : players) {
			player.getHand().clear();
		}
		
		//reset board
		playingBoard.clear();
		deck.clear();
		players.clear();
		trump = "";
		ledSuit = "";
	}

}


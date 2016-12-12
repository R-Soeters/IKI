/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 * 
 * Ruben Soeters
 * Studentnummer 11040017
 * Werkgroep G
 */

import acm.io.*;
import acm.program.*;
import acm.util.*;
import java.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
    
    	public static void main(String[] args) {
    	    new Yahtzee().start(args);
   	 }
 

    	public void run() {
    	    	IODialog dialog = getDialog();
    	    	nPlayers = dialog.readInt("Enter number of players");
    	    	playerNames = new String[nPlayers];
   	     	for (int i = 1; i <= nPlayers; i++) {
   	        	playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
    	    	}
     	   	display = new YahtzeeDisplay(getGCanvas(), playerNames);
		
     	   	playGame();
 	   }


	private void playGame() {
		
		trackCategories = new int[nPlayers+1][N_CATEGORIES+1];
		trackHelpCategories = new int[nPlayers+1][N_CATEGORIES+1];
		trackTurns();

		//display.printMessage(playerNames[whoWonGame()-1] + " won the game!");	
		//Heb geprobeerd op lijn 116 dit te laten werken maar voor some reason gingen de brackets raar doen	
	}


	private int player;
	//Keeps track of whose turn it is and how many turns are remaining
	private void trackTurns() {
		int totalAmountTurns = N_SCORING_CATEGORIES * nPlayers;
		int counter = 0;
		int i = 1;
		while (counter < totalAmountTurns) {
			player = i;
			playerTurn();
	
			if ( i == nPlayers){
				i = 1;
			} else {
			i = i + 1;
			}

			counter = counter + 1;
		}	
	}


	//The category a player has chosen
	private int category;

	//A full player turn
	private void playerTurn() {
		display.printMessage(playerNames[player-1] + "'s turn!" + " Click 'Roll Dice' button to roll the dice.");
		display.waitForPlayerToClickRoll(player);
		rollAllDice();
		for ( int i = 0; i < 2; i++){
			display.displayDice(fiveDices);
			display.printMessage("please select the dice you want to reroll.");
			display.waitForPlayerToSelectDice();
			for(int j=0; j < N_DICE; j++){
				if(display.isDieSelected(j)){
					fiveDices[j] = rollDice();
				}
			}
			display.displayDice(fiveDices);	
		}
		
		//select category and check whether category has already been used, if so don't change score and ask for other category
		display.printMessage("Your turn is almost over! Select a category.");		
		category = display.waitForPlayerToSelectCategory();

		while(trackHelpCategories[player][category] == -1){		
			display.printMessage("Please select an unfilled category.");
			category = display.waitForPlayerToSelectCategory();
			}
		
		//determine score and update scorecard
		determineScore();	
		display.updateScorecard(category, player, score);


		//putting scores in array
		trackCategories[player][category] = score;
		trackHelpCategories[player][category] = -1;

		//calculating of values for scores in arrays
		calcuUpper(player);
		calcuLower(player);
		if( trackCategories[player][UPPER_SCORE] >= 63){
			display.updateScorecard(UPPER_BONUS, player, 35);
			trackCategories[player][UPPER_BONUS] = 35;
		}
			
		
		//Display total/upper/lower at end of turn
		display.updateScorecard(TOTAL, player, calcuTotal(player));
		display.updateScorecard(UPPER_SCORE, player, calcuUpper(player));
		display.updateScorecard(LOWER_SCORE, player, calcuLower(player));
	}

	
/*	//Check who won the game
	private int whoWonGame(){
		int highest = 0;
		int winnerG = -1;
		int count = 1;
		while( count =< nPlayers){
			if(trackCategories[i][TOTAL] > highest){
				highest = trackCategories[i][TOTAL];
				winnerG = i;
			}
			count = count + 1;
		}
		
		return winner;
	}*/
		
				
			
			




 	//Keep track of scores for categories + helper categories for last checkup
	private int[][] trackCategories;
	private int[][] trackHelpCategories;  

	//calculate total
	private int calcuTotal(int player2){
		if(trackCategories[player2][UPPER_SCORE] >= 63){
		trackCategories[player2][TOTAL] = trackCategories[player2][UPPER_SCORE] + trackCategories[player2][LOWER_SCORE]
			+ trackCategories[player2][UPPER_BONUS];
		} else {trackCategories[player2][TOTAL] = trackCategories[player2][UPPER_SCORE] + trackCategories[player2][LOWER_SCORE];
		}
		return trackCategories[player2][TOTAL];
	}

	//calculate upper score
	private int calcuUpper(int player2){
		trackCategories[player2][UPPER_SCORE]= trackCategories[player2][ONES] + trackCategories[player2][TWOS] +
							trackCategories[player2][THREES] + trackCategories[player2][FOURS] +
							trackCategories[player2][FIVES] + trackCategories[player2][SIXES];
		return trackCategories[player2][UPPER_SCORE];
	}

	//calculate lower score
	private int calcuLower(int player2){
		trackCategories[player2][LOWER_SCORE]= trackCategories[player2][THREE_OF_A_KIND] + 
							trackCategories[player2][FOUR_OF_A_KIND] +
							trackCategories[player2][FULL_HOUSE] +
							trackCategories[player2][SMALL_STRAIGHT] +
							trackCategories[player2][LARGE_STRAIGHT] +
							trackCategories[player2][YAHTZEE] +
							trackCategories[player2][CHANCE];
		return trackCategories[player2][LOWER_SCORE];
	}
	




	//makes private instance variable for score
	private int score;
	//Determines the score a player gets in the category chosen
	private void determineScore(){
		score = 0;		

		switch(category){
			case ONES:
				for(int i=0; i < N_DICE; i++){
					if( fiveDices[i] == ONES){
						score = score + ONES;
					}
				}
				break;

			case TWOS:
				for(int i=0; i < N_DICE; i++){
					if( fiveDices[i] == TWOS){
						score = score + TWOS;
					}
				}
				break;

			case THREES:
				for(int i=0; i < N_DICE; i++){
					if( fiveDices[i] == THREES){
						score = score + THREES;
					}
				}
				break;

			case FOURS:
				for(int i=0; i < N_DICE; i++){
					if( fiveDices[i] == FOURS){
						score = score + FOURS;
					}
				}
				break;

			case FIVES:
				for(int i=0; i < N_DICE; i++){
					if( fiveDices[i] == FIVES){
						score = score + FIVES;
					}
				}
				break;

			case SIXES:
				for(int i=0; i < N_DICE; i++){
					if( fiveDices[i] == SIXES){
						score = score + SIXES;
					}
				}
				break;
	

			case THREE_OF_A_KIND:
				checkThreeOfAKind();
				break;

			case FOUR_OF_A_KIND:
				checkFourOfAKind();
				break;

			case FULL_HOUSE:
				checkFullHouse();
				break;

			case SMALL_STRAIGHT:
				checkSmallStraight();
				break;

			case LARGE_STRAIGHT:
				checkLargeStraight();
				break;

			case YAHTZEE:
				checkYahtzee();
				break;

			case CHANCE:
				for(int i=0; i < N_DICE; i++){
						score = score + fiveDices[i];
					}		
		}
	}
						
				
	



	//Method that checks for three of a kinds and returns the appropiate score
	private int checkThreeOfAKind(){
		score = 0;
		int trackerOne = 0;
		int trackerTwo = 0;
		int trackerThree = 0;
		int trackerFour = 0;
		int trackerFive = 0;
		int trackerSix = 0;

		for(int i=0; i < N_DICE; i++){
			if(fiveDices[i] == ONES){
				trackerOne = trackerOne + 1;
				if(trackerOne >= 3){
					for(int j=0; j < N_DICE; j++){
						score = score + fiveDices[j];
					}
				}
			} else if(fiveDices[i] == TWOS){
				trackerTwo = trackerTwo + 1;
				if(trackerTwo >= 3){
					for(int j=0; j < N_DICE; j++){
						score = score + fiveDices[j];
					}
				}
			} else if(fiveDices[i] == THREES){
				trackerThree = trackerThree + 1;
				if(trackerThree >= 3){
					for(int j=0; j < N_DICE; j++){
						score = score + fiveDices[j];
					}
				}
			} else if(fiveDices[i] == FOURS){
				trackerFour = trackerFour + 1;
				if(trackerFour >= 3){
					for(int j=0; j < N_DICE; j++){
						score = score + fiveDices[j];
					}
				}
			} else if(fiveDices[i] == FIVES){
				trackerFive = trackerFive + 1;
				if(trackerFive >= 3){
					for(int j=0; j < N_DICE; j++){
						score = score + fiveDices[j];
					}
				}
			} else if(fiveDices[i] == SIXES){
				trackerSix = trackerSix + 1;
				if(trackerSix >= 3){
					for(int j=0; j < N_DICE; j++){
						score = score + fiveDices[j];
					}
				}
			}
		}
		return score;
	}

	//Method that checks for four of a kinds and returns the appropiate score
	private int checkFourOfAKind(){
		score = 0;
		int trackerOne = 0;
		int trackerTwo = 0;
		int trackerThree = 0;
		int trackerFour = 0;
		int trackerFive = 0;
		int trackerSix = 0;

		for(int i=0; i < N_DICE; i++){
			if(fiveDices[i] == ONES){
				trackerOne = trackerOne + 1;
				if(trackerOne >= 4){
					for(int j=0; j < N_DICE; j++){
						score = score + fiveDices[j];
					}
				}
			} else if(fiveDices[i] == TWOS){
				trackerTwo = trackerTwo + 1;
				if(trackerTwo >= 4){
					for(int j=0; j < N_DICE; j++){
						score = score + fiveDices[j];
					}
				}
			} else if(fiveDices[i] == THREES){
				trackerThree = trackerThree + 1;
				if(trackerThree >= 4){
					for(int j=0; j < N_DICE; j++){
						score = score + fiveDices[j];
					}
				}
			} else if(fiveDices[i] == FOURS){
				trackerFour = trackerFour + 1;
				if(trackerFour >= 4){
					for(int j=0; j < N_DICE; j++){
						score = score + fiveDices[j];
					}
				}
			} else if(fiveDices[i] == FIVES){
				trackerFive = trackerFive + 1;
				if(trackerFive >= 4){
					for(int j=0; j < N_DICE; j++){
						score = score + fiveDices[j];
					}
				}
			} else if(fiveDices[i] == SIXES){
				trackerSix = trackerSix + 1;
				if(trackerSix >= 4){
					for(int j=0; j < N_DICE; j++){
						score = score + fiveDices[j];
					}
				}
			}
		}
		return score;
	}

	
	//method that checks for full houses
	private int checkFullHouse(){
		ArrayList<Integer> trackOnes = new ArrayList<Integer>();
		ArrayList<Integer> trackTwos = new ArrayList<Integer>();
		ArrayList<Integer> trackThrees = new ArrayList<Integer>();
		ArrayList<Integer> trackFours = new ArrayList<Integer>();
		ArrayList<Integer> trackFives = new ArrayList<Integer>();
		ArrayList<Integer> trackSixes = new ArrayList<Integer>();

	//puts numbers of thrown dices in seperate arraylists to keep track of how many times a number has been thrown
		for(int i=0; i < N_DICE; i++) {
			if (fiveDices[i] == ONES){
				trackOnes.add(fiveDices[i]);
			} else if (fiveDices[i] == TWOS){
				trackTwos.add(fiveDices[i]);
			} else if (fiveDices[i] == THREES){
				trackThrees.add(fiveDices[i]);
			} else if (fiveDices[i] == FOURS){
				trackFours.add(fiveDices[i]);
			} else if (fiveDices[i] == FIVES){
				trackFives.add(fiveDices[i]);
			} else if (fiveDices[i] == SIXES){
				trackSixes.add(fiveDices[i]);
			}	
		}

	//if size of one of the arraylists is 3 and another one is 2 a full house has been achieved and score = 25
		if( trackOnes.size() == 3 || trackTwos.size() == 3 || trackThrees.size() == 3 ||
			 trackFours.size() == 3 || trackFives.size() == 3 || trackSixes.size() == 3){

			if( trackOnes.size() == 2 || trackTwos.size() == 2 || trackThrees.size() == 2 ||
			 	trackFours.size() == 2 || trackFives.size() == 2 || trackSixes.size() == 2){
				score = 25;
			}
		}
		return score;
	}

	//method that checks for small straights
	private int checkSmallStraight(){
		ArrayList<Integer> trackOnes = new ArrayList<Integer>();
		ArrayList<Integer> trackTwos = new ArrayList<Integer>();
		ArrayList<Integer> trackThrees = new ArrayList<Integer>();
		ArrayList<Integer> trackFours = new ArrayList<Integer>();
		ArrayList<Integer> trackFives = new ArrayList<Integer>();
		ArrayList<Integer> trackSixes = new ArrayList<Integer>();

	//puts numbers of thrown dices in seperate arraylists to keep track of how many times a number has been thrown
		for(int i=0; i < N_DICE; i++) {
			if (fiveDices[i] == ONES){
				trackOnes.add(fiveDices[i]);
			} else if (fiveDices[i] == TWOS){
				trackTwos.add(fiveDices[i]);
			} else if (fiveDices[i] == THREES){
				trackThrees.add(fiveDices[i]);
			} else if (fiveDices[i] == FOURS){
				trackFours.add(fiveDices[i]);
			} else if (fiveDices[i] == FIVES){
				trackFives.add(fiveDices[i]);
			} else if (fiveDices[i] == SIXES){
				trackSixes.add(fiveDices[i]);
			}	
		}

	//if size of 4 different arraylists is at least 1 a small straight has been achieved, there are 3 configurations possible to achieve
	//a small straight (1234x, 2345x and 3456x) 
		if( trackOnes.size() >= 1 && trackTwos.size() >= 1 && trackThrees.size() >= 1 && trackFours.size() >= 1 ||
			trackTwos.size() >= 1 && trackThrees.size() >= 1 && trackFours.size() >= 1 && trackFives.size() >= 1 ||
			trackThrees.size() >= 1 && trackFours.size() >= 1 && trackFives.size() >= 1 && trackSixes.size() >= 1){
			
			score = 30;
		}
		return score;
	}

	//method that checks for large straights
	private int checkLargeStraight(){
		ArrayList<Integer> trackOnes = new ArrayList<Integer>();
		ArrayList<Integer> trackTwos = new ArrayList<Integer>();
		ArrayList<Integer> trackThrees = new ArrayList<Integer>();
		ArrayList<Integer> trackFours = new ArrayList<Integer>();
		ArrayList<Integer> trackFives = new ArrayList<Integer>();
		ArrayList<Integer> trackSixes = new ArrayList<Integer>();

	//puts numbers of thrown dices in seperate arraylists to keep track of how many times a number has been thrown
		for(int i=0; i < N_DICE; i++) {
			if (fiveDices[i] == ONES){
				trackOnes.add(fiveDices[i]);
			} else if (fiveDices[i] == TWOS){
				trackTwos.add(fiveDices[i]);
			} else if (fiveDices[i] == THREES){
				trackThrees.add(fiveDices[i]);
			} else if (fiveDices[i] == FOURS){
				trackFours.add(fiveDices[i]);
			} else if (fiveDices[i] == FIVES){
				trackFives.add(fiveDices[i]);
			} else if (fiveDices[i] == SIXES){
				trackSixes.add(fiveDices[i]);
			}	
		}

	//if size of 5 arraylists is 1 a large straight has been achieved, there are 2 configurations possible to achieve
	//a large straight (12345 and 23456) 
		if( trackOnes.size() == 1 && trackTwos.size() == 1 && trackThrees.size() == 1 && trackFours.size() == 1 
			&& trackFives.size() == 1 ||
			trackTwos.size() == 1 && trackThrees.size() == 1 && trackFours.size() == 1 && trackFives.size() == 1
			&& trackSixes.size() == 1){
		
			score = 40;
		}
		return score;
	}

	//method that checks for yahtzee
	private int checkYahtzee(){
		ArrayList<Integer> trackOnes = new ArrayList<Integer>();
		ArrayList<Integer> trackTwos = new ArrayList<Integer>();
		ArrayList<Integer> trackThrees = new ArrayList<Integer>();
		ArrayList<Integer> trackFours = new ArrayList<Integer>();
		ArrayList<Integer> trackFives = new ArrayList<Integer>();
		ArrayList<Integer> trackSixes = new ArrayList<Integer>();

	//puts numbers of thrown dices in seperate arraylists to keep track of how many times a number has been thrown
		for(int i=0; i < N_DICE; i++) {
			if (fiveDices[i] == ONES){
				trackOnes.add(fiveDices[i]);
			} else if (fiveDices[i] == TWOS){
				trackTwos.add(fiveDices[i]);
			} else if (fiveDices[i] == THREES){
				trackThrees.add(fiveDices[i]);
			} else if (fiveDices[i] == FOURS){
				trackFours.add(fiveDices[i]);
			} else if (fiveDices[i] == FIVES){
				trackFives.add(fiveDices[i]);
			} else if (fiveDices[i] == SIXES){
				trackSixes.add(fiveDices[i]);
			}	
		}

	//if one of the arrays has a size of 5 a yahtzee has been achieved
		if( trackOnes.size() == 5 || trackTwos.size() == 5 || trackThrees.size() == 5 ||
			 trackFours.size() == 5 || trackFives.size() == 5 || trackSixes.size() == 5){
			
			score = 50;
		}
		return score;
	}


		
	//create space for 5 dices
	private int[] fiveDices = new int[5];

	//method that rolls a single dice
	private int rollDice() {
		rgen = RandomGenerator.getInstance();
		int dieRoll = rgen.nextInt(1,6);
		return dieRoll;
	}
	
	//method that rolls 5 dice and displays them
	private void rollAllDice() {
		for(int i=0; i < N_DICE; i++) {
			fiveDices[i] = rollDice();
		}
	}
			

    	// Private instance variables
    	private int nPlayers;
    	private String[] playerNames;
    	private YahtzeeDisplay display;
    	private RandomGenerator rgen = new RandomGenerator();





}

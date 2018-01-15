package guessNumber;

import java.util.Random;
import java.util.Scanner;

import gamestudio.consoleui.ConsoleGameUI;

import java.lang.System;

public class guessNumber implements ConsoleGameUI {
	@Override
	public int run() {
		int score = 0;

		System.out.println("Select a max number");
		Scanner inputNumber = new Scanner(System.in);
		int maxNumber = inputNumber.nextInt();

		Random rand = new Random();
		int number = rand.nextInt(maxNumber) + 1;
		int tries = 0;
		Scanner input = new Scanner(System.in);
		int guessNumber;
		boolean win = false;

		while (win == false) {

			System.out.println("Guess a number between 1 and " + maxNumber + ": ");
			guessNumber = input.nextInt();
			tries++;

			if (guessNumber == number) {
				win = true;
			}

			else if (guessNumber < number) {
				System.out.println("Number is to low, tray again");

			}

			else if (guessNumber > number) {
				System.out.println("Number is to high, try again");

			}
		}
		System.out.println("You win!");
		System.out.println("It took you " + tries + " tries.");
		return score;

	}

	@Override
	public String getName() {

		return "GuessNumber";
	}
}
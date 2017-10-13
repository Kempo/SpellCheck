package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
/**
 * URL:
 * http://app.aspell.net/create?max_size=95&spelling=US&spelling=GBs&spelling=GBz&spelling=CA&spelling=AU&max_variant=0&diacritic=both&special=hacker&special=roman-numerals&download=wordlist&encoding=utf-8&format=inline
 * 
 * @author Aaron
 * text analyzer that will predict user input based on incorrectly spelled sentences or words.
 * assuming these are correctly formatted sentences(1 space separation)
 * 
 * algorithm:
 * 1) based on first letter
 * 2) based on last letter
 * - list of words based off first and last letter
 * 3) percent covered (50% > to be accepted)
 * 4) based on # of syllables 
 * 
 * TESTING PURPOSES:
 * pygeun
 * Pigeon
 * 
 * epephany
 * ephiphany
 * 
 * 
 * 
 * TODO:
 * - add a graphical user interface (incorporate javaFX because it's easier to use?)
 * - another additional method to use could be similar pronunciations s such as 'k' instead of 'c' or such. (more than just one letter differences)
 * - update it so you can use sentences? have it display multiple options of a sentence?
 */


public class Analyzer {
	private static String[] userInput; // in case I want to make input more than just one word
	private static String[] predictedInput;
	private static Scanner scanner = new Scanner(System.in);
	private static Words wordsInstance = new Words();
	public static void main(String args[]) throws IOException {
		wordsInstance.loadList();
		start();
	}
	private static void start() {
		System.out.println("Please enter a word.");
		String line = scanner.nextLine();
		userInput = line.split(" ");
		predictedInput = loopWords(userInput);
		if(!isDuplicate(userInput, predictedInput)) {
			System.out.println("input: " + Arrays.toString(userInput));
			System.out.println("predictions: " + Arrays.toString(predictedInput));
		}else{
			System.out.println("Correctly spelled text.");
		}
		System.out.println("New word? Y/N.");
		line = scanner.nextLine();
		if(line.equalsIgnoreCase("Y")) {
			start();
		}
	}
	// test method, not required
	private static boolean isDuplicate(String[] user, String[] predicted) {
		if(user.length == predicted.length) {
			for(int i = 0; i < user.length; i++) {
				if(!user[i].equals(predicted[i])) {
					return false;
				}
			}
		}else{
			return false;
		}
		return true;
	}
	/**
	 * 
	 * please edit this structure. using a string array just doesn't seem like it's very appropriate
	 */
	private static String[] loopWords(String[] input) {
		String[] pi = input.clone(); // clones the input array so we can edit our predicted input array separately! to keep things organized i guess
		if(pi.length == 1) { // if the input is only one word
			boolean correct = wordsInstance.getLoadedWords().contains(pi[0]);
			if(correct) {
				return pi; // returns the same word because it's correctly spelled
			}else{
				if(!correct) {
					int i = 1;
					List<String> words = getBestWord(pi[0]);
					pi[0] = ""; // clears the original input
					for(String options : words) {
						pi[0] += options + (i < (words.size()) ? ", " : ".");
						i++;
					}
					return pi;
				}
			}
		}else{
			/** have not looked into multiple words yet
			for(int i = 0; i < input.length; i++) { // loops through all the words the user has entered in
				pi[i] = getBestWord(input[i]);
			}
			**/
		}
		return null;
	}
	
	private static List<String> getBestWord(String inputWord) {
		List<String> recommended = new ArrayList<String>();
		double percent = 0;
		HashMap<String,Double> wordGroup = wordsInstance.getGroupBasedOnPercentage(inputWord, wordsInstance.getGroupBySyllables(inputWord, wordsInstance.getGroupBasedOnTerminalLetter(inputWord)));
		for(Entry<String, Double> word : wordGroup.entrySet()) {
			if(word.getValue() > percent){
				percent = word.getValue();
				recommended.add(word.getKey());
			}
		}
		return recommended;
	}
}

package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * 
 * @author Aaron
 *
 */
public class Words {
	private ArrayList<String> wordList = new ArrayList<String>();
	public void loadList() throws IOException {
		URL url = new URL("http://app.aspell.net/create?max_size=95&spelling=US&spelling=GBs&spelling=GBz&spelling=CA&spelling=AU&max_variant=0&diacritic=both&special=hacker&special=roman-numerals&download=wordlist&encoding=utf-8&format=inline");
        BufferedReader in = new BufferedReader(
        new InputStreamReader(url.openStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
        	if(!inputLine.isEmpty()) {
        		String[] segments = inputLine.split(" ");
        		String s = segments[0].toLowerCase();
        		// if the line contains one word and it is not a name
        		if(segments.length == 1 && segments[0].equals(s)) {
        			String word = segments[0].toLowerCase();
        			wordList.add(word);
        		}
        	}
        }
        in.close();
        System.out.println("Successfully loaded " + wordList.size() + " words.");
	}
	public ArrayList<String> getLoadedWords() {
		return wordList;
	}
	// 1) returns a list of words that have the same first and last letter
	public ArrayList<String> getGroupBasedOnTerminalLetter(String word) {
		ArrayList<String> group = new ArrayList<String>();
		String first = String.valueOf(word.charAt(0));
		String last = word.substring(word.length()-1);
		for(String s : wordList) {
			if(s.startsWith(first) && s.endsWith(last)) {
				group.add(s);
			}
		}
		return group;
	}
	// 2)
	public ArrayList<String> getGroupBySyllables(String word, ArrayList<String> wordsByTerminalLetter) {
		ArrayList<String> group = new ArrayList<String>();
		int originalSyllables = getSyllables(word);
		for(String w : wordsByTerminalLetter) {
			int s = getSyllables(word);
			if(originalSyllables == s) {
				group.add(w);
			}
		}
		
		return group;
	}
	private int getSyllables(String word) {
		word = word.toUpperCase();
		int syllables = 0; // number of syllables our input word has
		char[] vowels = {'A','E','I','O','U','Y'};
		/**
		certain vowel pairs = one syllable such as EA, IE, OU, EE, YO, OO, OA(if not last letters), EO, AY, EU
		other vowel pairs = two syllables, UA, IO, OA(if last letter)
		note: 'y' at the end is a syllable,
		**/
		char[] characters = word.toCharArray();
		
		for(int i = 0; i <= characters.length - 1; i++) {
			for(char v : vowels){
				if(characters[i] == v) {
					if(i != (characters.length - 1)) { // if it isn't the last word
						char nextChar = characters[i + 1];
						if(!isVowel(nextChar)) { // if a vowel is alone
							syllables++;
						}else{
							syllables += getSyllablesFromPair(characters[i], characters[i+1]);
							if((i+1) != characters.length-1) { // if the vowel is not the last character in the word
							i += 2; // move up index two spaces in order to skip the two letters that have been already examined
							}
						}
					}
					if(i == (characters.length - 1)) { // if it is the last word
						if(isVowel(characters[i])) {
							syllables++;
						}
					}
				}
			}
		}
		return syllables;
	}
	private boolean isVowel(char c) {
		if(c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U' || c == 'Y') {
			return true;
		}
		return false;
	}
	private int getSyllablesFromPair(char v, char v1) {
		String i = v + "" + v1; // supposedly an inefficient way of doing so, maybe improve later
		String[] pone = {"EA", "IE", "OU", "EE", "YO", "OO", "EO", "AY", "EU"}; // one syllable vowel pairs
		String[] tone = {"UA", "IO", "OA"};
		for(String s : pone) {
			if(i.equalsIgnoreCase(s)) {
				return 1; 
			}
		}
		for(String s : tone) {
			if(i.equalsIgnoreCase(s)){
				return 2;
			}
		}
		return 0;
	}
	
	// 3) returns a hashmap with words and their % closest to target word.
	public HashMap<String,Double> getGroupBasedOnPercentage(String word, ArrayList<String> wordsBySyllables) {
		char[] inputCharacters = word.toCharArray();
		HashMap<String,Double> group = new HashMap<String, Double>();
		for(String sampleWord : wordsBySyllables) { // loops through the words with both first and last letter the same
			char[] sampleCharacters = sampleWord.toCharArray(); // the word taken from the list
			int letterDiff = word.length() - sampleWord.length();
			int similarities = 0; // to compensate for the first and last letter
			// note, remember to end the method after these if statements are done. so it doesn't go on.
			if(letterDiff == 0) { // if the words are the same exact length.
				for(int i = 0; i < sampleCharacters.length; i++) {
					if(sampleCharacters[i] == inputCharacters[i]) {
						similarities++;
					}
				}
				double percent = ((double)similarities / (word.length()));
				if((percent >= .5)) { /** change, make it more efficient than just bigger than .5 **/
					group.put(sampleWord, percent);
				}
			}
			if((letterDiff < 0) && (letterDiff >= -2)) { // if the sample word is bigger and has a difference less than two
				
			}
			if(letterDiff > 0 && (letterDiff <= 2)) { // if the sample word is smaller and has difference less than two
				
			}
		}
		return group;
	}
	
	
}

package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author Aaron
 */
public class Organizer {
    private ArrayList<String> wordList = new ArrayList<String>();
    private String source = "http://app.aspell.net/create?max_size=95&spelling=US&spelling=GBs&spelling=GBz&spelling=CA&spelling=AU&max_variant=0&diacritic=both&special=hacker&special=roman-numerals&download=wordlist&encoding=utf-8&format=inline";

    public void loadList() throws IOException {
        URL url = new URL(source);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            if (!inputLine.isEmpty()) {
                String[] segments = inputLine.split(" ");
                String s = segments[0].toLowerCase();
                // if the line contains one word and it is not a name
                if (segments.length == 1 && segments[0].equals(s)) {
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
        ArrayList<String> group = new ArrayList<>();
        String first = String.valueOf(word.charAt(0));
        String last = word.substring(word.length() - 1);
        for (String s : wordList) {
            if (s.startsWith(first) && s.endsWith(last)) {
                group.add(s);
            }
        }
        return group;
    }

    // 2) returns a list of words that have the same # of syllables
    public ArrayList<String> getGroupBySyllables(String word, ArrayList<String> wordsByTerminalLetter) {
        ArrayList<String> group = new ArrayList<>();
        int originalSyllables = getSyllables(word);
        for (String w : wordsByTerminalLetter) {
            int sampleNum = getSyllables(w);
            if (originalSyllables == sampleNum) {
                group.add(w);
            }
        }
        return group;
    }

    /**
     * @param word
     * @return number of syllables in word
     */
    public int getSyllables(String word) {
        word = word.toUpperCase();
        int syllables = 0; // number of syllables our input word has
        char[] vowels = {'A', 'E', 'I', 'O', 'U', 'Y'};
        /**
         certain vowel pairs = one syllable such as EA, IE, OU, EE, YO, OO, OA(if not last letters), EO, AY, EU
         other vowel pairs = two syllables, UA, IO, OA(if last letter)
         note: 'y' at the end is a syllable,
         **/
        char[] characters = word.toCharArray();

        for (int i = 0; i <= characters.length - 1; i++) {
            for (char v : vowels) {
                if (characters[i] == v) {
                    if (i != (characters.length - 1)) { // if it isn't the last word
                        char nextChar = characters[i + 1];
                        if (!isVowel(nextChar)) { // if a vowel is alone
                            syllables++;
                        } else {
                            syllables += getSyllablesFromPair(characters, i, i+1);
                            if ((i + 1) != characters.length - 1) { // if the vowel is not the last character in the word
                                i += 2; // move up index two spaces in order to skip the two letters that have been already examined
                            }
                        }
                    }
                    if (i == (characters.length - 1)) { // if it is the last word
                        if (isVowel(characters[i])) {
                            syllables++;
                        }
                    }
                }
            }
        }
        return syllables;
    }

    private boolean isVowel(char c) {
        if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U' || c == 'Y') {
            return true;
        }
        return false;
    }

    private int getSyllablesFromPair(char[] characters, int i, int i1) {
        String pair = characters[i] + "" + characters[i1]; // supposedly an inefficient way of doing so, maybe improve later
        String[] one = {"EA", "IE", "OU", "EE", "YO", "OO", "EO", "AY", "EU", "EY"}; // one syllable vowel pairs
        String[] two = {"IO", "OA"}; // two syllable vowel pairs
        String[] varying = {"UA"}; // vowel pairs that may vary in syllables according to the word such as UA
        // guanine, antisexual, sensual, guard, manual, lingua
        // IF THE LETTER before UA is part of another vowel 'sound' then UA is two vowels. if the letter isn't part of a vowel sound, UA = 1
        for(String s : varying) {
            if(pair.equalsIgnoreCase(s)) {
                if((i != 0)) { // if the pair is not the first two characters of the word; it shouldn't be i think
                    if(((i-1) == 0) || ((i1) == (characters.length - 1)) || ((i1 + 1) == (characters.length-1))) { // if the character before the pair is the first letter or if the character after the pair is the last or if the pair is the last part to the word
                        return 1;
                    }else{
                        return 2;
                    }
                }
            }
        }
        for (String s : one) {
            if (pair.equalsIgnoreCase(s)) {
                return 1;
            }
        }
        for (String s : two) {
            if (pair.equalsIgnoreCase(s)) {
                return 2;
            }
        }
        return 0;
    }

    // 3) returns a hash map with words and their % closest to target word.
    public ArrayList<Word> getGroupBasedOnPercentage(String word, ArrayList<String> wordsBySyllables) {
        char[] inputCharacters = word.toCharArray();
        ArrayList<Word> group = new ArrayList<>();
        for (String sampleWord : wordsBySyllables) { // loops a list of words with the same # of syllables
            char[] sampleCharacters = sampleWord.toCharArray(); // the word taken from the list
            int letterDiff = Math.abs(word.length() - sampleWord.length());
            int similarities = 0;
            double percent;
            if (letterDiff == 0) { // if the comparison involves words of the same length
                for (int i = 0; i < sampleCharacters.length; i++) {
                    if (sampleCharacters[i] == inputCharacters[i]) {
                        similarities++;
                    }
                }
            } else {
                if ((letterDiff > 0) && letterDiff <= 2) { // if the sample word has a difference less than two in comparison to the input word

                    HashMap<Character, Integer> testData = new HashMap<>(); // a hash map of characters and their number of occurrences
                    HashMap<Character, Integer> inputData = new HashMap<>();

                    readData(sampleCharacters, testData);
                    readData(inputCharacters, inputData);
                    for (Character c : testData.keySet()) { // loops through our test word
                        if (inputData.containsKey(c)) { // if our input word has this character
                            int inputNum = inputData.get(c); // gets the number of occurrences for the character in our input word
                            int testNum = testData.get(c); // gets the number of occurrences for the character in our test word
                            if (inputNum == testNum) { // if the number of occurrences are equal, then add however many characters there are in the input word
                                similarities += inputNum;
                            }
                            if (inputNum > testNum) {
                                similarities += testNum;
                            }
                            if (inputNum < testNum) {
                                similarities += inputNum;
                            }
                        }
                    }
                }
            }
            percent = ((double) similarities) / sampleWord.length();
            if (percent >= .5) {
                group.add(new Word(sampleWord, percent));
            }
        }
        return group;
    }

    public void readData(char[] characterList, HashMap<Character, Integer> map) {
        for (char ch : characterList) {
            Integer count = map.get(ch); // gets the current count of the character 'ch'
            if (count == null) { // if the value is null
                map.put(ch, 1);
            } else {
                map.put(ch, count + 1);
            }
        }
    }
}

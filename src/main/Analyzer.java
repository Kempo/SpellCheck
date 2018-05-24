package main;

import java.io.IOException;
import java.util.*;

/**
 * URL:
 * http://app.aspell.net/create?max_size=95&spelling=US&spelling=GBs&spelling=GBz&spelling=CA&spelling=AU&max_variant=0&diacritic=both&special=hacker&special=roman-numerals&download=wordlist&encoding=utf-8&format=inline
 *
 * @author Aaron
 * text analyzer that will guess user input based on incorrectly spelled words.
 *
 * algorithm:
 * 1) based on first letter and last letter
 * 2) percent similarity (50% > to be accepted)
 * 3) based on # of syllables
 *
 * TESTING PURPOSES:
 * pygeun
 * Pigeon
 *
 * epephany
 * Epiphany
 *
 * TODO:
 * - add a graphical user interface (incorporate javaFX because it's easier to use?)
 * - another additional method to use could be similar pronunciations s such as 'k' instead of 'c' or such. (more than just one letter differences)
 * - update it so you can use sentences. perhaps, multiple options of the sentence
 */


public class Analyzer {
    private String userInput;
    private List<Word> predictedInput;
    private Organizer organizerInstance;

    public void start(Organizer organizer) throws IOException {
        organizerInstance = organizer;
        userInput = ""; //input case
        if(!userInput.isEmpty()) {
            predictedInput = getWordList(userInput);
            display(); // console output
        }
    }

    private void display() {
        if (!isDuplicate(userInput, predictedInput)) {
            System.out.println("input: " + userInput);
            System.out.println("predictions: " + getTopWords(6, predictedInput));
        } else {
            System.out.println("Correctly spelled text.");
        }
    }

    /**
     *
     * @param num
     * @param words
     * @return string of the top (num) words of the list
     */
    private String getTopWords(int num, List<Word> words) {
        String list = "";
        int i = 1;
        while((i <= num) && (words.size() != i)) {
            list += words.get(i).getWord() + (i != num ? ", " : ".");
            i += 1;
        }
        return list;
    }

    private boolean isDuplicate(String userInput, List<Word> predicted) {
        for (Word word : predicted) {
            if (word.getWord().equalsIgnoreCase(userInput)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param inputWord
     * @return list of words based upon percentage similarity, syllables, and terminal letters
     */
    private List<Word> getWordList(String inputWord) {
        List<Word> wordGroup = organizerInstance.getGroupBasedOnPercentage(inputWord, organizerInstance.getGroupBySyllables(inputWord, organizerInstance.getGroupBasedOnTerminalLetter(inputWord)));
        Collections.sort(wordGroup, new SimilarityComparator()); // sorts the list
        Collections.reverse(wordGroup); // reverses order so the first object is closest
        return wordGroup;
    }
}

package main;

import java.io.IOException;
import java.util.*;

/**
 * URL:
 * http://app.aspell.net/create?max_size=95&spelling=US&spelling=GBs&spelling=GBz&spelling=CA&spelling=AU&max_variant=0&diacritic=both&special=hacker&special=roman-numerals&download=wordlist&encoding=utf-8&format=inline
 *
 * @author Aaron
 * text analyzer that will predict user input based on incorrectly spelled sentences or words.
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
 * Epiphany
 *
 * TODO:
 * - add a graphical user interface (incorporate javaFX because it's easier to use?)
 * - another additional method to use could be similar pronunciations s such as 'k' instead of 'c' or such. (more than just one letter differences)
 * - update it so you can use sentences? have it display multiple options of a sentence?
 */


public class Analyzer {
    private String userInput;
    private List<Word> predictedInput;
    private Scanner scanner = new Scanner(System.in);
    private Organizer organizerInstance;

    public void start(Organizer organizer) throws IOException {
        if (organizer != organizerInstance) {
            organizerInstance = organizer;
        }
        System.out.println("Please enter a word.");
        String line = scanner.nextLine();
        userInput = line;
        predictedInput = getWordList(userInput);
        display();
        System.out.println("New word? Y/N.");
        line = scanner.nextLine();
        if (line.equalsIgnoreCase("Y")) {
            start(organizerInstance);
        }
    }

    private void display() {
        if (!isDuplicate(userInput, predictedInput)) {
            System.out.println("input: " + userInput);
            System.out.println("predictions: " + predictedInput.toString()); // FIX FIX FIX
        } else {
            System.out.println("Correctly spelled text.");
        }
    }

    private boolean isDuplicate(String userInput, List<Word> predicted) {
        for(Word word : predicted) {
            if(word.getWord().equalsIgnoreCase(userInput)) {
                return true;
            }
        }
        return false;
    }


    private List<Word> getWordList(String inputWord) {
        List<Word> wordGroup = organizerInstance.getGroupBasedOnPercentage(inputWord, organizerInstance.getGroupBySyllables(inputWord, organizerInstance.getGroupBasedOnTerminalLetter(inputWord)));
        Collections.sort(wordGroup, new SimilarityComparator());
        for(Word w : wordGroup) {
            System.out.println(w.getWord() + " " + w.getPercent());
        }
        return wordGroup;
    }
}

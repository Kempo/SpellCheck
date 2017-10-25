package main;

public class TextPredictor {
    private static Analyzer analyzer;
    private static Words wordsInstance;
    public static void main(String[] args) throws Exception {
        analyzer = new Analyzer();
        wordsInstance = new Words();
        wordsInstance.loadList();
        analyzer.start(wordsInstance);
    }
}

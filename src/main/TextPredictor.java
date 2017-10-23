package main;

public class TextPredictor {
    private static Analyzer analyzer;
    public static void main(String[] args) throws Exception {
        analyzer = new Analyzer();
        analyzer.start();
    }
}

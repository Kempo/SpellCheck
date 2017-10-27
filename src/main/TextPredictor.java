package main;


public class TextPredictor {
    private static Analyzer analyzer;
    private static Organizer organizerInstance;

    public static void main(String[] args) throws Exception {

        analyzer = new Analyzer();
        organizerInstance = new Organizer();
        organizerInstance.loadList();
        analyzer.start(organizerInstance);

    }

}

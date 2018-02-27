package com.kempo.spellcheck.application;


public class SpellCheck {
    private static Analyzer analyzer = new Analyzer();
    private static Organizer organizerInstance = new Organizer();

    public static void main(String[] args) throws Exception {
        /*
        organizerInstance.loadList();
        analyzer.start(organizerInstance);
        */

        System.out.println("Hello!");
    }
}

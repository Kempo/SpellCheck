package com.kempo.spellcheck;

import com.kempo.spellcheck.application.Analyzer;
import com.kempo.spellcheck.application.Organizer;

import static spark.Spark.*;

public class Server {
    /**
     * perhaps not the best way to go about initializing objects
     */
    public final static Organizer organizer = new Organizer();
    public final static Analyzer analyzer = new Analyzer();

    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        staticFileLocation("/public");
        get("/ping", (req, res) -> "pong!");

        post("/word", (req, res) -> {
            String word = req.queryParams("inputData");
            System.out.println("analyzing request with: " + word);
            organizer.loadList();
            analyzer.setInput(word);
            analyzer.start(organizer);
            String result = analyzer.getPredictedInput();
            return result;
        });
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}

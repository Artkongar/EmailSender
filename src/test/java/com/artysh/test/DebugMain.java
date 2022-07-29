package com.artysh.test;

import com.mailsender.data.joke.Joke;
import com.mailsender.data.joke.RussianJoke;
import com.mailsender.service.JokeGenerator;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class DebugMain {

    public static void main(String[] args) throws IOException, ParseException {
        RussianJoke t1 = new RussianJoke(5);
        RussianJoke t2 = new RussianJoke(5);
        t2.setJokeText("a");
        RussianJoke t3 = new RussianJoke(5);
        t3.setJokeText("b");
        Joke[] jokes = new Joke[]{t1, t2, t3};

        Joke j = Arrays.stream(jokes).max(new Comparator<Joke>() {
            @Override
            public int compare(Joke o1, Joke o2) {
                if (o1.getCellsNumber() > o2.getCellsNumber()){
                    return 1;
                } else if (o1.getCellsNumber() < o2.getCellsNumber()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }).get();

    }

}

package com.mailsender.data.joke;

public class RussianJoke extends Joke {

    private String jokeText;

    public RussianJoke(int cellsNumber) {
        super(cellsNumber);
    }

    @Override
    public String getHTMLRows() {
        StringBuffer bf = new StringBuffer();

        bf.append("" +
                "<tr>\n" +
                "    <td class=\"lineUp\" colspan=\"3\">\n" +
                "        <div style=\"color: #076b91;\">\n" +
                jokeText +
                "            \n" +
                "        </div>\n" +
                "    </td>\n" +
                "</tr>");

        return bf.toString();
    }

    public String getJokeText() {
        return jokeText;
    }

    public void setJokeText(String jokeText) {
        this.jokeText = jokeText;
    }
}

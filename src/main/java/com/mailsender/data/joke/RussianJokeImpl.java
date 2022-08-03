package com.mailsender.data.joke;

public class RussianJokeImpl extends JokeImpl {

    private String jokeText;

    @Override
    public String getHTMLRows() {
        StringBuffer bf = new StringBuffer();

        bf.append("" +
                "<table>" +
                "<tr>\n" +
                "    <td>\n" +
                "        <div style=\"color: #076b91;\">\n" +
                jokeText +
                "            \n" +
                "        </div>\n" +
                "    </td>\n" +
                "</tr>" +
                "</table>");

        return bf.toString();
    }

    public String getJokeText() {
        return jokeText;
    }

    public void setJokeText(String jokeText) {
        this.jokeText = jokeText;
    }
}

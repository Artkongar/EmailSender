package com.mailsender.utils.service_handler.joke;

import com.mailsender.utils.service_handler.ServiceContent;

public class TranslatedJokeImpl implements ServiceContent {

    private boolean hasPunchline = true;

    private String setup;
    private String punchline;
    private String subject;

    private String setupRu;
    private String punchlineRu;
    private String subjectRu;

    private String joke;
    private String jokeRu;

    public void setHasPunchline(boolean hasPunchline) {
        this.hasPunchline = hasPunchline;
    }

    @Override
    public String getHTMLRows() {
        StringBuffer htmlRows = new StringBuffer();
        htmlRows.append("" +
                "<div>" +
                "   <b>" +
                "       Шуточки" +
                "   </b>" +
                "</div>");
        htmlRows.append("<table>");
        if (hasPunchline) {
            htmlRows.append("" +
                    "<tr>\n" +
                    "        <td/>\n" +
                    "        <td>" +
                    "            <div style=\"color: #076b91; font-size: 80%\">\n" +
                    subject +
                    "                \n" +
                    "            </div>\n" +
                    "        </td>\n" +
                    "        <td>" +
                    "            <div style=\"color: #076b91; font-size: 80%\">\n" +
                    subjectRu +
                    "                \n" +
                    "            </div>\n" +
                    "        </td>\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <td>" +
                    "            <div style=\"color: #076b91; font-size: 80%\">\n" +
                    "                Затравка\n" +
                    "            </div>\n" +
                    "        </td>\n" +
                    "        <td>" +
                    "            <div style=\"color: #1896FF;\">\n" +
                    setup +
                    "                \n" +
                    "            </div>\n" +
                    "        </td>\n" +
                    "        <td>" +
                    "            <div style=\"color: #DA58A3;\">\n" +
                    setupRu +
                    "                \n" +
                    "            </div>\n" +
                    "        </td>\n" +
                    "\n" +
                    "    </tr>\n" +
                    "    <tr>\n" +
                    "        <td>" +
                    "            <div style=\"color: #076b91; font-size: 80%\">\n" +
                    "                Панчлайн\n" +
                    "            </div>\n" +
                    "        </td>\n" +
                    "        <td>" +
                    "            <div style=\"color: #58DAC8;\">\n" +
                    punchline +
                    "                \n" +
                    "            </div>\n" +
                    "        </td>\n" +
                    "        <td>" +
                    "            <div style=\"color: #58DAC8;\">\n" +
                    punchlineRu +
                    "                \n" +
                    "            </div>\n" +
                    "        </td>\n" +
                    "</tr>"
            );
        } else {
            htmlRows.append("" +
                    "<tr>\n" +
                    "    <td" +
                    "        <div style=\"color: #076b91;\">\n" +
                    joke +
                    "            \n" +
                    "        </div>\n" +
                    "    </td>\n" +
                    "    <td" +
                    "        <div style=\"color: #076b91;\">\n" +
                    jokeRu +
                    "            \n" +
                    "        </div>\n" +
                    "    </td>\n" +
                    "</tr>");
        }
        htmlRows.append("</table>");
        return htmlRows.toString();
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setSubjectRu(String subjectRu) {
        this.subjectRu = subjectRu;
    }

    public void setJoke(String joke) {
        this.joke = joke;
    }

    public void setJokeRu(String jokeRu) {
        this.jokeRu = jokeRu;
    }

    public void setSetup(String setup) {
        this.setup = setup;
    }

    public void setPunchline(String punchline) {
        this.punchline = punchline;
    }

    public void setSetupRu(String setupRu) {
        this.setupRu = setupRu;
    }

    public void setPunchlineRu(String punchlineRu) {
        this.punchlineRu = punchlineRu;
    }

    public String getSetup() {
        return setup;
    }

    public String getPunchline() {
        return punchline;
    }

    public String getSubject() {
        return subject;
    }

    public String getSetupRu() {
        return setupRu;
    }

    public String getPunchlineRu() {
        return punchlineRu;
    }

    public String getSubjectRu() {
        return subjectRu;
    }

    public String getJoke() {
        return joke;
    }

    public String getJokeRu() {
        return jokeRu;
    }

    public boolean isHasPunchline() {
        return hasPunchline;
    }
}

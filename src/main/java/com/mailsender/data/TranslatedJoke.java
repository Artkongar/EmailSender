package com.mailsender.data;

public class TranslatedJoke {

    private boolean hasPunchline = true;

    private String setup;
    private String punchline;
    private String subject;

    private String setupRu;
    private String punchlineRu;
    private String subjectRu;

    private String joke;
    private String jokeRu;

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

    public void setHasPunchline(boolean hasPunchline) {
        this.hasPunchline = hasPunchline;
    }
}

package com.mailsender.data.joke;

public abstract class Joke {

    private Integer cellsNumber;

    public Joke(int cellsNumber){
        this.cellsNumber = cellsNumber;
    }

    public abstract String getHTMLRows();

    public Integer getCellsNumber() {
        return cellsNumber;
    }

    public void setCellsNumber(int cellsNumber) {
        this.cellsNumber = cellsNumber;
    }
}

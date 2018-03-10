package client.models;

import java.io.Serializable;

public class Level implements Serializable {
    private String field;
    private int wordsToSolve;
    private int number;

    public int getNumber() {
        return number;
    }

    public Level(String field, int wordsToSolve, int number) {
        this.field = field;
        this.wordsToSolve = wordsToSolve;
        this.number = number;

    }

    public void solveWord() {
        wordsToSolve--;
    }

    public String getField() {
        return field;
    }

    public int getWordsToSolve() {
        return wordsToSolve;
    }
}

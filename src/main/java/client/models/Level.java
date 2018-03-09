package client.models;

public class Level {
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

package client.models;

public class Level {
    private String field;
    private int wordsToSolve;

    public Level(String field, int wordsToSolve) {
        this.field = field;
        this.wordsToSolve = wordsToSolve;
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

package models;

public class Level {
    String field;
    int wordsToSolve;

    public Level(String field, int wordsToSolve) {
        this.field = field;
        this.wordsToSolve = wordsToSolve;
    }

    public void solve() {
        wordsToSolve--;
    }

    public String getField() {
        return field;
    }
}

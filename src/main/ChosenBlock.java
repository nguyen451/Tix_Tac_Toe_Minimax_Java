package main;

public class ChosenBlock {
    public int col;
    public int row;
    
    public ChosenBlock(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public boolean isInBoard(char[][] stboard) {
        return row < stboard.length && col < stboard[0].length;
    }

    public boolean isValid(char[][] stboard) {
        return isInBoard(stboard) && stboard[row][col] == '_';
    }

}

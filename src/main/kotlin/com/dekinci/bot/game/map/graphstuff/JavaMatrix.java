package com.dekinci.bot.game.map.graphstuff;

public class JavaMatrix {
    private int[][] matrix;

    public JavaMatrix(int size) {
        matrix = new int[size][size];
    }

    public int get(int x, int y) {
        return matrix[x][y];
    }

    public void set(int x, int y, int value) {
        matrix[x][y] = value;
    }

    public int size() {
        return matrix.length;
    }
}

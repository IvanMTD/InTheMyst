package ru.phoenix.core.loader.sprite;

public class TextureConfig {
    private String path;
    private int row;
    private int column;

    public TextureConfig(String path, int row, int column) {
        this.path = path;
        this.row = row;
        this.column = column;
    }

    public String getPath() {
        return path;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}

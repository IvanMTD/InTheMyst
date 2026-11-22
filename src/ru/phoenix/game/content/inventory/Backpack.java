package ru.phoenix.game.content.inventory;

/**
 * Рюкзак персонажа — сетка 5×5 слотов.
 * Каждый слот может содержать Item или быть пустым (null).
 */
public class Backpack {
    public static final int GRID_SIZE = 5;

    private final Item[][] grid;

    public Backpack() {
        grid = new Item[GRID_SIZE][GRID_SIZE];
    }

    /** Положить предмет в первый свободный слот. Возвращает true если успешно. */
    public boolean addItem(Item item) {
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                if (grid[x][y] == null) {
                    grid[x][y] = item;
                    return true;
                }
            }
        }
        return false; // рюкзак полон
    }

    /** Положить предмет в конкретный слот. Возвращает старый предмет (или null). */
    public Item addItem(Item item, int x, int y) {
        if (x < 0 || x >= GRID_SIZE || y < 0 || y >= GRID_SIZE) return null;
        Item old = grid[x][y];
        grid[x][y] = item;
        return old;
    }

    /** Убрать предмет из слота */
    public Item removeItem(int x, int y) {
        if (x < 0 || x >= GRID_SIZE || y < 0 || y >= GRID_SIZE) return null;
        Item item = grid[x][y];
        grid[x][y] = null;
        return item;
    }

    /** Получить предмет из слота */
    public Item getItem(int x, int y) {
        if (x < 0 || x >= GRID_SIZE || y < 0 || y >= GRID_SIZE) return null;
        return grid[x][y];
    }

    /** Заменить предмет в слоте, вернуть старый */
    public Item swapItem(int x, int y, Item newItem) {
        if (x < 0 || x >= GRID_SIZE || y < 0 || y >= GRID_SIZE) return null;
        Item old = grid[x][y];
        grid[x][y] = newItem;
        return old;
    }

    /** Количество занятых слотов */
    public int getOccupiedCount() {
        int count = 0;
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                if (grid[x][y] != null) count++;
            }
        }
        return count;
    }

    /** Рюкзак полон? */
    public boolean isFull() {
        return getOccupiedCount() >= GRID_SIZE * GRID_SIZE;
    }

    public Item[][] getGrid() { return grid; }
    public int getGridSize() { return GRID_SIZE; }
}

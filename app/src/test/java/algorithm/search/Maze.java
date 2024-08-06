package algorithm.search;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Maze {
    private final Cell[][] grid;// 迷宫表
    private final int rows, columns;// 行数 列数
    public final CellLocation startLocation, goalLocation;// 起点 终点

    public static class CellLocation {
        int row, column;

        CellLocation(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CellLocation that)) return false;
            return row == that.row && column == that.column;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, column);
        }
    }

    public enum Cell {
        EMPTY(" "),
        BLOCKED("X"),// 障碍
        START("S"),
        GOAL("G"),
        PATH("*");

        private final String code;

        Cell(String c) {
            code = c;
        }

        @NonNull
        @Override
        public String toString() {
            return code;
        }
    }

    public Maze(int rows, int columns, float percentBlocked) {
        this.rows = rows;
        this.columns = columns;
        grid = new Cell[rows][columns];
        for (Cell[] cells : grid) {
            Arrays.fill(cells, Cell.EMPTY);
        }
        generateRandomBlock(percentBlocked);
        grid[0][0] = Cell.START;
        grid[rows - 1][columns - 1] = Cell.GOAL;

        startLocation = new CellLocation(0, 0); // 起点
        goalLocation = new CellLocation(rows - 1, columns - 1);// 终点
    }

    // 生成随机障碍
    private void generateRandomBlock(float percentBlocked) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (Math.random() < percentBlocked) {
                    grid[i][j] = Cell.BLOCKED;
                }
            }
        }
    }

    // 判断是否到达终点
    public boolean goalReached(CellLocation location) {
        return location.equals(goalLocation);
    }

    // 获取可移动方向
    public List<CellLocation> nextCanMove(CellLocation location) {
        List<CellLocation> list = List.of(
                new CellLocation(location.row - 1, location.column),
                new CellLocation(location.row + 1, location.column),
                new CellLocation(location.row, location.column - 1),
                new CellLocation(location.row, location.column + 1)
        );
        return list.stream().filter(this::isValid).toList();
    }

    // 判断是否可移动
    private boolean isValid(CellLocation cellLocation) {
        return cellLocation.row >= 0 && cellLocation.row < rows
                && cellLocation.column >= 0 && cellLocation.column < columns
                && grid[cellLocation.row][cellLocation.column] != Cell.BLOCKED;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                builder.append(grid[i][j]);
            }
            builder.append(System.lineSeparator());
        }

        return builder.toString();
    }


    public void mark(List<CellLocation> path) {
        for (CellLocation ml : path) {
            grid[ml.row][ml.column] = Cell.PATH;
        }
        grid[startLocation.row][startLocation.column] = Cell.START;
        grid[goalLocation.row][goalLocation.column] = Cell.GOAL;
    }

    public void clear(List<CellLocation> path) {
        for (CellLocation ml : path) {
            grid[ml.row][ml.column] = Cell.EMPTY;
        }
        grid[startLocation.row][startLocation.column] = Cell.START;
        grid[goalLocation.row][goalLocation.column] = Cell.GOAL;
    }

    // 曼哈顿距离比勾股定理更适合城市距离判断，因为城市无法点对点直线方式通行
    public double manhattanDistance(CellLocation ml) {
        int xDist = Math.abs(ml.column - goalLocation.column);
        int yDist = Math.abs(ml.row - goalLocation.row);
        return (xDist + yDist);
    }
}

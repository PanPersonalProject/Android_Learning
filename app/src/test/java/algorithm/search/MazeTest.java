package algorithm.search;

import org.junit.Test;

import java.util.List;

public class MazeTest {
    @Test
    public void test() {
        Maze m = new Maze(10, 10, 0.2f);

        GenericSearch.Node<Maze.CellLocation> solution1 = GenericSearch.dfs(m.startLocation, m::goalReached, m::nextCanMove);
        if (solution1 == null) {
            System.out.println("No solution found using depth-first search!");
        } else {
            System.out.println("dfs:");
            printPath(solution1, m);
        }


        GenericSearch.Node<Maze.CellLocation> solution2 = GenericSearch.bfs(m.startLocation, m::goalReached, m::nextCanMove);
        if (solution1 == null) {
            System.out.println("No solution found using breadth-first search!");
        } else {
            System.out.println("bfs:");
            printPath(solution2, m);
        }
    }

    private static void printPath(GenericSearch.Node<Maze.CellLocation> solution1, Maze m) {
        List<Maze.CellLocation> path1 = GenericSearch.nodeToPath(solution1);
        m.mark(path1);
        System.out.println(m);
        m.clear(path1);
    }
}

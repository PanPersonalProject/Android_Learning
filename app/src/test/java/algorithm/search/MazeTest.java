package algorithm.search;

import org.junit.Test;

import java.util.List;
/*
dfs:
SX  X  X
**********
    X    *
    XX X**
  X******
****
*   X X
***X******
  ***XX  *
X   X   XG

bfs:
SX  X  X
*
*   X
*   XX X
* X
******
    X*X
   X ***
     XX***
X   X   XG

A*:
SX  X  X
*
****X
   *XX X
  X***
     ****
    X X *
   X    *
     XX **
X   X   XG
*/
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

        GenericSearch.Node<Maze.CellLocation> solution3 = GenericSearch.aStar(m.startLocation, m::goalReached, m::nextCanMove, m::manhattanDistance);
        if (solution3 == null) {
            System.out.println("No solution found using A*!");
        } else {
            System.out.println("A*:");
            printPath(solution3, m);
        }
    }

    private static void printPath(GenericSearch.Node<Maze.CellLocation> solution1, Maze m) {
        List<Maze.CellLocation> path1 = GenericSearch.nodeToPath(solution1);
        m.mark(path1);
        System.out.println(m);
        m.clear(path1);
    }
}

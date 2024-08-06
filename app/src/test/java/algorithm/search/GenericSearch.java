package algorithm.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

public class GenericSearch {


    // assumes *list* is already sorted
    public static <T extends Comparable<T>> boolean binaryContains(List<T> list, T key) {
        int low = 0;
        int high = list.size() - 1;
        while (low <= high) { // while there is still a search space
            int middle = (low + high) / 2;
            int comparison = list.get(middle).compareTo(key);
            if (comparison < 0) { // middle codon is less than key
                low = middle + 1;
            } else if (comparison > 0) { // middle codon is greater than key
                high = middle - 1;
            } else { // middle codon is equal to key
                return true;
            }
        }
        return false;
    }

    public static class Node<T> implements Comparable<Node<T>> {
        final T state;
        Node<T> parent;
        double cost;//指在到达当前状态时，已经走过了多少个单元格
        double heuristic;//启发式信息(heuristic)是解决问题的一种直觉，估算距离最终目的地的成本

        // for dfs and bfs we won't use cost and heuristic
        Node(T state, Node<T> parent) {
            this.state = state;
            this.parent = parent;
        }

        // for astar we will use cost and heuristic
        //到达任一状态所需的总成本为f(n)，它是g(n)与h(n)之和，即f(n)=g(n)+h(n)。
        Node(T state, Node<T> parent, double cost, double heuristic) {
            this.state = state;
            this.parent = parent;
            this.cost = cost;
            this.heuristic = heuristic;
        }

        @Override
        public int compareTo(Node<T> other) {
            Double mine = cost + heuristic;
            Double theirs = other.cost + other.heuristic;
            return mine.compareTo(theirs);
        }
    }

    /**
     * 深度优先搜索（Depth-First Search, DFS）顾名思义就是尽可能地往路径深处进行搜索，
     * 如果遇到了死胡同，则回溯到最后一次做出决策的那个位置。
     *
     * @param initial    初始状态
     * @param goalTest   一个 Predicate 函数，用于判断当前状态是否为目标状态
     * @param successors 一个 Function 函数，用于获取给定状态的所有后继状态
     * @return 如果找到目标状态，则返回对应的 Node 对象；否则返回 null
     */
    public static <T> Node<T> dfs(T initial, Predicate<T> goalTest, Function<T, List<T>> successors) {
        // frontier 是我们尚未探索的地方
        Stack<Node<T>> frontier = new Stack<>();
        frontier.push(new Node<>(initial, null));
        // explored 是我们已经探索过的地方
        Set<T> explored = new HashSet<>();
        explored.add(initial);

        // 只要还有地方可探索就继续进行
        while (!frontier.isEmpty()) {
            Node<T> currentNode = frontier.pop();
            T currentState = currentNode.state;
            // 如果找到了目标，搜索完成
            if (goalTest.test(currentState)) {
                return currentNode;
            }
            // 检查下一步可以去哪里，同时确保这些地方还未被探索过
            for (T child : successors.apply(currentState)) {
                if (explored.contains(child)) {
                    continue; // 跳过已经探索过的孩子节点
                }
                explored.add(child);
                frontier.push(new Node<>(child, currentNode));
            }
        }
        return null; // 遍历了所有可能的路径，但从未找到目标
    }


    /**
     * 它在每次迭代中都会从起始状态开始由近至远地搜索每一层级的所有节点。在某些特定的问题中，深度优先搜索可能比广度优先搜索更快地找到解，反之亦然。
     * <p>
     * 因此，需要在快速求解和找到最短路径（如果存在最短路径）之间进行权衡，然后决定最终选择哪种搜索方式
     * <p>
     * 广度优先搜索算法居然与深度优先搜索算法出奇的一致，只是frontier从栈变成了队列
     */
    public static <T> Node<T> bfs(T initial, Predicate<T> goalTest, Function<T, List<T>> successors) {
        // frontier 是我们尚未探索的地方
        LinkedList<Node<T>> frontier = new LinkedList<>();
        frontier.push(new Node<>(initial, null));
        // explored 是我们已经探索过的地方
        Set<T> explored = new HashSet<>();
        explored.add(initial);

        // 只要还有地方可探索就继续进行
        while (!frontier.isEmpty()) {
            Node<T> currentNode = frontier.pop();
            T currentState = currentNode.state;
            // 如果找到了目标，搜索完成
            if (goalTest.test(currentState)) {
                return currentNode;
            }
            // 检查下一步可以去哪里，同时确保这些地方还未被探索过
            for (T child : successors.apply(currentState)) {
                if (explored.contains(child)) {
                    continue; // 跳过已经探索过的孩子节点
                }
                explored.add(child);
                frontier.offer(new Node<>(child, currentNode));
            }
        }
        return null; // 遍历了所有可能的路径，但从未找到目标
    }

    public static <T> Node<T> aStar(T initial, Predicate<T> goalTest,
                                    Function<T, List<T>> successors, ToDoubleFunction<T> heuristic) {
        // frontier 是我们尚未探索的地方，PriorityQueue是优先队列，优先级最高的数据项是f(n)最小的那个
        PriorityQueue<Node<T>> frontier = new PriorityQueue<>();
        frontier.offer(new Node<>(initial, null, 0.0, heuristic.applyAsDouble(initial)));
        // explored 是我们已经探索过的地方
        Map<T, Double> explored = new HashMap<>();
        explored.put(initial, 0.0);
        // 只要还有地方可以探索就继续进行
        while (!frontier.isEmpty()) {
            Node<T> currentNode = frontier.poll();
            T currentState = currentNode.state;
            // 如果找到了目标，任务完成
            if (goalTest.test(currentState)) {
                return currentNode;
            }
            // 检查下一步可以去哪些地方并且这些地方还未被探索过
            for (T child : successors.apply(currentState)) {
                // 这里的 1 假定是在网格上移动，对于更复杂的应用需要一个代价函数
                double newCost = currentNode.cost + 1;
                //explored.get(child)这个条件表示，我们找到了到达 child 节点的一条更短的路径
                if (!explored.containsKey(child) || explored.get(child) > newCost) {
                    explored.put(child, newCost);
                    frontier.offer(new Node<>(child, currentNode, newCost, heuristic.applyAsDouble(child)));
                }
            }
        }

        return null; // 遍历了所有可能而未找到目标
    }


    public static <T> List<T> nodeToPath(Node<T> node) {
        List<T> path = new ArrayList<>();
        path.add(node.state);
        // 从终点反向追踪到起点
        while (node.parent != null) {
            node = node.parent;
            path.add(0, node.state); // 添加到列表的前端
        }
        return path;
    }


    public static void main(String[] args) {
        System.out.println(binaryContains(List.of("a", "d", "e", "f", "z"), "f")); // true
        System.out.println(binaryContains(List.of("john", "mark", "ronald", "sarah"), "sheila")); // false
    }

}

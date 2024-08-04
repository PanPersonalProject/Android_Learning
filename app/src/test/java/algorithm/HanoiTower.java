package algorithm;

import java.util.Stack;

public class HanoiTower {
    Stack<Character> stackA = new Stack<>();
    Stack<Character> stackB = new Stack<>();
    Stack<Character> stackC = new Stack<>();
    int deep;//层数

    public HanoiTower(int deep) {
        this.deep = deep;
        for (int i = 0; i < deep; i++) {
            //从字符a开始
            stackA.push((char) (97 + i));
        }
    }

    public void start() {
        move(stackA, stackB, stackC, deep);
    }


    /**
     * 1.将上面的n-1个圆盘从A塔移到B塔（暂存塔）使用C塔作为中转塔
     * <br>
     * 2.将最底层的圆盘从A塔移到C塔
     * <br>
     * 3.将n-1个圆盘从B塔移到C塔，使用A塔作为中转塔
     * <p>
     * 在编写汉诺塔解决方案时，我们不必了解将多个圆盘从A塔移到C塔所需的每一步。
     * <br>
     * 在了解了移动任意数量圆盘的通用递归算法并完成编码后，剩下的工作就交给计算机去完成吧。
     * <br>
     * 这就是构思递归解法的威力：可以用抽象的方式思考解决方案，而无须在脑海中考虑每一个单独的步骤。
     * <p>
     * move()方法的执行次数会呈指数级增长，因此无法解决64个圆盘的情况
     */
    private void move(Stack<Character> beginStack, Stack<Character> tempStack, Stack<Character> endStack, int deep) {
        if (deep == 1) {
            char disk = beginStack.pop();
            endStack.push(disk);
            System.out.println("将盘子 " + disk + " 从 " + getStackName(beginStack) + " 移动到 " + getStackName(endStack));
        } else {
            move(beginStack, endStack, tempStack, deep - 1);
            move(beginStack, tempStack, endStack, 1);
            move(tempStack, beginStack, endStack, deep - 1);
        }
    }

    private String getStackName(Stack<Character> stack) {
        if (stack == stackA) return "A柱";
        if (stack == stackB) return "B柱";
        return "C柱";
    }
}

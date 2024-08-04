package algorithm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.HashMap;

public class FibonacciTest {
    HashMap<Integer, Long> cache = new HashMap<>();

    // 斐波那契递归实现
    public Long fibonacciRecursion(int n) {
        if (n < 2) return (long) n;
        if (cache.containsKey(n)) return cache.get(n);
        Long value = fibonacciRecursion(n - 1) + fibonacciRecursion(n - 2);
        cache.put(n, value);
        return value;
    }

    //斐波那契正向实现
    public Long fibonacci(int n) {
        if (n < 2) return (long) n;
        long secondLast = 0, last = 1;
        for (int i = 2; i < n; i++) {
            long tempSecondLast = secondLast;
            secondLast = last;
            last = tempSecondLast + last;
        }
        return secondLast + last;
    }


    // 打印斐波那契所有值
    public void printFibonacciAllValue(int n) {
        if (n == 0) {
            System.out.println(0);
            return;
        }
        long secondLast = 0, last = 1;
        System.out.print(secondLast + ",");
        System.out.print(last + ",");
        for (int i = 2; i <= n; i++) {
            long tempSecondLast = secondLast;
            secondLast = last;
            last = tempSecondLast + last;
            System.out.print(last + ",");
        }
    }


    @Test
    public void testFibonacci() {
        int n = 40;
        Long fibonacciRecursion = fibonacciRecursion(n);
        System.out.println("fibonacciRecursion result=" + fibonacciRecursion);

        Long fibonacci = fibonacci(n);
        System.out.println("fibonacci result=" + fibonacci);
        printFibonacciAllValue(n);

        assertEquals(fibonacciRecursion, fibonacci);

    }
}

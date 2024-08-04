package algorithm;

import org.junit.Test;

public class HanoiTowerTest {


    @Test
    public void test() {
        HanoiTower hanoiTower = new HanoiTower(4);
        hanoiTower.start();
        System.out.println(hanoiTower.stackA);
        System.out.println(hanoiTower.stackB);
        System.out.println(hanoiTower.stackC);
    }
}

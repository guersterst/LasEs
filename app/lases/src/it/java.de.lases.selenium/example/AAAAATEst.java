package example;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@Order(3)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class AAAAATEst {

    @Test
    void test1() {
        System.out.println("a");
    }

    @Test
    void test2() {
        System.out.println("b");
    }
}

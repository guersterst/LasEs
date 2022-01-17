import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@Order(2)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class SecondTestClass {

    @Test
    void test1() {
        System.out.println("3");
    }

    @Test
    void test2() {
        System.out.println("4");
    }
}

package de.lases.selenium.stress;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Stress {

    public static final int N = 1;

    static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(N);

    public static void main(String... args) {
//        for (int i = 0; i < N; i++) {
//            executor.submit(UserRegistersClicksRandomlyAndDeletesHimself::new);
//        }
        UserRegistersClicksRandomlyAndDeletesHimself callable = new UserRegistersClicksRandomlyAndDeletesHimself();

        callable.call();
    }

}

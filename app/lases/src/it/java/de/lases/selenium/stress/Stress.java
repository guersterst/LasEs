package de.lases.selenium.stress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.*;

public class Stress {

    public static final int USER_CREATION = 3;

    public static final int FORUM_SUBMISSION = 2;

    public static final int THREADS = FORUM_SUBMISSION + USER_CREATION;

    public static final String URL = "http://ds9.fim.uni-passau.de:8002/lases/";

    public static void main(String... args) throws ExecutionException, InterruptedException, FileNotFoundException {

        Deque<List<ResponseTimeEntry>> futures = new ConcurrentLinkedDeque<>();

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < USER_CREATION; i++) {
            Thread thread = new Thread(() -> {
                UserRegistersClicksRandomlyAndDeletesHimself u = new UserRegistersClicksRandomlyAndDeletesHimself();
                futures.push(u.call());
            });
            threads.add(thread);
            thread.start();
        }
        for (int i = 0; i < FORUM_SUBMISSION; i++) {
            Thread thread = new Thread(() -> {
                UserCreatesForumAndSubmission u = new UserCreatesForumAndSubmission();
                futures.push(u.call());
            });
            threads.add(thread);
            thread.start();
        }

        for (Thread thread: threads) {
            thread.join();
        }

        Map<String, List<Long>> responseTimes = new HashMap<>();

        while (!futures.isEmpty()) {
            List<ResponseTimeEntry> responseTimeEntries = futures.pop();
            for (ResponseTimeEntry responseTimeEntry: responseTimeEntries) {
                String actionName = responseTimeEntry.actionName();
                Long responseTime = responseTimeEntry.responseTime();
                responseTimes.putIfAbsent(actionName, new ArrayList<>());
                responseTimes.get(actionName).add(responseTime);
            }
        }
        System.out.println(responseTimes);

        File csvOutput = new File("out.csv");
        try (PrintWriter pw = new PrintWriter(csvOutput)) {
            for (String headerString: responseTimes.keySet()) {
                StringBuilder line = new StringBuilder();
                line.append(headerString);
                for (Long time: responseTimes.get(headerString)) {
                    line.append(", ").append(time);
                }
                pw.println(line);
            }
        }

    }

}

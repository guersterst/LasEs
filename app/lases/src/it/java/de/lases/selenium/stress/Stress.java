package de.lases.selenium.stress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class Stress {

    public static final int USER_CREATION = 30;

    public static final int FORUM_SUBMISSION = 20;

    public static final int THREADS = FORUM_SUBMISSION + USER_CREATION;

    public static final String URL = "http://ds9.fim.uni-passau.de:8002/lases/";

    static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREADS);

    public static void main(String... args) throws ExecutionException, InterruptedException, FileNotFoundException {

        Deque<Future<List<ResponseTimeEntry>>> futures = new ArrayDeque<>();

        for (int i = 0; i < USER_CREATION; i++) {
            futures.push(executor.submit(new UserRegistersClicksRandomlyAndDeletesHimself()));
        }
        for (int i = 0; i < FORUM_SUBMISSION; i++) {
            futures.push(executor.submit(new UserCreatesForumAndSubmission()));
        }

        Map<String, List<Long>> responseTimes = new HashMap<>();

        for (int i = 0; i < THREADS; i++) {
            executor.
            List<ResponseTimeEntry> responseTimeEntries = futures.pop().get();
            for (ResponseTimeEntry responseTimeEntry: responseTimeEntries) {
                String actionName = responseTimeEntry.actionName();
                Long responseTime = responseTimeEntry.responseTime();
                responseTimes.putIfAbsent(actionName, new ArrayList<>());
                responseTimes.get(actionName).add(responseTime);
            }
        }
        System.out.println(responseTimes);
        executor.shutdown();

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

package com.example.jtcp.tests;

import com.example.jtcp.userdetails.InMemoryUserStore;
import com.example.jtcp.userdetails.User;
import com.example.jtcp.userdetails.UserStore;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class UserStoreConcurrencyTest {

    private static final int THREAD_COUNT = 100;
    private static final int TEST_ROUNDS = 100;

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Starting UserStore concurrency test...");
        System.out.println("Threads per round: " + THREAD_COUNT);
        System.out.println("Rounds: " + TEST_ROUNDS);

        for (int round = 1; round <= TEST_ROUNDS; round++) {

            runTestRound(round);

        }

        System.out.println();
        System.out.println("=================================");
        System.out.println("ALL CONCURRENCY TESTS PASSED");
        System.out.println("=================================");
    }

    private static void runTestRound(int round)
            throws InterruptedException {

        UserStore userStore = new InMemoryUserStore();

        ExecutorService executor =
                Executors.newFixedThreadPool(THREAD_COUNT);

        CountDownLatch ready =
                new CountDownLatch(THREAD_COUNT);

        CountDownLatch start =
                new CountDownLatch(1);

        AtomicInteger successfulCreations =
                new AtomicInteger();

        for (int i = 0; i < THREAD_COUNT; i++) {

            executor.submit(() -> {

                ready.countDown();

                try {

                    start.await();

                    User user =
                            new User("alice", "password");

                    if (userStore.createIfAbsent(user)) {
                        successfulCreations.incrementAndGet();
                    }

                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();

                }
            });
        }

        // Make sure every worker has reached the starting line.
        ready.await();

        // Release all workers.
        start.countDown();

        executor.shutdown();

        boolean finished =
                executor.awaitTermination(
                        10,
                        TimeUnit.SECONDS
                );

        if (!finished) {

            executor.shutdownNow();

            throw new AssertionError(
                    "Round " + round +
                            " did not finish within 10 seconds"
            );
        }

        int successful =
                successfulCreations.get();

        if (successful != 1) {

            throw new AssertionError(
                    "Round " + round +
                            " failed. Expected exactly 1 successful creation, but got "
                            + successful
            );
        }

        User storedUser =
                userStore.find("alice");

        if (storedUser == null) {

            throw new AssertionError(
                    "Round " + round +
                            " failed. Alice was not stored."
            );
        }

        if (!"alice".equals(storedUser.getUsername())) {

            throw new AssertionError(
                    "Round " + round +
                            " failed. Incorrect user stored."
            );
        }

        System.out.println(
                "Round " + round + " PASSED"
        );
    }
}
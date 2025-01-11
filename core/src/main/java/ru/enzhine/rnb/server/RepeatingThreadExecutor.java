package ru.enzhine.rnb.server;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;

public class RepeatingThreadExecutor extends Thread implements RepeatingExecutable {

    private final Runnable runnable;
    @Getter
    private final long tickMillis;
    private long lastTickTimeMs;
    private long ticksCount;
    private boolean executing;

    public RepeatingThreadExecutor(Runnable runnable, int tps) {
        this.runnable = runnable;
        this.tickMillis = 1_000 / tps;

        this.lastTickTimeMs = 0L;
        this.executing = false;
    }

    @Override
    public void run() {
        executing = true;
        while (isAlive() && !isInterrupted() && executing) {
            ++ticksCount;
            var at = Instant.now();
            runnable.run();
            lastTickTimeMs = Duration.between(at, Instant.now()).toMillis();

            if (lastTickTimeMs < tickMillis) {
                try {
                    sleep(tickMillis - lastTickTimeMs);
                } catch (InterruptedException ignored) {
                    executing = false;
                }
            }
        }
    }

    @Override
    public long getExecLastMillis() {
        return lastTickTimeMs;
    }

    @Override
    public long getExecCount() {
        return ticksCount;
    }

    @Override
    public boolean isExecuting() {
        return executing;
    }

    @Override
    public void execute() {
        this.start();
    }

    @Override
    public void cancel() {
        executing = false;
    }
}

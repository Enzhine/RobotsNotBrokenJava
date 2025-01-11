package ru.enzhine.rnb.server;

public interface RepeatingExecutable {
    long getExecLastMillis();

    long getExecCount();

    boolean isExecuting();

    void execute();

    void cancel();
}

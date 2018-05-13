package org.hallock.dota.util;

import java.util.LinkedList;

public class Logger {
    private final LinkedList<LogOutput> outputs = new LinkedList<>();

    public void addOutput(LogOutput output) {
        synchronized (outputs) {
            outputs.add(output);
        }
    }

    private LinkedList<LogOutput> getLoggers() {
        synchronized (outputs) {
            return (LinkedList<LogOutput>) outputs.clone();
        }
    }

    public void log(String string) {
        for (LogOutput output : getLoggers()) {
            output.log(string);
        }
    }

    public void log(String string, Throwable exception) {
        for (LogOutput output : getLoggers()) {
            output.log(string, exception);
        }
    }

    public static interface LogOutput {
        void log(String string);
        void log(String string, Throwable ex);
    }

    private static final class ConsoleOutput implements LogOutput {
        public void log(String string) {
            System.out.println(string);
        }

        public void log(String string, Throwable exception) {
            System.out.println(string);
            exception.printStackTrace();
        }
    }

    public static Logger buildLogger() {
        Logger logger = new Logger();
        logger.addOutput(new ConsoleOutput());
        return logger;
    }
}

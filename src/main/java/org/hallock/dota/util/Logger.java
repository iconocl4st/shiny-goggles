package org.hallock.dota.util;

public class Logger {
    public void log(String string) {
        System.out.println(string);
    }

    public void log(String string, Throwable exception) {
        System.out.println(string);
        exception.printStackTrace();
    }
}

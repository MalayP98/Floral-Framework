package com.dummyframework.logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static final String RESET = "\u001B[0m";
    private static final String BLUE = "\u001B[34m";
    private static final String YELLOW = "\u001B[33m";
    private static final String PURPLE = "\u001B[35m";
    private static final String RED = "\u001B[31m";

    private Class<?> forClass;

    private Logger() {

    }

    public Logger(Class<?> clazz) {
        this.forClass = clazz;
    }

    private void log(String msg) {
        System.out.printf("%-1s\n", msg);
    }

    private void logDateTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.print(BLUE + dateTimeFormatter.format(now) + RESET + " ");
    }

    private void logClassName() {
        System.out.printf("%s %s %s", YELLOW, forClass.getName(), RESET);
    }

    public void info(String msg) {
        logDateTime();
        System.out.print(PURPLE + " INFO " + RESET + " ");
        logClassName();
        log(msg);
    }

    public void error(String msg) {
        logDateTime();
        System.out.printf("%s %-1s %s", RED, " ERROR ", RESET);
        logClassName();
        log(msg);
    }
}

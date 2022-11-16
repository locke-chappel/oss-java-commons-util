package com.github.lc.oss.commons.util;

import java.util.Scanner;
import java.util.function.Consumer;

public abstract class AbstractConsoleListener {
    protected static class Listener implements Runnable {
        private Consumer<String> processor;

        private boolean shouldRun = true;

        public Listener(Consumer<String> processor) {
            this.processor = processor;
        }

        @Override
        public void run() {
            try (Scanner in = new Scanner(System.in)) {
                System.out.print("$ ");
                while (this.shouldRun && in.hasNextLine()) {
                    String line = in.nextLine();
                    this.processor.accept(line.trim().toLowerCase());
                    if (this.shouldRun) {
                        System.out.print("$ ");
                    }
                }
            }
        }

        public void stop() {
            this.shouldRun = false;
        }
    }

    private Thread thread;
    private Listener listener;

    protected synchronized Listener getListener() {
        if (this.listener == null) {
            this.listener = new Listener(line -> this.process(line));
        }
        return this.listener;
    }

    protected synchronized Thread getThread() {
        if (this.thread == null) {
            this.thread = new Thread(this.getListener());
            this.thread.setDaemon(true);
        }
        return this.thread;
    }

    protected abstract void process(String line);

    public synchronized void start() {
        if (this.isRunning()) {
            return;
        }

        this.getThread().start();
    }

    public synchronized void stop() {
        this.getListener().stop();
    }

    public synchronized boolean isRunning() {
        return this.getThread().isAlive();
    }
}

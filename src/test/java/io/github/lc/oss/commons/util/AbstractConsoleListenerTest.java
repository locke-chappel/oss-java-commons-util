package io.github.lc.oss.commons.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AbstractConsoleListenerTest extends AbstractMockTest {
    private static class TestListener extends AbstractConsoleListener {
        public String lastLine;

        @Override
        protected void process(String line) {
            this.lastLine = line;

            if ("stop".equals(line)) {
                this.stop();
            }
        }
    }

    private InputStream stdin;

    @BeforeEach
    public void init() {
        this.stdin = System.in;
    }

    @AfterEach
    public void teardown() {
        System.setIn(this.stdin);
    }

    @Test
    public void test_commands() {
        try (PipedInputStream in = new PipedInputStream(); PipedOutputStream out = new PipedOutputStream(in)) {
            System.setIn(in);

            TestListener listener = new TestListener();
            Assertions.assertFalse(listener.isRunning());
            listener.start();
            this.waitUntil(() -> listener.isRunning());

            // this should be a no-op
            listener.start();
            this.waitUntil(() -> listener.isRunning());

            out.write("command\n".getBytes(StandardCharsets.UTF_8));
            this.waitUntil(() -> "command".equals(listener.lastLine), 5000);
            out.write("\n".getBytes(StandardCharsets.UTF_8));
            this.waitUntil(() -> "".equals(listener.lastLine), 5000);
            out.write(" \t \n".getBytes(StandardCharsets.UTF_8));
            this.waitUntil(() -> "".equals(listener.lastLine), 5000);
            out.write("stop\n".getBytes(StandardCharsets.UTF_8));
            this.waitUntil(() -> "stop".equals(listener.lastLine), 5000);

            this.waitUntil(() -> !listener.isRunning());
        } catch (IOException ex) {
            Assertions.fail("Unexpected exception");
        }
    }

    @Test
    public void test_streamClosed() {
        try (PipedInputStream in = new PipedInputStream(); PipedOutputStream out = new PipedOutputStream(in)) {
            System.setIn(in);

            TestListener listener = new TestListener();
            Assertions.assertFalse(listener.isRunning());
            listener.start();
            this.waitUntil(() -> listener.isRunning());

            // this should be a no-op
            listener.start();
            this.waitUntil(() -> listener.isRunning());

            out.write("command\n".getBytes(StandardCharsets.UTF_8));
            this.waitUntil(() -> "command".equals(listener.lastLine), 5000);
            out.close();

            this.waitUntil(() -> !listener.isRunning());
        } catch (IOException ex) {
            Assertions.fail("Unexpected exception");
        }
    }
}

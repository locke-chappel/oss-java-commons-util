package io.github.lc.oss.commons.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IoToolsTest extends AbstractMockTest {
    private static final byte[] CLASS_FILE_HEADER = new byte[] { (byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE };

    private static String tempDir = null;

    private String getTempDirPath() {
        if (IoToolsTest.tempDir == null) {
            IoToolsTest.tempDir = System.getProperty("java.io.tmpdir").replace("\\", "/");
            Assertions.assertTrue(Files.isDirectory(Paths.get(IoToolsTest.tempDir)), String.format("'%s' is not a directory", IoToolsTest.tempDir));
            if (!IoToolsTest.tempDir.endsWith("/")) {
                IoToolsTest.tempDir += "/";
            }
        }
        return IoToolsTest.tempDir;
    }

    private String getTempFilePath() {
        return this.getTempDirPath() + "iotools-test.dat";
    }

    private String getTempSubdirPath() {
        return this.getTempDirPath() + "iotools-test-dir";
    }

    @AfterEach
    public void cleanup() {
        File f = new File(this.getTempFilePath());
        f.delete();

        f = new File(this.getTempSubdirPath());
        f.delete();
    }

    @Test
    public void test_directIO() {
        String path = this.getTempFilePath();
        File f = new File(path);
        Assertions.assertFalse(f.exists());

        IoTools.writeToFile("data", path);

        Assertions.assertTrue(f.exists());

        byte[] read = IoTools.readFile(f);
        Assertions.assertEquals("data", new String(read, StandardCharsets.UTF_8));
    }

    @Test
    public void test_readAbsoluteFile_badPaths() {
        Assertions.assertNull(IoTools.readAbsoluteFile(null));
        Assertions.assertNull(IoTools.readAbsoluteFile(""));
        Assertions.assertNull(IoTools.readAbsoluteFile(" "));
        Assertions.assertNull(IoTools.readAbsoluteFile(" \t \r \n \t "));
        Assertions.assertNull(IoTools.readAbsoluteFile("bad-path"));
        Assertions.assertNull(IoTools.readAbsoluteFile("C:\\C:\\///root"));
    }

    @Test
    public void test_readAbsoluteFile_jarFile() {
        byte[] read = IoTools.readAbsoluteFile("IoTools.class");
        Assertions.assertNotNull(read);
        for (int i = 0; i < IoToolsTest.CLASS_FILE_HEADER.length; i++) {
            Assertions.assertEquals(IoToolsTest.CLASS_FILE_HEADER[i], read[i]);
        }
    }

    @Test
    public void test_getAbsoluteFilePath_badPaths() {
        Assertions.assertNull(IoTools.getAbsoluteFilePath(null));
        Assertions.assertNull(IoTools.getAbsoluteFilePath(""));
        Assertions.assertNull(IoTools.getAbsoluteFilePath(" "));
        Assertions.assertNull(IoTools.getAbsoluteFilePath(" \t \r \n \t "));
        Assertions.assertNull(IoTools.getAbsoluteFilePath("bad-path"));
        Assertions.assertNull(IoTools.getAbsoluteFilePath("C:\\C:\\///root"));
        Assertions.assertNull(IoTools.getAbsoluteFilePath("IoTools.class"));
    }

    @Test
    public void test_getAbsoluteFilePath() {
        Assertions.assertNotNull(IoTools.getAbsoluteFilePath("io/github/lc/oss/commons/util/IoTools.class"));
    }

    @Test
    public void test_listDir() {
        List<String> result = IoTools.listDir("io/github/lc/oss/commons/util", 1, file -> file.toString().endsWith(".junk"));
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());

        result = IoTools.listDir("io/github/lc/oss/commons/util/junk/does/not/exist", 1, file -> file.toString().endsWith(".junk"));
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());

        result = IoTools.listDir("io/github/lc/oss/commons/util", 1, file -> file.toString().endsWith(".class"));
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    public void test_listDirs_null() {
        try {
            IoTools.listDir(null, 1, file -> file.toString().endsWith(".txt"));
            Assertions.fail("Expected exception");
        } catch (RuntimeException ex) {
            Assertions.assertEquals("Error converting 'null' to URI.", ex.getMessage());
        }
    }

    @Test
    public void test_listDirs_uri_null() {
        try {
            IoTools.listDir(null, "/", 1, file -> file.toString().endsWith(".txt"));
            Assertions.fail("Expected exception");
        } catch (RuntimeException ex) {
            Assertions.assertEquals("Error listing files in directory.", ex.getMessage());
        }
    }

    @Test
    public void test_listDir_absolutePath() {
        File dir = new File(this.getTempSubdirPath());
        Assertions.assertTrue(dir.mkdirs());

        List<String> result = IoTools.listDir(this.getTempSubdirPath(), 1, file -> file.toString().endsWith(".txt"));
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());

        IoTools.writeToFile("test".getBytes(StandardCharsets.UTF_8), this.getTempFilePath());

        result = IoTools.listDir(this.getTempDirPath(), 1, file -> file.toString().endsWith("iotools-test.dat"));
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertTrue(result.iterator().next().endsWith("iotools-test.dat"));

        result = IoTools.listDir(this.getTempFilePath(), 1);
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertTrue(result.iterator().next().endsWith("iotools-test.dat"));

        /* jar file test requires non-source classpath jar */
        result = IoTools.listDir("org/junit/jupiter/api", 1);
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());

        /* test a cached jar */
        result = IoTools.listDir("org/junit/jupiter/api", 1);
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    public void test_readFile() {
        byte[] read = IoTools.readFile("io/github/lc/oss/commons/util/IoTools.class");
        Assertions.assertNotNull(read);
        for (int i = 0; i < IoToolsTest.CLASS_FILE_HEADER.length; i++) {
            Assertions.assertEquals(IoToolsTest.CLASS_FILE_HEADER[i], read[i]);
        }
    }

    @Test
    public void test_readFile_error() {
        File subdir = new File(this.getTempSubdirPath());
        Assertions.assertTrue(subdir.mkdirs());

        try {
            IoTools.readAbsoluteFile(subdir.getAbsolutePath());
            Assertions.fail("Expected exception");
        } catch (RuntimeException ex) {
            Assertions.assertEquals("Error reading file from disk.", ex.getMessage());
        }
    }

    @Test
    public void test_readStream() {
        File file = new File("target/classes/io/github/lc/oss/commons/util/IoTools.class");
        try (InputStream read = IoTools.readStream(file)) {

            Assertions.assertNotNull(read);
            for (int i = 0; i < IoToolsTest.CLASS_FILE_HEADER.length; i++) {
                Assertions.assertEquals(IoToolsTest.CLASS_FILE_HEADER[i], (byte) read.read());
            }
        } catch (IOException ex) {
            Assertions.fail("Unexpected exception");
        }
    }

    @Test
    public void test_readStream_error() {
        File subdir = new File(this.getTempSubdirPath());
        Assertions.assertTrue(subdir.mkdirs());

        try {
            IoTools.readAbsoluteStream(subdir.getAbsolutePath());
            Assertions.fail("Expected exception");
        } catch (RuntimeException ex) {
            Assertions.assertEquals("Error getting file stream from disk.", ex.getMessage());
        }
    }

    @Test
    public void test_readAbsoluteStream_nullBlank() {
        InputStream result = IoTools.readAbsoluteStream(null);
        Assertions.assertNull(result);

        result = IoTools.readAbsoluteStream("");
        Assertions.assertNull(result);

        result = IoTools.readAbsoluteStream(" ");
        Assertions.assertNull(result);

        result = IoTools.readAbsoluteStream(" \t \r \n \t ");
        Assertions.assertNull(result);
    }

    @Test
    public void test_readAbsoluteStream_fromJar() {
        try (InputStream read = IoTools.readAbsoluteStream("IoTools.class")) {

            Assertions.assertNotNull(read);
            for (int i = 0; i < IoToolsTest.CLASS_FILE_HEADER.length; i++) {
                Assertions.assertEquals(IoToolsTest.CLASS_FILE_HEADER[i], (byte) read.read());
            }
        } catch (IOException ex) {
            Assertions.fail("Unexpected exception");
        }
    }

    @Test
    public void test_readFileFromJar_error() {
        try {
            IoTools.readFileFromJar(null);
            Assertions.fail("Expected exception");
        } catch (RuntimeException ex) {
            Assertions.assertEquals("Error reading file from jar.", ex.getMessage());
        }
    }

    @Test
    public void test_readFileFromDisk_error() {
        try {
            IoTools.readFileFromDisk(null);
            Assertions.fail("Expected exception");
        } catch (RuntimeException ex) {
            Assertions.assertEquals("Error reading file from disk.", ex.getMessage());
        }
    }

    @Test
    public void test_writeToFile_error() {
        File subdir = new File(this.getTempSubdirPath());
        Assertions.assertTrue(subdir.mkdirs());

        try {
            IoTools.writeToFile(new byte[0], subdir.getAbsolutePath());
            Assertions.fail("Expected exception");
        } catch (RuntimeException ex) {
            Assertions.assertEquals("Error writing to file.", ex.getMessage());
        }
    }

    @Test
    public void test_getFileSystem_error() {
        try {
            IoTools.getFileSystem(new URI("jar:file:/junk"));
            Assertions.fail("Expected exception");
        } catch (RuntimeException ex) {
            Assertions.assertEquals("Error getting FileSystem", ex.getMessage());
        } catch (URISyntaxException e) {
            Assertions.fail("Unexpected exception");
        }
    }
}

package com.github.lc.oss.commons.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class IoTools {
    public static String getAbsoluteFilePath(String filePath) {
        if (filePath == null || filePath.trim().equals("")) {
            return null;
        }

        String normalized = filePath.replace("\\", "/");
        Collection<String> files = IoTools.listDir(filePath, 1, path -> path.toAbsolutePath().toString().replace("\\", "/").endsWith(normalized));
        if (files.isEmpty()) {
            return null;
        }
        return files.iterator().next();
    }

    public static List<String> listDir(String path, int depth) {
        return IoTools.listDir(path, depth, null);
    }

    public static FileSystem getFileSystem(URI uri) {
        int retryCount = 5;
        Exception cause = null;
        while (retryCount > 0) {
            try {
                /* JVM throws exception instead of being a good API... */
                return FileSystems.getFileSystem(uri);
            } catch (FileSystemNotFoundException fex) {
                try {
                    return FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
                } catch (Exception ex) {
                    cause = ex;
                }
            }

            retryCount--;
        }
        throw new RuntimeException("Error getting FileSystem", cause);
    }

    public static List<String> listDir(String path, int depth, Predicate<Path> matches) {
        try {
            URL resource = IoTools.class.getClassLoader().getResource(path);
            if (resource == null) {
                File file = new File(path);
                if (!file.exists()) {
                    return new ArrayList<>();
                }
                resource = file.toURI().toURL();
            }
            URI uri = resource.toURI();
            return IoTools.listDir(uri, path, depth, matches);
        } catch (Exception ex) {
            throw new RuntimeException(String.format("Error converting '%s' to URI.", path), ex);
        }
    }

    public static List<String> listDir(URI uri, String path, int depth, Predicate<Path> matches) {
        List<String> result = new ArrayList<>();
        Stream<Path> pathWalker = null;
        try {
            Path myPath;
            if (uri.getScheme().equals("jar")) {
                FileSystem fileSystem = IoTools.getFileSystem(uri);
                myPath = fileSystem.getPath(path);
                if (!Files.exists(myPath)) {
                    String subpath = path;
                    if (subpath != null && subpath.startsWith("/")) {
                        subpath = subpath.substring(1);
                    }
                    myPath = fileSystem.getPath("/BOOT-INF/classes/" + subpath);
                }
            } else {
                myPath = Paths.get(uri);
            }
            pathWalker = Files.walk(myPath, depth);
            Path p;
            Iterator<Path> i = pathWalker.iterator();
            while (i.hasNext()) {
                p = i.next();
                if (matches == null || matches.test(p)) {
                    result.add(p.toAbsolutePath().toString());
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error listing files in directory.", ex);
        } finally {
            if (pathWalker != null) {
                pathWalker.close();
            }
        }
        return result;
    }

    public static byte[] readFile(File file) {
        return IoTools.readAbsoluteFile(file.getAbsolutePath());
    }

    public static byte[] readFile(String filePath) {
        String path = IoTools.getAbsoluteFilePath(filePath);
        return IoTools.readAbsoluteFile(path);
    }

    public static InputStream readStream(File file) {
        return IoTools.readStream(file.getAbsolutePath());
    }

    public static InputStream readStream(String filePath) {
        String path = IoTools.getAbsoluteFilePath(filePath);
        return IoTools.readAbsoluteStream(path);
    }

    public static byte[] readAbsoluteFile(String absolutePath) {
        if (absolutePath == null || absolutePath.trim().equals("")) {
            return null;
        }

        File file = new File(absolutePath);
        if (file.exists()) {
            return IoTools.readFileFromDisk(absolutePath);
        } else {
            return IoTools.readFileFromJar(absolutePath);
        }
    }

    public static InputStream readAbsoluteStream(String absolutePath) {
        if (absolutePath == null || absolutePath.trim().equals("")) {
            return null;
        }

        File file = new File(absolutePath);
        if (file.exists()) {
            return IoTools.readStreamFromDisk(absolutePath);
        } else {
            return IoTools.readStreamFromJar(absolutePath);
        }
    }

    public static byte[] readFileFromDisk(String filePath) {
        try (InputStream reader = IoTools.readStreamFromDisk(filePath); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int len = 0;
            while ((len = reader.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            return out.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException("Error reading file from disk.", ex);
        }
    }

    public static byte[] readFileFromJar(String filePath) {
        try (InputStream input = IoTools.readStreamFromJar(filePath); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            if (input == null) {
                return null;
            }

            int len = 0;
            byte[] buffer = new byte[4096];
            while ((len = input.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            return out.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException("Error reading file from jar.", ex);
        }
    }

    public static InputStream readStreamFromDisk(String filePath) {
        try {
            return new DataInputStream(new BufferedInputStream(new FileInputStream(filePath)));
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("Error getting file stream from disk.", ex);
        }
    }

    public static InputStream readStreamFromJar(String filePath) {
        return IoTools.class.getResourceAsStream(filePath);
    }

    public static void writeToFile(String content, String filePath) {
        IoTools.writeToFile(content.getBytes(StandardCharsets.UTF_8), filePath);
    }

    public static void writeToFile(byte[] content, String filePath) {
        try (DataOutputStream writer = new DataOutputStream(new FileOutputStream(filePath))) {
            writer.write(content);
        } catch (IOException ex) {
            throw new RuntimeException("Error writing to file.", ex);
        }
    }

    private IoTools() {
    }
}

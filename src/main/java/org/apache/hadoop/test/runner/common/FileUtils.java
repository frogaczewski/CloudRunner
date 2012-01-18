package org.apache.hadoop.test.runner.common;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.apache.hadoop.fs.FileUtil;

public class FileUtils {

    public static String extension(File file) {
        String name = file.getName();
        int i = name.length() - 1;
        while (i >= 0 && name.charAt(i) != '.')
            i--;
        if (i < 0) {
            return "";
        } else {
            return name.substring(i + 1).toLowerCase();
        }
    }

    public static File makeDirectory(File file, boolean deleteIfExists) {
        if (file.exists() && deleteIfExists) {
            FileUtil.fullyDelete(file);
        }
        file.mkdirs();
        return file;
    }

    public static boolean unzip(File file, File directory) {
        try {
            FileUtil.unZip(file, directory);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static Collection<File> traverseDirectory(File directory) {
        Stack<File> files = new Stack<File>();
        Set<File> result = new HashSet<File>();
        files.add(directory);
        File current = null;
        while (!files.isEmpty() && (current = files.pop()) != null) {
            if (current.isDirectory()) {
                files.addAll(Arrays.asList(current.listFiles()));
            } else if (current.isFile()) {
                result.add(current);
            }
        }
        return result;
    }

    public static void fullyDelete(File directory) {
        FileUtil.fullyDelete(directory);
    }
}

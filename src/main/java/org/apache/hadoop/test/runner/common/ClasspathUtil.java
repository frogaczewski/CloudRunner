package org.apache.hadoop.test.runner.common;

import org.apache.hadoop.conf.Configuration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ClasspathUtil {

    /**
     * Creates a name of a class from a path to class stored in HDFS Classpath
     * Directory. For instance, returns <code>org.apache.hadoop.Job</code> from
     * <code>/hadoop/test/runner/classpath/org/apache/hadoop/Job.class</code>.
     * 
     * @param entry
     * @throws IllegalArgumentException
     * @return
     */
    private static String hdfsClasspathEntryToClassName(String entry) {
        String classpathDirectory = RunnerConfiguration
                .getHDFSClasspathDirectory();
        if (entry.contains(classpathDirectory)) {
            entry = entry.substring(entry.indexOf(classpathDirectory)
                    + classpathDirectory.length());
            if (entry.endsWith(".class")) {
                return classFileNameToClassName(entry);
            } else {
                return entry;
            }
        }
        throw new IllegalArgumentException("Entry " + entry
                + " is not a HDFS class file");
    }

    /**
     * Creates a name of a class from a name of a class file. For instance,
     * returns <code>org.apache.hadoop.Job</code> for
     * <code>org/apache/hadoop/Job.class</code>.
     * 
     * @param classFileName
     * @throws IllegalArgumentException
     * @return
     */
    public static String classFileNameToClassName(String classFileName) {
        if (classFileName.endsWith(".class")) {
            classFileName = classFileName.substring(0,
                    classFileName.indexOf(".class"));
            return classFileName.replace("/", ".");
        }
        throw new IllegalArgumentException("The name of file " + classFileName
                + " is not a java class file.");
    }

    /**
     * Creates a map(K, V) of classes from the distributed classpath, where K -
     * name of class from the distributed classpath, V - url to this class.
     * 
     * @param configuration
     * @return
     * @throws MalformedURLException
     */
    public static Map<String, URL> classpathToURL(Configuration configuration)
            throws MalformedURLException {
        Collection<String> classpathEntries = configuration
                .getStringCollection(RunnerConfiguration
                        .getConfigurationClasspathEntry());
        Map<String, URL> classpath = new HashMap<String, URL>();
        for (String entry : classpathEntries) {
            if (entry != null) {
                classpath.put(hdfsClasspathEntryToClassName(entry), new URL(
                        entry));
            }
        }
        return classpath;
    }

}

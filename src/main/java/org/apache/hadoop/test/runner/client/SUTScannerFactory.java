package org.apache.hadoop.test.runner.client;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.hadoop.test.runner.common.FileUtils;

public class SUTScannerFactory {

    private SUTScannerFactory() {
        super();
    }

    public static SUTScannerFactory get() {
        return new SUTScannerFactory();
    }

    public ISUTScanner create(String... args) {
        if (args.length == 0) {
            throw new IllegalArgumentException(
                    "No jar file or no classes passed for creating SUTScanner");
        } else if (args.length == 1
                && FileUtils.extension(new File(args[0])).equals("jar")) {
            return jarSUTScanner(args[0]);
        } else {
            Set<URL> classes = new HashSet<URL>();
            for (String arg : args) {
                try {
                    if (arg.endsWith(".class")) {
                        classes.add(new URL(arg));
                    } else {
                        throw new IllegalArgumentException(arg
                                + " is not a class file.");
                    }
                } catch (MalformedURLException e) {
                    throw new IllegalArgumentException(e);
                }
            }
            return classFileSUTScanner(classes);
        }
    }

    /**
     * TODO add info about parent folder so it is possible to get the proper
     * name of the class.
     * 
     * @param classes
     * @return
     */
    private ISUTScanner classFileSUTScanner(final Collection<URL> classes) {

        return new AbstractSUTScanner<URL>(new URLClassLoader(
                classes.toArray(new URL[0]))) {

            private Enumeration<URL> iterator = Collections
                    .enumeration(classes);

            @Override
            protected Enumeration<URL> getEnumerator() {
                return iterator;
            }

            @Override
            protected Enumeration<URL> getNewEnumerator() {
                return Collections.enumeration(classes);
            }

            @Override
            protected String getElementName(URL element) {
                return element.getFile();
            }
        };
    }

    private ISUTScanner jarSUTScanner(String filename) {
        try {
            final File file = new File(filename);
            final JarFile jarFile = new JarFile(file);
            ClassLoader classLoader = new URLClassLoader(new URL[] { file
                    .toURI().toURL() });
            return new AbstractSUTScanner<JarEntry>(classLoader) {

                private Enumeration<JarEntry> iterator = jarFile.entries();

                @Override
                protected Enumeration<JarEntry> getNewEnumerator() {
                    return jarFile.entries();
                }

                @Override
                protected Enumeration<JarEntry> getEnumerator() {
                    return iterator;
                }

                @Override
                protected String getElementName(JarEntry element) {
                    return element.getName();
                }
            };
        } catch (IOException e) {
            throw new RuntimeException("Can't create a SUT Scanner for "
                    + filename, e);
        }
    }

}

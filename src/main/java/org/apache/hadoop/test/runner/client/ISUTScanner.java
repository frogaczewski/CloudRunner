package org.apache.hadoop.test.runner.client;

import java.util.Enumeration;

/**
 * This is an interface for classes which scans the path for classes which may
 * contains tests to execute.
 * 
 * All scanners have to scan the sut-path for classes from left to right.
 */
public interface ISUTScanner extends Enumeration<Class<?>> {

    /**
     * Finds the class in SUT path with the given name. If there are multiple
     * classes with this name, this method returns the first found class.
     * 
     * @param name
     * @return
     */
    Class<?> find(String name) throws ClassNotFoundException;

}

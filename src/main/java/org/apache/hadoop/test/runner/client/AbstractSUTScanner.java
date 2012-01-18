package org.apache.hadoop.test.runner.client;

import org.apache.hadoop.test.runner.common.ClasspathUtil;

import java.util.Enumeration;

public abstract class AbstractSUTScanner<T> implements ISUTScanner {

    /**
     * Returns enumerator used for iterating over the files in SUT-path.
     * 
     * @return
     */
    protected abstract Enumeration<T> getEnumerator();

    /**
     * Returns a new instance of enumerator used for iterating over the files in
     * SUT-path.
     * 
     * @return
     */
    protected abstract Enumeration<T> getNewEnumerator();

    /**
     * Returns the name of an entry from sut-path.
     * 
     * @param element
     * @return
     */
    protected abstract String getElementName(T element);

    private T next;

    /**
     * Classloader used for returning test classes from SUT path.
     */
    private ClassLoader classLoader;

    protected AbstractSUTScanner(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public Class<?> find(String name) throws ClassNotFoundException {
        Enumeration<T> enumerator = getNewEnumerator();
        while (enumerator.hasMoreElements()) {
            String className = getElementName(enumerator.nextElement());
            if (name.endsWith(".class")
                    && ClasspathUtil.classFileNameToClassName(className)
                            .equals(name)) {
                return classLoader.loadClass(name);
            }
        }
        throw new ClassNotFoundException("Class " + name
                + " not found in SUT path");
    }

    @Override
    public boolean hasMoreElements() {
        if (next != null) {
            return true;
        } else {
            while (getEnumerator().hasMoreElements()) {
                next = getEnumerator().nextElement();
                if (getElementName(next).endsWith(".class")) {
                    return true;
                }
            }
        }
        next = null;
        return false;
    }

    @Override
    public Class<?> nextElement() {
        try {
            String result = null;
            if (next != null || hasMoreElements()) {
                result = getElementName(next);
                next = null;
            }
            String className = ClasspathUtil.classFileNameToClassName(result);
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
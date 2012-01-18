package org.apache.hadoop.test.runner.common;

import java.util.Collection;

public interface IDescriptionAdapter {

    Collection<? extends IDescriptionAdapter> getAtomicTests();

    boolean isExecutable();

    String text();

    String getClassName();

    String getMethodName();

}
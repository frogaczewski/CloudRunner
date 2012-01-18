package org.apache.hadoop.test.runner.common;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.junit.runner.Description;

public class DescriptionAdapter implements IDescriptionAdapter {

    private Description description;
    // This is a cheap workaround for Description#getClassName which
    // returns 
//    private String className;
    //private String methodName;

    public DescriptionAdapter(Description description) {
        super();
        this.description = description;
    }

    @Override
    public Collection<? extends IDescriptionAdapter> getAtomicTests() {
        if (isExecutable()) {
            return Collections.singleton(this);
        } else {
            return traverseDescriptionTree();
        }
    }

    /**
     * The test is executable when it's description is a leaf in a description's
     * tree.
     * 
     * @return true, if this is an atomic test ready to execute.
     */
    @Override
    public boolean isExecutable() {
        return description.isTest();
    }

    @Override
    public String text() {
        if (isExecutable()) {
            StringBuilder sb = new StringBuilder();
            sb.append(description.getClassName());
            sb.append(DescriptionAdapterFactory.BREAKING_CHAR);
            sb.append(description.getMethodName());
            return sb.toString();
        } else {
            throw new RuntimeException(
                    "The description must be an atomic test.");
        }
    }

    @Override
    public String getClassName() {
        return description.getClassName();
    }

    @Override
    public String getMethodName() {
        if (isExecutable()) {
            return description.getMethodName();
        } else {
            return "";
        }
    }

    private Collection<? extends IDescriptionAdapter> traverseDescriptionTree() {
        Stack<DescriptionAdapter> stack = new Stack<DescriptionAdapter>();
        stack.push(this);
        Set<DescriptionAdapter> result = new HashSet<DescriptionAdapter>();
        DescriptionAdapter iterator = null;
        while (!stack.isEmpty() && (iterator = stack.pop()) != null) {
            if (iterator.isExecutable()) {
                result.add(iterator);
            } else {
                for (Description child : iterator.description.getChildren()) {
                    stack.add(new DescriptionAdapter(child));
                }
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof IDescriptionAdapter) {
            IDescriptionAdapter other = (IDescriptionAdapter) obj;
            return other.getClassName().equals(this.getClassName())
                    && other.getMethodName().equals(this.getMethodName());
        }
        return false;
    }

    public static IDescriptionAdapter empty() {
        return new DescriptionAdapter(Description.EMPTY) {
            @Override
            public boolean isExecutable() {
                return false;
            }

            @Override
            public String getClassName() {
                return "";
            }

            @Override
            public String getMethodName() {
                return "";
            }
        };
    }       


}

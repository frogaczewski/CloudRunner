package org.apache.hadoop.test.runner.common;

import org.apache.hadoop.io.Text;
import org.junit.runner.Description;
import org.junit.runner.Request;

public class DescriptionAdapterFactory {

    protected static final String BREAKING_CHAR = "#";

    public IDescriptionAdapter create(Text text, ClassLoader classLoader) {
        try {
            String test = text.toString();
            String className = test.substring(0, test.indexOf(BREAKING_CHAR));
            String methodName = test.substring(test.indexOf(BREAKING_CHAR) + 1);
            Class<?> clazz = classLoader.loadClass(className);
            return new DescriptionAdapter(Description.createTestDescription(
                    clazz, methodName));
        } catch (ClassNotFoundException e) {
            return DescriptionAdapter.empty();
        } catch (IndexOutOfBoundsException e) {
            return DescriptionAdapter.empty();
        }
    }

    public IDescriptionAdapter create(Class<?> clazz) {
        return new DescriptionAdapter(Request.aClass(clazz).getRunner()
                .getDescription());
    }

}

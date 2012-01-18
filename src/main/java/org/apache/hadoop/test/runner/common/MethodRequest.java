package org.apache.hadoop.test.runner.common;

import java.util.Collections;
import java.util.List;

import org.junit.internal.requests.ClassRequest;
import org.junit.runner.Request;
import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public class MethodRequest extends ClassRequest {
    
    private final String methodName;
    private final Class<?> klass;

    private MethodRequest(Class<?> testClass, String methodName) {
        super(testClass);
        this.klass = testClass;
        this.methodName = methodName;
    }

    public static Request method(Class<?> clazz, String methodName) {
        // Description method = Description.createTestDescription(clazz, methodName);
        return new MethodRequest(clazz, methodName);
    }
    
    @Override
    public Runner getRunner() {        
        try {
            final FrameworkMethod method = new FrameworkMethod(klass.getMethod(methodName, new Class<?>[0]));
            return new BlockJUnit4ClassRunner(klass) {

                @Override
                protected List<FrameworkMethod> computeTestMethods() {
                    return Collections.singletonList(method);
                }
                
            };
        } catch (InitializationError e) {
            e.printStackTrace();
            return null;
        } catch (SecurityException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

}

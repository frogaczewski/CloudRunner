package org.apache.hadoop.test.runner.server;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.test.runner.common.DescriptionAdapterFactory;
import org.apache.hadoop.test.runner.common.IDescriptionAdapter;
import org.apache.hadoop.test.runner.common.MethodRequest;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;

public class RunTestCaseJob extends Thread {

    private Result testResult;
    private Text text;

    /**
     * Creates a new instance
     * 
     * 
     * @param description
     * @param classLoader
     * @param semaphore
     * @return
     */
    public static RunTestCaseJob get(ClassLoader classLoader, Text text) {
        return new RunTestCaseJob(classLoader, text);
    }

    private RunTestCaseJob(ClassLoader classLoader, Text text) {
        this.text = text;
        setContextClassLoader(classLoader);
    }

    public void run() {
        try {
            IDescriptionAdapter adapter = new DescriptionAdapterFactory().create(text, this.getContextClassLoader());
            String classname = adapter.getClassName();
            String method = adapter.getMethodName();
            Request request =
                    MethodRequest.method(getContextClassLoader().loadClass(classname), method);
            testResult = new JUnitCore().run(request);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Result getTestResult() {
        return testResult;
    }
}

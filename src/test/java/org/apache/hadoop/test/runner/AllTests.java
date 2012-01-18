package org.apache.hadoop.test.runner;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
      TestClassLoaderUtil.class,
      TestClasspathUtil.class,
      TestClasspathUploader.class,
      TestJobSerializer.class,
      TestDistributedClassLoader.class,
      TestSUTScanner.class
})
public class AllTests {

   public static Test suite() {
      return new JUnit4TestAdapter(AllTests.class);
   }

}

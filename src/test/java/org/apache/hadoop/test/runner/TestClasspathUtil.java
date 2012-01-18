package org.apache.hadoop.test.runner;

import static org.junit.Assert.assertTrue;

import org.apache.hadoop.test.runner.common.ClasspathUtil;
import org.junit.Test;

public class TestClasspathUtil {

   @Test
   public void testClassFileNameToClassName() {
      String classFileName = "org/apache/hadoop/Job.class";
      assertTrue(ClasspathUtil.classFileNameToClassName(classFileName).equals("org.apache.hadoop.Job"));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testIllegalClassFileNameToClassName() {
      String classFileName = "org/apache/hadoop/Job.jpg";
      ClasspathUtil.classFileNameToClassName(classFileName).equals("org.apache.hadoop.Job");
   }

}

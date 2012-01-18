package org.apache.hadoop.test.runner;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.test.runner.client.ClasspathUploader;
import org.apache.hadoop.test.runner.client.IClasspathUploader;
import org.apache.hadoop.test.runner.server.DistributedClassLoader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;

public class TestDistributedClassLoader {

   private Configuration configuration;

   @Before
   public void setUp() throws IOException, URISyntaxException {
      configuration = new Configuration();
      IClasspathUploader uploader = ClasspathUploader.get(configuration);
      uploader.upload(new File(getClass().getResource(TestsUtil.localJunitJarPath).toURI()),
         configuration);
   }

   @Test
   public void testSingleURLClassLoader() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
      ClassLoader loader = DistributedClassLoader.get(configuration);
      Class<?> clazz = loader.loadClass("org.junit.runner.JUnitCore");
      Assert.assertNotNull(clazz);
      Object obj = clazz.newInstance();
      Assert.assertNotNull(obj);
      Assert.assertTrue(obj.getClass().getCanonicalName().equals("org.junit.runner.JUnitCore"));
      Assert.assertFalse(obj instanceof JUnitCore);
   }

   @Test(expected = ClassNotFoundException.class)
   public void testSingleURLNoHadoopDependencies() throws ClassNotFoundException {
      ClassLoader loader = DistributedClassLoader.get(configuration);
      loader.loadClass("org.apache.hadoop.mapreduce.Job");
   }


}

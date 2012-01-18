package org.apache.hadoop.test.runner;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.test.runner.client.IClasspathUploader;
import org.apache.hadoop.test.runner.common.RunnerConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;


public class TestClasspathUploader {

   private Configuration configuration;
   private static final String classesParent = "/";
   private IClasspathUploader classpathUploader;

   @Before
   public void setUp() throws IOException {
      configuration = new Configuration();
      classpathUploader = org.apache.hadoop.test.runner.client.ClasspathUploader.get(configuration);
   }

   @Test
   public void testUploadJar() throws URISyntaxException, IOException {
      File local = new File(getClass().getResource(TestsUtil.localJunitJarPath).toURI());
      classpathUploader.upload(local, configuration);
      assertTrue(jobContainsClassPathEntry(
            configuration,
            RunnerConfiguration.getHDFSClasspathDirectory() + "org/junit/runner/JUnitCore.class"));
   }

   @Test
   public void testUploadClassFile() throws URISyntaxException, IOException {
      File local = new File(getClass().getResource(TestsUtil.localJunitCoreClasspath).toURI());
      File parent = new File(getClass().getResource(classesParent).toURI());
      classpathUploader.upload(local, configuration, parent);
      assertTrue(jobContainsClassPathEntry(
            configuration,
            RunnerConfiguration.getHDFSClasspathDirectory() + "org/junit/runner/JUnitCore.class"));
   }

   @Test
   public void testUploadClassAndJar() throws URISyntaxException, IOException {
      File jar = new File(getClass().getResource(TestsUtil.localJunitJarPath).toURI());
      File classFile = new File(getClass().getResource(TestsUtil.localJunitCoreClasspath).toURI());
      File parent = new File(getClass().getResource(classesParent).toURI());
      classpathUploader.upload(jar, configuration);
      classpathUploader.upload(classFile, configuration, parent);
      assertTrue(jobContainsClassPathEntry(configuration,
         RunnerConfiguration.getHDFSClasspathDirectory() + "org/junit/runner/JUnitCore.class"));
      assertTrue(jobContainsClassPathEntry(configuration,
         RunnerConfiguration.getHDFSClasspathDirectory() + "org/junit/Assert.class"));
      assertTrue(!jobContainsClassPathEntry(configuration,
         RunnerConfiguration.getHDFSClasspathDirectory() + "java/lang/Object.class"));
   }

   private boolean jobContainsClassPathEntry(Configuration configuration, String classpathEntry)
         throws IOException {
      classpathEntry = FileSystem.get(configuration).makeQualified(new Path(classpathEntry)).toString();
      return configuration
            .getStringCollection(RunnerConfiguration.getConfigurationClasspathEntry())
            .contains(classpathEntry);
   }

}

package org.apache.hadoop.test.runner;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.test.runner.common.ClasspathUtil;
import org.apache.hadoop.test.runner.common.RunnerConfiguration;
import org.junit.Before;
import org.junit.Test;

public class TestClassLoaderUtil {

	private Configuration configuration;
	private URL junitCore;

	@Before
	public void setUp() throws IOException, URISyntaxException {
		configuration = new Configuration();
		junitCore = new File(RunnerConfiguration.getHDFSClasspathDirectory()
				+ "org/junit/runner/JUnitCore.class").toURI().toURL();
		configuration.setStrings(
				RunnerConfiguration.getConfigurationClasspathEntry(),
				junitCore.toString());
	}

	@Test
	public void testSingleFileInClasspathToURL() throws MalformedURLException {
		Map<String, URL> classpath = ClasspathUtil
				.classpathToURL(configuration);
		assertTrue(classpath.size() == 1);
		assertTrue(classpath.keySet().contains("org.junit.runner.JUnitCore"));
	}

	@Test
	public void testMultipleFileInClasspathToURL() throws MalformedURLException {
		URL junitAssert = new File(
				RunnerConfiguration.getHDFSClasspathDirectory()
						+ "org/junit/runner/Assert.class").toURI().toURL();
		configuration.setStrings(
				RunnerConfiguration.getConfigurationClasspathEntry(),
				junitAssert.toString(), junitCore.toString());
		Map<String, URL> classpath = ClasspathUtil
				.classpathToURL(configuration);
		assertTrue(classpath.size() == 2);
		assertTrue(classpath.keySet().contains("org.junit.runner.JUnitCore"));
		assertTrue(classpath.keySet().contains("org.junit.runner.Assert"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentExceptionClasspathToURL()
			throws MalformedURLException {
		URL jarFile = new File(TestsUtil.localClassFilePath).toURI().toURL();
		configuration.setStrings(
				RunnerConfiguration.getConfigurationClasspathEntry(),
				jarFile.toString());
		ClasspathUtil.classpathToURL(configuration);
	}

}

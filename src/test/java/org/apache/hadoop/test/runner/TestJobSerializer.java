package org.apache.hadoop.test.runner;

import org.apache.hadoop.test.runner.client.JobSerializer;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class TestJobSerializer {

   @Test
   public void testMakeFileWithListOfDescriptions() throws URISyntaxException, IOException {
      final File jarFile = new File(getClass().getResource(TestsUtil.localJunitTestsJarPath).toURI());
      JobSerializer jobSerializer =
            new JobSerializer(1, jarFile.getAbsolutePath());
      jobSerializer.serialize(new File("output.txt"));
   }

}

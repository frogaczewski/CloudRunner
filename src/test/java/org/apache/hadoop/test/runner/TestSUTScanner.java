package org.apache.hadoop.test.runner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;

import org.apache.hadoop.test.runner.client.ISUTScanner;
import org.apache.hadoop.test.runner.client.SUTScannerFactory;
import org.junit.Test;

public class TestSUTScanner {

   @Test
   public void testCreatingSUTScannerForJar() {
      String unitPath = this.getClass().getResource(TestsUtil.localJunitTestsJarPath).getPath();
      ISUTScanner scanner = SUTScannerFactory.get().create(unitPath);
      assertNotNull(scanner);
   }

   @Test(expected = RuntimeException.class)
   public void testCreatingSUTScannerFromInvalidJarFile() {
      SUTScannerFactory.get().create("somefile.jar");
   }

   @Test(expected = IllegalArgumentException.class)
   public void testCreatingSUTScannerFromNoArguments() {
      SUTScannerFactory.get().create("");
   }

   @Test(expected = IllegalArgumentException.class)
   public void testCreatingSUTScannerFromSetOfNotClassFiles() {
      String[] args = {"something.class", "somethingelse.class", "some.jpg"};
      SUTScannerFactory.get().create(args);
   }


   @Test
   public void testFindingTestClassInJarUsingScanner() {
      String unitPath = this.getClass().getResource(TestsUtil.localJunitTestsJarPath).getPath();
      ISUTScanner scanner = SUTScannerFactory.get().create(unitPath);
      while (scanner.hasMoreElements()) {
         assertNotNull(scanner.nextElement());
      }
   }

   @Test
   public void testFindingClassesUsingClassFiles() throws URISyntaxException {
      String path =
            this.getClass().getResource(TestsUtil.localJunitCoreClasspath).toString();
      ISUTScanner scanner = SUTScannerFactory.get().create(path);
      assertTrue(scanner.hasMoreElements());
      assertNotNull(scanner.nextElement());
   }

}

package org.apache.hadoop.test.runner.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.hadoop.test.runner.common.DescriptionAdapterFactory;
import org.apache.hadoop.test.runner.common.IDescriptionAdapter;

public class JobSerializer {

    private String[] sutPath;
    private int repeatCount;
    private DescriptionAdapterFactory descriptionFactory;

    public JobSerializer(int repeatCount, String... sutPath) {
        this.repeatCount = repeatCount;
        this.sutPath = sutPath;
        this.descriptionFactory = new DescriptionAdapterFactory();
    }

    public void serialize(File output) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(output));
            ISUTScanner scanner = SUTScannerFactory.get().create(sutPath);
            while (scanner.hasMoreElements()) {
                IDescriptionAdapter descriptionAdapter = descriptionFactory
                        .create(scanner.nextElement());
                for (IDescriptionAdapter d : descriptionAdapter
                        .getAtomicTests()) {
                    writeDescription(repeatCount, writer, d);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(
                    "Exception during creating list of tests to execute.", e);
        } finally {
            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void writeDescription(int count, Writer writer,
            IDescriptionAdapter descriptionAdapter) throws IOException {
        while (count-- > 0) {
            writer.write(descriptionAdapter.text() + "\n");
        }
    }

}

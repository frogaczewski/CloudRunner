package org.apache.hadoop.test.runner.client;

import java.io.File;

import org.apache.hadoop.conf.Configuration;

public interface IClasspathUploader {

    void upload(File local, Configuration configuration);

    /**
     * Upload a classpath entry to HDFS.
     * 
     * @param local
     *            - an archive to upload.
     * @param configuration
     *            - Hadoop configuration.
     * @param parent
     *            - path from which the file is uploaded.
     */
    void upload(File local, Configuration configuration, File parent);
}

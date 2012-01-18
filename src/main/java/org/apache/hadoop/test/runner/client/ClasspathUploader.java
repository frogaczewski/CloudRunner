package org.apache.hadoop.test.runner.client;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.test.runner.common.FileUtils;
import org.apache.hadoop.test.runner.common.RunnerConfiguration;

public class ClasspathUploader implements IClasspathUploader {

    private FileSystem fileSystem;

    public static IClasspathUploader get(Configuration configuration)
            throws IOException {
        return new ClasspathUploader(FileSystem.get(configuration));
    }

    private ClasspathUploader(FileSystem fs) {
        this.fileSystem = fs;
    }

    @Override
    public void upload(File local, Configuration configuration) {
        upload(local, configuration, null);
    }

    @Override
    public void upload(File local, Configuration configuration, File parent) {
        String fileExtension = FileUtils.extension(local);
        if (fileExtension.equals("war") || fileExtension.equals("jar")) {
            uploadJavaArchive(local, configuration);
        } else if (fileExtension.equals("class")) {
            uploadClassFile(local, configuration, parent);
        } else {
            throw new IllegalArgumentException("Unsupported file format");
        }
    }

    private void uploadJavaArchive(File local, Configuration configuration) {
        File directory = FileUtils.makeDirectory(
                RunnerConfiguration.getLocalClasspathDirectoryFile(), true);
        if (FileUtils.unzip(local, directory)) {
            for (File f : FileUtils.traverseDirectory(directory)) {
                uploadClassFile(f, configuration, null);
            }
            FileUtils.fullyDelete(directory);
        }
    }

    private void uploadClassFile(File local, Configuration configuration,
            File parent) {
        try {
            Path remote = getHDFSPathToUploadedFile(local, parent);
            fileSystem.copyFromLocalFile(new Path(local.toURI()), remote);
            setConfigurationClasspath(configuration,
                    fileSystem.makeQualified(remote).toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setConfigurationClasspath(Configuration configuration,
            String url) {
        String classpathEntry = configuration.get(RunnerConfiguration
                .getConfigurationClasspathEntry());
        if (classpathEntry != null) {
            configuration.setStrings(
                    RunnerConfiguration.getConfigurationClasspathEntry(), url,
                    classpathEntry);
        } else {
            configuration.set(
                    RunnerConfiguration.getConfigurationClasspathEntry(), url);
        }
    }

    private Path getHDFSPathToUploadedFile(File local, File parent) {
        String fileName = local.getPath();
        if (fileName.startsWith(RunnerConfiguration
                .getLocalClasspathDirectoryFile().getPath())) {
            fileName = fileName.substring(RunnerConfiguration
                    .getLocalClasspathDirectoryFile().getPath().length());
        } else if (parent != null
                && local.getAbsolutePath().startsWith(parent.getAbsolutePath())) {
            fileName = local.getAbsolutePath().substring(
                    parent.getAbsolutePath().length());
        } else {
            throw new IllegalArgumentException(
                    "Can't create HDFS path for file " + local);
        }
        return new Path(RunnerConfiguration.getHDFSClasspathDirectory()
                + fileName);
    }
}

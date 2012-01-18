package org.apache.hadoop.test.runner.common;

import java.io.File;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * This class contains the configuration for the tests execution.  
 */
public final class RunnerConfiguration {

    private static final String localClasspathDirectory = "/.unpack/";
    private static final String hdfsClasspathDirectory = "/hadoop/test/runner/classpath/";
    private static final String configClasspathEntry = "test.runner.classpath";
    
    private String classpath;
    private String sutPath;
    private Integer executionCount;
        
    private RunnerConfiguration(String classpath, String sutPath) {
        super();
        this.classpath = classpath;
        this.sutPath = sutPath;
    }

    public static File getLocalClasspathDirectoryFile() {
        return new File(localClasspathDirectory);
    }

    public static String getHDFSClasspathDirectory() {
        return hdfsClasspathDirectory;
    }

    public static String getConfigurationClasspathEntry() {
        return configClasspathEntry;
    }

    public String getClasspath() {
        return classpath;
    }

    public String getSutPath() {
        return sutPath;
    }
    
    public Integer getExecutionCount() {
        return executionCount;
    }

    /**
     * Parses the command line arguments and returns an instance
     * of the RunnerConfiguration for the given arguments.  
     * 
     * @param args - command line arguments. 
     * @return
     */
    public static RunnerConfiguration get(String[] args) {            
        try {
            return parseArgs(commandLineOptions(), args);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }        
    }
    
    private static Options commandLineOptions() {
        Options options = new Options();
        options.addOption("sut", true, "Specifies the location of a jar archive containing system to be tested.");
        options.addOption("classpath", true, "Specifies the paths where ScajaDoc will look for referenced classes (.class files) ");
        return options;
    }
    
    private static RunnerConfiguration parseArgs(Options options, String[] args) throws ParseException {
        CommandLine cmd = new BasicParser().parse(options, args);
        RunnerConfiguration configuration = null;
        if (cmd.hasOption("sut") && cmd.hasOption("classpath")) {
            configuration = new RunnerConfiguration(
                    cmd.getOptionValue("classpath"), cmd.getOptionValue("sut"));
            if (cmd.hasOption("execCount"))
                configuration.executionCount = Integer.parseInt(cmd.getOptionValue("execCount"));
        } else {
            throw new ParseException("Invalid test runner configuration.");
        }        
        return configuration;
    }
}

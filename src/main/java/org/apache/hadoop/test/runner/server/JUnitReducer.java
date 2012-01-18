package org.apache.hadoop.test.runner.server;

import java.io.IOException;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class JUnitReducer extends Reducer<Text, IntWritable, Text, BooleanWritable> {

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
        System.out.println("Recuding ");
        int failCount = 0;
        for (IntWritable i : values) {
            failCount += i.get();
        }
        context.write(key, new BooleanWritable(failCount > 0 ? false : true));
    }
    
    
    
}

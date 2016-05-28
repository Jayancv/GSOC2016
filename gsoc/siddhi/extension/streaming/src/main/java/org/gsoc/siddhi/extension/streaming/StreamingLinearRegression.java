package org.gsoc.siddhi.extension.streaming;

import scala.Tuple2;
import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.regression.LinearRegressionModel;
import org.apache.spark.mllib.regression.LinearRegressionWithSGD;
import org.apache.spark.SparkConf;
import java.util.Scanner;
import java.util.*;
import java.util.Arrays;
/**
 * Created by mahesh on 5/28/16.
 */
public class StreamingLinearRegression {
    private int paramCount = 0;                                         // Number of x variables +1
    private int calcInterval = 1;                                       // The frequency of regression calculation
    private int batchSize = 10;                                 // Maximum # of events, used for regression calculation
    private double ci = 0.95;                                           // Confidence Interval
    private int paramPosition = 0;

    private int numIterations = 100;
    private double stepSize = 0.00000001;

    public StreamingLinearRegression(int paramCount, int calcInterval, int batchSize, double ci, int numIteration, double stepSize){
            this.paramCount =paramCount;
            this.calcInterval = calcInterval;
            this.batchSize = batchSize;
            this.ci = ci;
            this.numIterations = numIteration ;
            this.stepSize      = stepSize;
    }

    public static void addToRDD(Double[] eventData){


    }
    public static Double regress(Double[] eventData){

        List <Double> data=Arrays.asList(eventData);

         return 0.0;

    }



}

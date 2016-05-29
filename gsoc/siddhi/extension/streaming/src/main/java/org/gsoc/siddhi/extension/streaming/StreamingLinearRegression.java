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
    private JavaRDD<String> events=null;
    private List<String> eventsMem=null;

    private int k=0;

    private  LinearRegressionModel model;
    SparkConf conf = null;
    JavaSparkContext sc = null;
    LinearRegressionModel prevModel=null;
    JavaRDD<LabeledPoint> eventsRDD;


    public StreamingLinearRegression(int paramCount, int calcInterval, int batchSize, double ci, int numIteration, double stepSize){

        System.out.println("StreamingLinearRegression");
            this.paramCount =paramCount;
            this.calcInterval = calcInterval;
            this.batchSize = batchSize;
            this.ci = ci;
            this.numIterations = numIteration ;
            this.stepSize      = stepSize;

            conf = new SparkConf().setMaster("local[*]").setAppName("Linear Regression Example").set("spark.driver.allowMultipleContexts", "true") ;
            sc = new JavaSparkContext(conf);
            eventsMem = new ArrayList<String>();
            k=0;

    }


    public Double regress(Double[] eventData){


           String str="";
           for (int i=0;i<paramCount;i++){
               str+= eventData[i];
               if(i!=paramCount-1)str+=",";
           }
           eventsMem.add(str);
           k++;

        if(k==batchSize){
            k=0;
            System.out.println("Start Training");
            eventsRDD=getRDD(sc,eventsMem);
            model = trainData(eventsRDD);
            eventsMem.clear();

        }

        List <Double> data=Arrays.asList(eventData);
        String eventStr = data.toString();
        System.out.println("Event String :"+eventStr);
         return 0.0;

    }

    public static JavaRDD<LabeledPoint> getRDD (JavaSparkContext sc ,List<String> events){
        System.out.println("Train-Stream-Data\n");
        JavaRDD<String> data = sc.parallelize(events);
        JavaRDD<LabeledPoint> parsedData = data.map(
                new Function<String, LabeledPoint>() {
                    public LabeledPoint call(String line) {

                        String[] features = line.split(",");
                        double[] v = new double[features.length-1];
                        for (int i = 0; i < features.length - 1; i++)
                            v[i] = Double.parseDouble(features[i+1]);
                        return new LabeledPoint(Double.parseDouble(features[0]), Vectors.dense(v));
                    }
                }
        );
        parsedData.cache();

        return parsedData;
    }


    public static LinearRegressionModel trainData (JavaRDD<LabeledPoint> parsedData) {

        // Building the model
        int numIterations = 100;
        double stepSize = 0.00000001;
        final LinearRegressionModel model =  LinearRegressionWithSGD.train(JavaRDD.toRDD(parsedData), numIterations, stepSize);

        // LinearRegressionWithSGD.train(JavaRDD.toRDD(parsedData), numIterations, stepSize,1,prevModel.weights())

        // Evaluate model on training examples and compute training error
        JavaRDD<Tuple2<Double, Double>> valuesAndPreds = parsedData.map(
                new Function<LabeledPoint, Tuple2<Double, Double>>() {
                    public Tuple2<Double, Double> call(LabeledPoint point) {
                        double prediction = model.predict(point.features());
                        return new Tuple2<Double, Double>(prediction, point.label());
                    }
                }
        );
        double MSE = new JavaDoubleRDD(valuesAndPreds.map(
                new Function<Tuple2<Double, Double>, Object>() {
                    public Object call(Tuple2<Double, Double> pair) {
                        return Math.pow(pair._1() - pair._2(), 2.0);
                    }
                }
        ).rdd()).mean();
        System.out.println("training Mean Squared Error = " + MSE);

        return model;
    }




}

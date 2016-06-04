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
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;
/**
 * Created by mahesh on 5/28/16.
 */
public class StreamingLinearRegression {
    private int learnType;
    private int paramCount = 0;                                         // Number of x variables +1
    private int batchSize = 10;                                 // Maximum # of events, used for regression calculation
    private double ci = 0.95;                                           // Confidence Interval
    private int numIterations = 100;
    private double stepSize = 0.00000001;
    private List<String> eventsMem=null;

    private  LinearRegressionModel model;
    private SparkConf conf = null;
    private JavaSparkContext sc = null;
    private LinearRegressionModel prevModel=null;
    private JavaRDD<LabeledPoint> eventsRDD;
    private boolean isBuiltModel;
    private MODEL_TYPE type;
    public enum MODEL_TYPE {BATCH_PROCESS, MOVING_WINDOW,TIME_BASED }

    public StreamingLinearRegression(int learnType,int paramCount, int batchSize, double ci, int numIteration, double stepSize){

        System.out.println("StreamingLinearRegression");
            //this.type = learnType;
            this.learnType = learnType;
            this.paramCount =paramCount;
            this.batchSize = batchSize;
            this.ci = ci;
            this.numIterations = numIteration ;
            this.stepSize      = stepSize;
            this.isBuiltModel = false;
            type=MODEL_TYPE.BATCH_PROCESS;

            conf = new SparkConf().setMaster("local[*]").setAppName("Linear Regression Example").set("spark.driver.allowMultipleContexts", "true") ;
            sc = new JavaSparkContext(conf);
            eventsMem = new ArrayList<String>();

    }

    public Double regress(Double[] eventData){

           String str="";
           for (int i=0;i<paramCount;i++){
               str+= eventData[i];
               if(i!=paramCount-1)str+=",";
           }
           eventsMem.add(str);

        double mse=0.0;

        switch(type){
            case BATCH_PROCESS:
                return regressAsBatches();

            case TIME_BASED:
                return regressAsTimeBased();

            case MOVING_WINDOW:
                return regressAsMovingWindow();

            default:
                return 0.0;
        }
    }

    public double regressAsBatches(){
        int memSize=eventsMem.size();
        if(memSize >= batchSize){

            System.out.println("Start Training");
            double mse= buildModel(eventsMem);
            eventsMem.clear();
            return mse;

        }else{
            return 0.0;
        }
    }

//Time Based Learning Model
    public double regressAsTimeBased(){
        return 0;
    }

    public double regressAsMovingWindow(){
        int memSize=eventsMem.size();
        double mse=0;
        if(memSize >= batchSize){
            int eventCounter=0;
            List<String>movingEventsMem=null;
            Iterator<String> memIter = movingEventsMem.iterator();

            while(memIter.hasNext() && eventCounter<=batchSize){
                movingEventsMem.add(memIter.next());
                eventCounter++;
            }
            mse=buildModel(movingEventsMem);
            eventsMem.remove(0);
        }else{
            mse=0;
        }
        return mse;
    }

    public double buildModel(List<String> eventsMem){

        eventsRDD=getRDD(sc,eventsMem);
        //Learning Methods
        if(!isBuiltModel) {
            isBuiltModel = true;
            model = trainData(eventsRDD, numIterations, stepSize);
        }
        else {
            model = trainStreamData(eventsRDD, numIterations, stepSize,model);
        }
        double mse= getMSE(eventsRDD,model);
        StreamingLinearRegressionModel streamModel = new StreamingLinearRegressionModel(model,mse);
        return mse;
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

    public static double getMSE(JavaRDD<LabeledPoint> parsedData,final LinearRegressionModel builtModel){

        JavaRDD<Tuple2<Double, Double>> valuesAndPreds = parsedData.map(
                new Function<LabeledPoint, Tuple2<Double, Double>>() {
                    public Tuple2<Double, Double> call(LabeledPoint point) {
                        double prediction = builtModel.predict(point.features());
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
        return MSE;
    }

    //Standalone Learning Algorithms
    public static LinearRegressionModel trainData (JavaRDD<LabeledPoint> parsedData, int numIterations, double stepSize) {
        // Building the model
        final LinearRegressionModel model =  LinearRegressionWithSGD.train(JavaRDD.toRDD(parsedData), numIterations, stepSize);
        return model;
    }


    //Incremental Learning Models
    public static LinearRegressionModel trainStreamData (JavaRDD<LabeledPoint> parsedData,int numIterations, double stepSize,LinearRegressionModel prevModel ) {
        // Building the model
        final LinearRegressionModel model = LinearRegressionWithSGD.train(JavaRDD.toRDD(parsedData), numIterations, stepSize,1,prevModel.weights());
        return model;
    }


}

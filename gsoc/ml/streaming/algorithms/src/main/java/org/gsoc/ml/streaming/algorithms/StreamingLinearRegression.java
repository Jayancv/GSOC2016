package org.gsoc.ml.streaming.algorithms;
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

public class StreamingLinearRegression {
    public static void main(String []args){

        System.out.println("LinearRegressionTest\n");
        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("Linear Regression Example").set("spark.driver.allowMultipleContexts", "true") ;
        JavaSparkContext sc = new JavaSparkContext(conf);
        LinearRegressionModel prevModel=null;

        Scanner scn= new Scanner(System.in);
        String path = scn.next();
        JavaRDD<LabeledPoint> parsedData;
        parsedData = getRDD(sc,path);
        LinearRegressionModel model= trainData(parsedData);

        System.out.println(model.weights());
        System.out.println("\n");
        //Incremental Modelling
        while(scn.hasNext()) {
            path = scn.next();
            parsedData = getRDD(sc,path);
            prevModel=model;
            model = trainStreamData(parsedData,prevModel);
            System.out.println(model.weights());
            System.out.println("\n");
        }


    }

    public static JavaRDD<LabeledPoint> getRDD (JavaSparkContext sc, String path){
        System.out.println("Train-Stream-Data\n");
        JavaRDD<String> data = sc.textFile(path);
        JavaRDD<LabeledPoint> parsedData = data.map(
                new Function<String, LabeledPoint>() {
                    public LabeledPoint call(String line) {
                        String[] parts = line.split(",");
                        String[] features = parts[1].split(" ");
                        double[] v = new double[features.length];
                        for (int i = 0; i < features.length - 1; i++)
                            v[i] = Double.parseDouble(features[i]);
                        return new LabeledPoint(Double.parseDouble(parts[0]), Vectors.dense(v));
                    }
                }
        );
        parsedData.cache();

        return parsedData;
    }

    public static LinearRegressionModel trainStreamData (JavaRDD<LabeledPoint> parsedData,LinearRegressionModel prevModel) {
        // Building the model
        int numIterations = 100;
        double stepSize = 0.00000001;
        final LinearRegressionModel model =
                LinearRegressionWithSGD.train(JavaRDD.toRDD(parsedData), numIterations, stepSize,1,prevModel.weights());
        // LinearRegressionWithSGD.train(JavaRDD.toRDD(parsedData), numIterations, stepSize);
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

package org.gsoc.samoa.streaming.clustering;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mahesh on 7/30/16.
 */
public class CepEventSimulator {

    private static final Logger logger = LoggerFactory.getLogger(CepEventSimulator.class);

    public static void main(String[] args){
        System.out.println("Starts");
        int learnType = 0;
        int paramCount = 5;
        int batchSize = 1000;
        double ci = 0.95;
        int numClusters = 2;
        int numIterations = 10;
        int alpha = 1;
       int numInsancesSent=0;
        int numAttribute = 5;
        StreamingClusteringWithSamoa streamingClusteringWithSamoa = new StreamingClusteringWithSamoa(learnType,paramCount, batchSize, ci,numClusters, numIterations,alpha);

        new Thread(streamingClusteringWithSamoa).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("Successfully Instatiated the Clustering with samoa");
        while(true){
            double [] cepEvent=new double[paramCount];
            Object[] outputData = null;
          // logger.info("Sending Next Event"+numInsancesSent++);
            // Object[] outputData= streamingLinearRegression.addToRDD(eventData);
            //Calling the regress function
            outputData = streamingClusteringWithSamoa.cluster(cepEvent);
            if(outputData == null){
              //  System.out.println("null");
            }else{
                System.out.println("Error: "+outputData[0]);
            }


            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }
}

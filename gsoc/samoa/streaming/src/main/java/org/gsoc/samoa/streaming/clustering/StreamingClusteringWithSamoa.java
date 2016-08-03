package org.gsoc.samoa.streaming.clustering;


import org.apache.samoa.moa.cluster.Cluster;
import org.apache.samoa.moa.cluster.Clustering;
import org.apache.samoa.tasks.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by mahesh on 6/4/16.
 */
public class StreamingClusteringWithSamoa extends Thread{
    private int learnType;
    private int paramCount = 0;
    private int numAttributes=0;// Number of x variables +1
    private int batchSize = 10;                                 // Maximum # of events, used for regression calculation
    private double ci = 0.95;                                           // Confidence Interval
    private int numClusters=1;
    private int numIterations = 100;
    private double alpha=0;
    private int nt=0;
    private int mt=0;
    private List<String> eventsMem=null;

    private boolean isBuiltModel;
    private MODEL_TYPE type;
    public enum MODEL_TYPE {BATCH_PROCESS, MOVING_WINDOW,TIME_BASED }

    //public  LinkedList<double[]> cepEvents;
   // public LinkedList<Clustering>samoaClusters;

    public ConcurrentLinkedQueue<double[]>cepEvents;
    public ConcurrentLinkedQueue<Clustering>samoaClusters;
    public int maxNumEvents=0;
    public int numEventsReceived=0;

    public DoClusteringTask clusteringTask;
    private static final Logger logger = LoggerFactory.getLogger(StreamingClusteringWithSamoa.class);


    public StreamingClusteringWithSamoa(int learnType,int paramCount, int batchSize, double ci, int numClusters,int numIteration, double alpha){
        this.learnType = learnType;
        this.paramCount =paramCount;
        this.numAttributes = paramCount;
        this.batchSize = batchSize;
        this.ci = ci;
        this.numClusters = numClusters;
        this.numIterations = numIteration ;
        this.alpha = alpha;
        this.isBuiltModel = false;
        type=MODEL_TYPE.BATCH_PROCESS;
       // System.out.println("A");
        ///cepEvents = new LinkedList<double[]>();
       //  System.out.println("B");
       // samoaClusters = new LinkedList<Clustering>();
       // System.out.println("C");

       cepEvents = new ConcurrentLinkedQueue<double[]>();
        samoaClusters = new  ConcurrentLinkedQueue<Clustering>();
        try {

            this.clusteringTask = new DoClusteringTask(this.numClusters,this.cepEvents, this.samoaClusters);
        }catch(Exception e){
            System.out.println(e.toString());
        }


    }

    public void run(){
        this.clusteringTask.initTask(paramCount,numClusters,batchSize);
    }

    public Object[] cluster(double[] eventData) {
       // System.out.println("Events Added to the CEP Events");
        numEventsReceived++;
       // logger.info("CEP Event Received : "+numEventsReceived);
        cepEvents.add(eventData);
        Object[] output;
        if(!samoaClusters.isEmpty()){
            output = new Object[numClusters +1];
            output[0] = 0.0;
             System.out.println("++++ We got a hit ++++");
            Clustering clusters = samoaClusters.poll();
            int dim = clusters.dimension();
             System.out.println("KMEANS CLusters size: "+ clusters.size());
            for (int i=0;i<numClusters;i++){
                Cluster cluster= clusters.get(i);
                String centerStr="";
                double [] center=cluster.getCenter();
                centerStr += center[0];
                for(int j=1;j<numAttributes;j++){
                    centerStr += (","+center[i]);
                }
                output[i+1]= centerStr;
                logger.info("cluster "+i+" center:"+centerStr);

            }
            //for(int i=0;i<dim;i++){
            //  output[i+1] = ""      }

        }else{
            output=null;
        }
        return output;
    }



}

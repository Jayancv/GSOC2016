package org.gsoc.samoa.streaming.clustering;

import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.samoa.core.ContentEvent;
import org.apache.samoa.core.Processor;
import org.apache.samoa.evaluation.ClusteringEvaluationContentEvent;
import org.apache.samoa.evaluation.ClusteringResultContentEvent;
import org.apache.samoa.instances.Instance;
import org.apache.samoa.learners.clusterers.ClusteringContentEvent;

import org.apache.samoa.moa.cluster.Cluster;
import org.apache.samoa.moa.cluster.Clustering;
import org.apache.samoa.moa.clusterers.KMeans;
import org.apache.samoa.moa.clusterers.clustream.WithKmeans;
import org.gsoc.samoa.streaming.gsocexample.GsocDestinationProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by mahesh on 7/19/16.
 */
public class MyClusteringEvaluation implements Processor{
    private static final long serialVersionUID = -6043613438148776446L;
    private int processorId;
    private static final Logger logger = LoggerFactory.getLogger(MyClusteringEvaluation.class);

    String evalPoint;
    //public LinkedList<Clustering>samoaClusters;
    //public ConcurrentLinkedQueue<double[]> cepEvents;
    public ConcurrentLinkedQueue<Clustering>samoaClusters;
    public int numClusters=0;

    Clustering gtClustering;
    MyClusteringEvaluation(String evalPoint){
        this.evalPoint = evalPoint;
    }
    @Override
    public boolean process(ContentEvent event) {

        logger.info("Process");
        if (event instanceof ClusteringContentEvent) {
            logger.info(event.getKey()+""+evalPoint+"ClusteringContentEvent");
            ClusteringContentEvent e= (ClusteringContentEvent)event;
            Instance inst = e.getInstance();

            int numAttributes=inst.numAttributes();
          //  logger.info("Attribute Size: "+numAttributes);
           /* for(int i=0;i<numAttributes;i++){
                logger.info(inst.attribute(i).toString()+""+inst.value(i)+"");
            }*/
        }

        else if(event instanceof ClusteringResultContentEvent){
            logger.info(event.getKey()+""+evalPoint+"ClusteringResultContentEvent\n");
            ClusteringResultContentEvent resultEvent = (ClusteringResultContentEvent)event;

           // Clustering clustering = KMeans.gaussianMeans(gtClustering, resultEvent.getClustering());
            Clustering clustering=resultEvent.getClustering();

            Clustering kmeansClustering = WithKmeans.kMeans_rand(numClusters,clustering);
            //Adding samoa Clusters into my class
            samoaClusters.add(kmeansClustering);

            int numClusters = clustering.size();
            logger.info("Nunber of Clustering: "+numClusters);
          /*  for(int i=0;i<numClusters;i++){
                Cluster cluster = clustering.get(i);

                logger.info("++++++Cluster"+i+"+++++");
                double []clusterCenter=cluster.getCenter();
                for(int j=0;j<clusterCenter.length;j++){
                    logger.info("Dim: "+j+":"+clusterCenter[j]);
                }
                logger.info("Cluster End\n");
            }
            logger.info("Clustering End\n\n\n");*/

        }

        else if(event instanceof ClusteringEvaluationContentEvent){
            logger.info(event.getKey()+""+evalPoint+"ClusteringEvaluationContentEvent\n");
        }
        else{
            logger.info(event.getKey()+""+evalPoint+"ContentEvent\n");
        }



        return true;
    }

    @Override
    public void onCreate(int id) {
        this.processorId = id;
    }

    @Override
    public Processor newProcessor(Processor p) {
        MyClusteringEvaluation newEval = (MyClusteringEvaluation)p;
        return newEval;
    }

    public void setSamoaClusters(ConcurrentLinkedQueue<Clustering> samoaClusters){
        this.samoaClusters = samoaClusters;
    }

    public void setNumClusters(int numClusters){
        this.numClusters = numClusters;
    }

}

package org.gsoc.samoa.streaming.clustering;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.samoa.evaluation.ClusteringEvaluatorProcessor;
import org.apache.samoa.learners.Learner;
import org.apache.samoa.learners.clusterers.simple.ClusteringDistributorProcessor;
import org.apache.samoa.learners.clusterers.simple.DistributedClusterer;
import org.apache.samoa.streams.ClusteringEntranceProcessor;
import org.apache.samoa.streams.InstanceStream;
import org.apache.samoa.streams.clustering.ClusteringStream;
import org.apache.samoa.streams.clustering.RandomRBFGeneratorEvents;
import org.apache.samoa.topology.ComponentFactory;
import org.apache.samoa.topology.Stream;
import org.apache.samoa.topology.Topology;
import org.apache.samoa.topology.TopologyBuilder;
import org.apache.samoa.tasks.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javacliparser.ClassOption;
import com.github.javacliparser.Configurable;
import com.github.javacliparser.FileOption;
import com.github.javacliparser.FloatOption;
import com.github.javacliparser.IntOption;
import com.github.javacliparser.StringOption;

/**
 * A task that runs and evaluates a distributed clustering algorithm.
 *
 */
public class MyClustering implements Task, Configurable {

    private static final long serialVersionUID = -8246537378371580550L;

    private static final int DISTRIBUTOR_PARALLELISM = 1;

    private static final Logger logger = LoggerFactory.getLogger(ClusteringEvaluation.class);

    public ClassOption learnerOption = new ClassOption("learner", 'l', "Clustering to run.", Learner.class,
            DistributedClusterer.class.getName());


    public ClassOption streamTrainOption = new ClassOption("streamTrain", 's', "Input stream.", InstanceStream.class,
            RandomRBFGeneratorEvents.class.getName());

    public IntOption instanceLimitOption = new IntOption("instanceLimit", 'i',
            "Maximum number of instances to test/train on  (-1 = no limit).", 100000, -1,
            Integer.MAX_VALUE);

    public IntOption measureCollectionTypeOption = new IntOption("measureCollectionType", 'm',
            "Type of measure collection", 0, 0, Integer.MAX_VALUE);

    public IntOption timeLimitOption = new IntOption("timeLimit", 't',
            "Maximum number of seconds to test/train for (-1 = no limit).", -1, -1,
            Integer.MAX_VALUE);

    public IntOption sampleFrequencyOption = new IntOption("sampleFrequency", 'f',
            "How many instances between samples of the learning performance.", 1000, 0,
            Integer.MAX_VALUE);

    public StringOption evaluationNameOption = new StringOption("evaluationName", 'n', "Identifier of the evaluation",
            "Clustering__"
                    + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

    public FileOption dumpFileOption = new FileOption("dumpFile", 'd', "File to append intermediate csv results to",
            null, "csv", true);

    public FloatOption samplingThresholdOption = new FloatOption("samplingThreshold", 'a',
            "Ratio of instances sampled that will be used for evaluation.", 0.5,
            0.0, 1.0);

    private MyClusteringEntranceProcessor source;
    private InstanceStream streamTrain;
    private ClusteringDistributorProcessor distributor;
    private Stream distributorStream;
    private Stream evaluationStream;

    // Default=0: no delay/waiting
    public IntOption sourceDelayOption = new IntOption("sourceDelay", 'w',
            "How many miliseconds between injections of two instances.", 0, 0, Integer.MAX_VALUE);

    private Learner learner;
    private ClusteringEvaluatorProcessor evaluator;

    private Topology topology;
    private TopologyBuilder builder;

    public void getDescription(StringBuilder sb) {
        sb.append("Clustering evaluation");
    }

    @Override
    public void init() {
        // TODO remove the if statement theoretically, dynamic binding will work
        // here! for now, the if statement is used by Storm

        if (builder == null) {
            logger.warn("Builder was not initialized, initializing it from the Task");

            builder = new TopologyBuilder();
            logger.debug("Successfully instantiating TopologyBuilder");

            builder.initTopology(evaluationNameOption.getValue(), sourceDelayOption.getValue());
            logger.debug("Successfully initializing SAMOA topology with name {}", evaluationNameOption.getValue());
        }

        // instantiate ClusteringEntranceProcessor and its output stream
        // (sourceStream)
        source = new MyClusteringEntranceProcessor();
        streamTrain = this.streamTrainOption.getValue();
        source.setStreamSource(streamTrain);
        builder.addEntranceProcessor(source);
        source.setSamplingThreshold(samplingThresholdOption.getValue());
        source.setMaxNumInstances(instanceLimitOption.getValue());
        logger.debug("Successfully instantiated ClusteringEntranceProcessor");

        Stream sourceStream = builder.createStream(source);

        //
        // starter.setInputStream(sourcePiOutputStream); // FIXME set stream in the
        // EntrancePI
        // distribution of instances and sampling for evaluation

        distributor = new ClusteringDistributorProcessor();
        builder.addProcessor(distributor, DISTRIBUTOR_PARALLELISM);
        builder.connectInputShuffleStream(sourceStream, distributor);
        distributorStream = builder.createStream(distributor);
        distributor.setOutputStream(distributorStream);
        evaluationStream = builder.createStream(distributor);
        distributor.setEvaluationStream(evaluationStream); // passes evaluation events along
        logger.debug("Successfully instantiated Distributor");
        logger.info("Successfully instantiated Distributor");


        // instantiate learner and connect it to distributorStream
        learner = this.learnerOption.getValue();
        learner.init(builder, source.getDataset(), 1);
        builder.connectInputShuffleStream(distributorStream, learner.getInputProcessor());
        logger.debug("Successfully instantiated Learner");
        logger.info("Successfully instantiated Learner");


        MyClusteringEvaluation resultCheckPoint = new MyClusteringEvaluation("Result Check Point ");
        builder.addProcessor(resultCheckPoint);

        for (Stream evaluatorPiInputStream : learner.getResultStreams()) {
            builder.connectInputShuffleStream(evaluatorPiInputStream, resultCheckPoint);
        }

        logger.debug("Successfully instantiated EvaluatocrProessor");
        logger.info("Successfully instantiated EvaluatocrProessor");

        topology = builder.build();
        logger.debug("Successfully built the topology");
    }

    @Override
    public void setFactory(ComponentFactory factory) {
        // TODO unify this code with init() for now, it's used by S4 App
        // dynamic binding theoretically will solve this problem
        builder = new TopologyBuilder(factory);
        logger.debug("Successfully instantiated TopologyBuilder");

        builder.initTopology(evaluationNameOption.getValue());
        logger.debug("Successfully initialized SAMOA topology with name {}", evaluationNameOption.getValue());

    }

    public Topology getTopology() {
        return topology;
    }
}
